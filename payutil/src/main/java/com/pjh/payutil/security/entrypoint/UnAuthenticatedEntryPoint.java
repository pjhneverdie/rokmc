package com.pjh.payutil.security.entrypoint;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.pjh.payutil.common.config.YmlInfo;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UnAuthenticatedEntryPoint implements AuthenticationEntryPoint {

    private final YmlInfo ymlInfo;

    private static final String LOGIN_PATH = "/";

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        response.sendRedirect(ymlInfo.getBaseUrl() + LOGIN_PATH);
    }

}
