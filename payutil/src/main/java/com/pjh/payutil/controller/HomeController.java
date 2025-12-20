package com.pjh.payutil.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String kakaoClientId;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String kakaoRedirectUri;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("kakaoClientId", kakaoClientId);
        model.addAttribute("kakaoRedirectUri", kakaoRedirectUri);
        return "home";
    }

    @GetMapping("/welcome")
    public String welcome(Model model) {
        return "home";
    }
}
