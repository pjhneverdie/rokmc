package com.pjh.payutil.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.pjh.payutil.security.oauth2.dto.KakaoUser;

@Controller
public class HomeController {

    @GetMapping("/")
    public String login(Model model) {
        return "login";
    }

    @GetMapping("/home")
    public String home(@AuthenticationPrincipal KakaoUser user, Model model) {
        System.out.println("요청왔어");
        model.addAttribute("user", user);

        return "home";
    }

}
