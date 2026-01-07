package com.pjh.payutil.security.oauth2.handler;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.pjh.payutil.common.config.YmlInfo;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class KakaoLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final YmlInfo serverInfo;

    private static final String HOME_PATH = "/home";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        response.sendRedirect(serverInfo.getBaseUrl() + HOME_PATH);
    }

}
