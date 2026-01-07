package com.pjh.payutil.common.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;

import com.pjh.payutil.common.config.YmlInfo;
import com.pjh.payutil.security.oauth2.dto.KakaoUser;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class HomeController {
    private final YmlInfo ymlInfo;

    @GetMapping("/")
    public String login(@AuthenticationPrincipal KakaoUser user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("jsKey", ymlInfo.getJsKey());
        model.addAttribute("baseUrl", ymlInfo.getBaseUrl());

        return "home";
    }

    @GetMapping("/home")
    public String home(@AuthenticationPrincipal KakaoUser user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("jsKey", ymlInfo.getJsKey());
        model.addAttribute("baseUrl", ymlInfo.getBaseUrl());

        return "home";
    }

}
