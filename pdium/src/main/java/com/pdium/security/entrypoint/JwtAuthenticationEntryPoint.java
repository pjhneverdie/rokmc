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
        UnAuthorizedException e = new UnAuthorizedException();

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        ApiResponse<Void> body = ApiResponse.createDefaultFailureResponse(e.getClass().getSimpleName(), e.getMessage(),
                e.getHttpStatus());

        response.getWriter().write(objectMapper.writeValueAsString(body));
    }

    public static class UnAuthorizedException extends AppException {

        public UnAuthorizedException() {
            super("login please");
        }

        @Override
        public HttpStatus getHttpStatus() {
            return HttpStatus.UNAUTHORIZED;
        }

    }

}
