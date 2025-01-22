package konkuk.kuit.durimong.global.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import konkuk.kuit.durimong.domain.user.entity.User;
import konkuk.kuit.durimong.global.exception.CustomException;
import konkuk.kuit.durimong.global.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class JwtProvider {

    private final Long ACCESS_TOKEN_EXPIRE_MILLIS;
    private final Long REFRESH_TOKEN_EXPIRE_MILLIS;
    private final SecretKey secretKey;

    private final RedisTemplate<String, Object> redisTemplate;


    public JwtProvider(@Value("${jwt.secret}") String secretKey,
                       @Value("${jwt.accessTokenExpiration}") Long accessTokenExpiration,
                       @Value("${jwt.refreshTokenExpiration}") Long refreshTokenExpiration, RedisTemplate<String, Object> redisTemplate) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.ACCESS_TOKEN_EXPIRE_MILLIS = accessTokenExpiration;
        this.REFRESH_TOKEN_EXPIRE_MILLIS = refreshTokenExpiration;
        this.redisTemplate = redisTemplate;
    }

    public String createAccessToken(User user) {
        Date now = new Date();
        return Jwts.builder()
                .claim("category", "access")
                .claim("userId", user.getUserId())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_MILLIS))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String createRefreshToken(User user) {
        Date now = new Date();
        return Jwts.builder()
                .claim("category","refresh")
                .claim("userId",user.getUserId())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + REFRESH_TOKEN_EXPIRE_MILLIS))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }
    public void storeRefreshToken(String token, Long userId) {
        redisTemplate.opsForValue().set(String.valueOf(userId), token, REFRESH_TOKEN_EXPIRE_MILLIS, TimeUnit.MILLISECONDS);
    }

    public boolean checkTokenExists(String userId) {
        Boolean result = redisTemplate.hasKey(userId);
        return result != null && result;
    }

    public void invalidateToken(Long userId) {
        redisTemplate.delete(String.valueOf(userId));
    }

    public Long getUserIdFromRefreshToken(String refreshToken) {
        Claims claims = getClaims(refreshToken);
        if (!"refresh".equals(claims.get("category"))) {
            throw new CustomException(ErrorCode.JWT_ERROR_TOKEN); // 적절한 에러 코드 설정
        }
        return Long.parseLong(claims.get("userId").toString());
    }


    public boolean validateRefreshToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            Date expiration = claims.getExpiration();
            if (expiration.before(new Date())) {
                throw new CustomException(ErrorCode.JWT_EXPIRE_TOKEN);
            }
            if (!"refresh".equals(claims.get("category"))) {
                throw new CustomException(ErrorCode.JWT_ERROR_TOKEN);
            }

            return true;
        } catch (ExpiredJwtException e) {
            throw new CustomException(ErrorCode.JWT_EXPIRE_TOKEN);
        } catch (JwtException | IllegalArgumentException e) {
            throw new CustomException(ErrorCode.JWT_ERROR_TOKEN);
        }
    }


    public boolean verify(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
        } catch (SecurityException | MalformedJwtException e) {
            log.debug("잘못된 Jwt 서명입니다.");
            throw new CustomException(ErrorCode.JWT_ERROR_TOKEN);
        } catch (ExpiredJwtException e) {
            log.debug("만료된 토큰입니다.");
            throw new CustomException(ErrorCode.JWT_EXPIRE_TOKEN);
        } catch (UnsupportedJwtException e) {
            log.debug("지원하지 않는 토큰입니다.");
            throw new CustomException(ErrorCode.JWT_ERROR_TOKEN);
        } catch (IllegalArgumentException e) {
            log.debug("잘못된 토큰입니다.");
            throw new CustomException(ErrorCode.JWT_ERROR_TOKEN);
        } catch (Exception e) {
            log.debug(e.getMessage());
            throw new CustomException(ErrorCode.JWT_ERROR_TOKEN);
        }

        return true;
    }

    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Long getUserId(String token) {
        return Long.parseLong(getClaims(token).get("userId").toString());
    }

}

