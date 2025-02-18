package konkuk.kuit.durimong.domain.auth.service;

import konkuk.kuit.durimong.domain.auth.dto.request.ReIssueTokenReq;
import konkuk.kuit.durimong.domain.auth.dto.request.UserLoginReq;
import konkuk.kuit.durimong.domain.auth.dto.response.ReIssueTokenRes;
import konkuk.kuit.durimong.domain.auth.dto.response.UserTokenRes;
import konkuk.kuit.durimong.domain.user.entity.User;
import konkuk.kuit.durimong.domain.user.repository.UserRepository;
import konkuk.kuit.durimong.global.exception.CustomException;
import konkuk.kuit.durimong.global.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;

import static konkuk.kuit.durimong.global.exception.ErrorCode.*;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final RedisService redisService;

    public UserTokenRes login(UserLoginReq req){
        if(req.getPassword().isEmpty()){
            throw new CustomException(LOGIN_PASSWORD_EMPTY);
        }
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
}
