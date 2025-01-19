package konkuk.kuit.durimong.domain.user.service;

import jakarta.transaction.Transactional;
import konkuk.kuit.durimong.domain.user.repository.UserRepository;
import konkuk.kuit.durimong.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
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



}
