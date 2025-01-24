package konkuk.kuit.durimong.domain.user.service;

import jakarta.transaction.Transactional;
import konkuk.kuit.durimong.domain.mong.entity.Mong;
import konkuk.kuit.durimong.domain.mong.entity.MongQuestion;
import konkuk.kuit.durimong.domain.mong.repository.MongQuestionRepository;
import konkuk.kuit.durimong.domain.mong.repository.MongRepository;
import konkuk.kuit.durimong.domain.user.dto.request.UserEditPasswordReq;
import konkuk.kuit.durimong.domain.user.dto.request.UserInfoReq;
import konkuk.kuit.durimong.domain.user.dto.request.login.ReIssueTokenReq;
import konkuk.kuit.durimong.domain.user.dto.request.login.UserLoginReq;
import konkuk.kuit.durimong.domain.user.dto.request.signup.UserSignUpReq;
import konkuk.kuit.durimong.domain.user.dto.response.ReIssueTokenRes;
import konkuk.kuit.durimong.domain.user.dto.response.UserHomeRes;
import konkuk.kuit.durimong.domain.user.dto.response.UserTokenRes;
import konkuk.kuit.durimong.domain.user.dto.response.UserUnRegisterRes;
import konkuk.kuit.durimong.domain.user.entity.User;
import konkuk.kuit.durimong.domain.user.repository.UserRepository;
import konkuk.kuit.durimong.global.annotation.UserId;
import konkuk.kuit.durimong.global.exception.CustomException;
import konkuk.kuit.durimong.global.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import static konkuk.kuit.durimong.global.exception.ErrorCode.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private static final String AUTH_CODE_PREFIX = "AuthCode";
    private final MailService mailService;
    private final RedisService redisService;
    private final UserRepository userRepository;
    private final MongRepository mongRepository;
    private final MongQuestionRepository mongAnswerRepository;
    private final JwtProvider jwtProvider;
    @Value("${spring.mail.auth-code-expiration-millis}")
    private long authCodeExpirationMillis;

    public String validateId(String id) {
        if(userRepository.existsById(id)) {
            throw new CustomException(USER_DUPLICATE_ID);
        }
        return "사용 가능한 아이디입니다.";
    }
    public String validateEmail(String email) {
        if(userRepository.existsByEmail(email)) {
            throw new CustomException(USER_DUPLICATE_EMAIL);
        }
        return "사용 가능한 이메일입니다.";
    }
    public String validatePassword(String password) {
        if (password.length() < 6 || password.length() > 10) {
            throw new CustomException(USER_PASSWORD_SHORT);
        }

        if (!password.matches(".*[0-9].*")) {
            throw new CustomException(USER_PASSWORD_NONUM);
        }

        if (!password.matches(".*[a-zA-Z].*")) {
            throw new CustomException(USER_PASSWORD_ENGLISH);
        }

        return "사용 가능한 비밀번호입니다.";
    }
    private String createCode() {
        int length = 6;
        try {
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < length; i++) {
                builder.append(random.nextInt(10));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            log.debug("MemberService.createCode() exception occur");
            throw new CustomException(NO_SUCH_ALGORITHM);
        }
    }
    public String sendCodeToEmail(String toEmail){
        String title = "Durymong 이메일 인증 번호";
        String authcode = this.createCode();
        mailService.sendEmail(toEmail, title, authcode);
        redisService.setValues(AUTH_CODE_PREFIX + toEmail,
                authcode,
                Duration.ofMillis(authCodeExpirationMillis));
        return "인증번호가 전송되었습니다.";
    }

    public String verifyCode(String email, String authCode){
        String redisAuthCode = redisService.getValues(AUTH_CODE_PREFIX + email);
        boolean authResult = redisService.checkExistsValue(redisAuthCode) && redisAuthCode.equals(authCode);
        if(!authResult) {
            throw new CustomException(USER_EMAIL_VERIFY_FAILED);
        }
        return "인증이 완료되었습니다.";
    }

    public String register(UserSignUpReq req) {
        User findUser = userRepository.findById(req.getId()).orElseThrow(
                () -> new CustomException(USER_DUPLICATE_ID)
        );
        User user = User.create(req.getId(), req.getPassword(), req.getEmail(),req.getName(),req.getEmail());
        userRepository.save(user);
        return "회원가입이 완료되었습니다.";
    }

    public UserTokenRes login(UserLoginReq req){
        User user = userRepository.findById(req.getId())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        if (!req.getPassword().equals(user.getPassword())) {
            throw new CustomException(USER_NOT_MATCH_PASSWORD);
        }
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);
        String accessToken = jwtProvider.createAccessToken(user);
        String refreshToken = jwtProvider.createRefreshToken(user);
        jwtProvider.storeRefreshToken(refreshToken,user.getUserId());
        return new UserTokenRes(accessToken,refreshToken);
    }

    public ReIssueTokenRes reissueToken(ReIssueTokenReq req){
        String refreshToken = req.getRefreshToken();
        if(!jwtProvider.validateRefreshToken(refreshToken)) {
            throw new CustomException(JWT_EXPIRE_TOKEN);
        }
        Long userId = jwtProvider.getUserIdFromRefreshToken(refreshToken);
        if(!jwtProvider.checkTokenExists(String.valueOf(userId))) {
            throw new CustomException(BAD_REQUEST);
        }
        jwtProvider.invalidateToken(userId);

        User user = userRepository.findByUserId(userId).orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        String newAccessToken = jwtProvider.createAccessToken(user);
        String newRefreshToken = jwtProvider.createRefreshToken(user);
        return new ReIssueTokenRes(newAccessToken,newRefreshToken);

    }

    public UserHomeRes homePage(@UserId Long userId) {
        LocalDate todayDate = LocalDate.now();
        LocalDateTime today = LocalDateTime.now();
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        Mong findMong = mongRepository.findByUser(user).orElseThrow(() -> new CustomException(MONG_NOT_FOUND));
        String mongImage = findMong.getImage();
        String mongName = findMong.getName();
        LocalDateTime createdAt = findMong.getCreatedAt();
        int dateWithMong = getDateWithMong(today,createdAt);
        String mongQuestion = getDailyQuestion(userId);

        return new UserHomeRes(todayDate, dateWithMong, mongName, mongImage, mongQuestion);
    }

    private int getDateWithMong(LocalDateTime today, LocalDateTime createdAt){
        return (int) Duration.between(createdAt, today).toDays() + 1;
    }

    private String getDailyQuestion(@UserId Long userId){
        List<MongQuestion> mongAnswers = mongAnswerRepository.findAll();
        if(mongAnswers.isEmpty()){
            throw new CustomException(QUESTION_NOT_EXISTS);
        }
        LocalDate today = LocalDate.now();
        int date = today.getDayOfMonth();
        return mongAnswerRepository.findQuestionByDate(date).orElseThrow(
                () -> new CustomException(QUESTION_NOT_EXISTS)
        );

    }

    public String logout(String accessToken, Long userId){
        if(accessToken == null) {
            throw new CustomException(USER_LOGOUTED);
        }
        long accessTokenExpirationMillis = jwtProvider.getClaims(accessToken).getExpiration().getTime() - System.currentTimeMillis();
        if (accessTokenExpirationMillis > 0) {
            redisService.setValues("BLACKLIST:" + accessToken, "logout", Duration.ofMillis(accessTokenExpirationMillis));
        }
        if (jwtProvider.checkTokenExists(String.valueOf(userId))) {
            jwtProvider.invalidateToken(userId);
        }
        return "로그아웃이 완료되었습니다.";
    }

    public String editUserInfo(UserInfoReq req, Long userId){
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        if(req.getNewUserName().equals(user.getName())){
            throw new CustomException(USER_SAME_NAME);
        }
        user.setName(req.getNewUserName());
        userRepository.save(user);
        Mong mong = mongRepository.findByUser(user).orElseThrow(() -> new CustomException(MONG_NOT_FOUND));
        if(req.getNewMongName().equals(mong.getName())){
            throw new CustomException(MONG_SAME_NAME);
        }
        mong.setName(req.getNewMongName());
        mongRepository.save(mong);
        return "회원 정보 수정이 완료되었습니다.";
    }

    public String editUserPassword(UserEditPasswordReq req, Long userId){
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        if(!req.getNowPassword().equals(user.getPassword())){
            throw new CustomException(USER_NOT_MATCH_PASSWORD);
        }
        if(req.getNewPassword().equals(req.getNowPassword())){
            throw new CustomException(USER_SAME_PASSWORD);
        }

        if (req.getNewPassword().length() < 6 || req.getNewPassword().length() > 10) {
            throw new CustomException(USER_PASSWORD_SHORT);
        }

        if (!req.getNewPassword().matches(".*[0-9].*")) {
            throw new CustomException(USER_PASSWORD_NONUM);
        }

        if (!req.getNewPassword().matches(".*[a-zA-Z].*")) {
            throw new CustomException(USER_PASSWORD_ENGLISH);
        }
        user.setPassword(req.getNewPassword());
        userRepository.save(user);
        return "비밀번호 수정이 완료되었습니다.";
    }

    public UserUnRegisterRes unregister(Long userId){
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        Mong mong = mongRepository.findByUser(user).orElseThrow(() -> new CustomException(MONG_NOT_FOUND));
        LocalDateTime createdAt = mong.getCreatedAt();
        LocalDateTime today = LocalDateTime.now();
        int dateWithMong = getDateWithMong(today,createdAt);
        return new UserUnRegisterRes(user.getName(),dateWithMong,mong.getImage());
    }






}
