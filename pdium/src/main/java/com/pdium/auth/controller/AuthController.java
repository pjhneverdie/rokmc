package com.pdium.auth.controller;

import org.springframework.web.bind.annotation.RestController;

import com.pdium.auth.dto.AuthenticateRequest;
import com.pdium.auth.dto.TokenResponse;
import com.pdium.auth.form.LoginForm;
import com.pdium.auth.service.AuthService;
import com.pdium.common.dto.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse.Success<TokenResponse>> login(
            @Valid @RequestBody LoginForm loginForm) {

        Authentication authentication = authService
                .authenticate(new AuthenticateRequest(loginForm.email(), loginForm.password()));

        return ApiResponse.createDefaultSuccessResponse(authService.issueToken(authentication))
                .toResponseEntity();
    }

}
