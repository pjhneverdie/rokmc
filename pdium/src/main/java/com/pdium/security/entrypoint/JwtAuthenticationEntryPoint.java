package com.pdium.security.entrypoint;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pdium.common.dto.ApiResponse;
import com.pdium.common.exception.AppException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        UnAuthorizedException ex = new UnAuthorizedException();

        response.getWriter()
                .write(objectMapper.writeValueAsString(
                        ApiResponse.failure(ex.getClass().getSimpleName(), ex.getMessage(), ex.getHttpStatus())));
    }

    public static class UnAuthorizedException extends AppException {

        public UnAuthorizedException() {
            super("로그인이 필요한 서비스입니다.");
        }

        @Override
        public HttpStatus getHttpStatus() {
            return HttpStatus.UNAUTHORIZED;
        }

    }

}
