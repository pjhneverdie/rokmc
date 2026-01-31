package com.pdium.security.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pdium.common.dto.ApiResponse;
import com.pdium.common.exception.AppException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
public class JwtFilterExceptionHandlingFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (AppException e) {
            sendError(response, e.getClass().getSimpleName(), e.getMessage(), e.getHttpStatus());
        }
    }

    private void sendError(HttpServletResponse response, String exceptionName, String message, HttpStatus status)
            throws IOException {
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        response.getWriter().write(
                objectMapper
                        .writeValueAsString(ApiResponse.createDefaultFailureResponse(exceptionName, message, status)));
    }

}
