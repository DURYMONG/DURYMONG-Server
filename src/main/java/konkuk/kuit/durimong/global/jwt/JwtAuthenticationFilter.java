package konkuk.kuit.durimong.global.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setHeader("Access-Control-Allow-Headers", "*");

        // Swagger UI 경로를 제외한 요청에서만 인증을 적용
        if (shouldNotFilter(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = getToken(request);
        if (token != null && jwtProvider.verify(token)) {
            Authentication authentication = getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
    /**
     * Swagger UI 또는 Swagger 관련 경로를 제외한 요청에 대해서만 필터를 적용
     */
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return uri.startsWith("/swagger-ui") || uri.startsWith("/v3/api-docs") || uri.startsWith("/swagger-config");
    }

    private String getToken(HttpServletRequest request) {
        String authorization = request.getHeader("authorization");
        String validTokenPrefix = "Bearer ";
        if (authorization == null || !authorization.startsWith(validTokenPrefix)) {
            return null;
        }
        return authorization.substring(validTokenPrefix.length()).trim();
    }

    private Authentication getAuthentication(String token) {
        Long usIdx = jwtProvider.getUserId(token);
        return new JwtTokenAuthentication(usIdx);
    }
}
