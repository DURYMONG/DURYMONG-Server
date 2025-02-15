package konkuk.kuit.durimong.global.jwt;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import konkuk.kuit.durimong.global.exception.ErrorCode;
import konkuk.kuit.durimong.global.response.ErrorResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setHeader("Access-Control-Allow-Headers", "*");

        // 요청 속성에서 예외 유형 확인
        Object exceptionType = request.getAttribute("exception");

        ErrorCode error;
        if ("JWT_EXPIRE_TOKEN".equals(exceptionType)) {
            error = ErrorCode.JWT_EXPIRE_TOKEN;  // 403 Forbidden 반환 (토큰 만료)
        } else if ("JWT_ERROR_TOKEN".equals(exceptionType)) {
            error = ErrorCode.JWT_ERROR_TOKEN; // 403 Forbidden 반환 (잘못된 토큰)
        } else if ("JWT_LOGOUT_TOKEN".equals(exceptionType)) {
            error = ErrorCode.JWT_LOGOUT_TOKEN; // 403 Forbidden 반환 (로그아웃된 토큰)
        } else if ("INVALID_TOKEN".equals(exceptionType)) {
            error = ErrorCode.INVALID_TOKEN; // 403 Forbidden 반환 (저장된 토큰과 다름)
        } else {
            error = ErrorCode.UNAUTHORIZED;  // 401 Unauthorized 반환 (기본값)
        }

        response.setStatus(error.getHttpCode());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String json = new ObjectMapper().writeValueAsString(ErrorResponse.of(error.getErrorCode(), error.getMessage()));
        response.getWriter().write(json);
    }
}


