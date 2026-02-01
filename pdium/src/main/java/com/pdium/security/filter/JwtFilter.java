package com.pdium.security.filter;

import java.io.IOException;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.pdium.common.exception.AppException;
import com.pdium.jwt.service.JwtService;
import com.pdium.security.config.SecurityConfig;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    public static final String AUTHORIZATION_HEADER = "Authorization";

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return SecurityConfig.EXCLUDE_URLS.stream()
                .anyMatch(url -> pathMatcher.match(url, request.getServletPath()));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String accessToken = resolveToken(request);

        authorizeToSecurityContext(accessToken);

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }

    private void authorizeToSecurityContext(String accessToken) {
        if (StringUtils.hasText(accessToken)) {
            try {
                jwtService.validateToken(accessToken);
            } catch (AppException e) {
                throw e; // ExceptionHandlingFilter에서 처리
            }

            SecurityContextHolder.getContext()
                    .setAuthentication(jwtService.toAuthentication(accessToken));
        }
    }

}
