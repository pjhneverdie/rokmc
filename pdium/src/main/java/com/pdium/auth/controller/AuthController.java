package com.pdium.auth.controller;

import org.springframework.web.bind.annotation.RestController;

import com.pdium.auth.dto.CreateTokenDto;
import com.pdium.auth.form.LoginForm;
import com.pdium.auth.service.AuthService;
import com.pdium.common.web.dto.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse.Success<CreateTokenDto.CreateTokenResponse>> login(
            @Valid @RequestBody LoginForm loginForm) {
        CreateTokenDto.CreateTokenResponse tokenResponse = authService.createToken(loginForm.toCreateTokenRequest());

        return new ApiResponse.Success<>(tokenResponse).toResponseEntity();
    }

}
