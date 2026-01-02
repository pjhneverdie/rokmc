package com.pjh.payutil.common.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pjh.payutil.security.oauth2.dto.KakaoUser;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class HomeController {

    @GetMapping("/")
    public String login(@AuthenticationPrincipal KakaoUser user, Model model) {
        model.addAttribute("user", user);

        return "home";
    }

    @GetMapping("/home")
    public String home(@AuthenticationPrincipal KakaoUser user, Model model) {
        model.addAttribute("user", user);

        return "home";
    }

    // 카카오 로그인 안 되면 여기로 오게 했어
    @RequestMapping("/error")
    public String error(HttpServletRequest request, Model model) {
        String errorMessage = (String) request.getAttribute("errorMessage");

        model.addAttribute("errorMessage", errorMessage);

        return "error";
    }

}
