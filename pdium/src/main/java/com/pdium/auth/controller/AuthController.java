package com.pdium.auth.controller;

import org.springframework.web.bind.annotation.RestController;

import com.pdium.auth.dto.AuthenticateDto;
import com.pdium.auth.dto.TokenResponse;
import com.pdium.auth.form.LoginForm;
import com.pdium.auth.service.AuthService;
import com.pdium.common.dto.ApiResponse;
import com.pdium.member.dto.MemberPrincipal;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CookieValue;
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
                                .authenticate(new AuthenticateDto(loginForm.email(), loginForm.password()));

                return ApiResponse
                                .createDefaultSuccessResponse(
                                                authService.issueToken((MemberPrincipal) authentication.getPrincipal()))
                                .toResponseEntity();
        }

        @PostMapping("/logout")
        public ResponseEntity<ApiResponse.Success<Void>> logout(
                        @AuthenticationPrincipal MemberPrincipal memberPrincipal) {
                authService.logout(memberPrincipal.getAccessToken());

                return ApiResponse.createEmptySuccessResponse().toResponseEntity();
        }

        @PostMapping("/reissue")
        public ResponseEntity<ApiResponse.Success<TokenResponse>> reissue(
                        @CookieValue(name = "refreshToken") String refreshToken) {
                return ApiResponse.createDefaultSuccessResponse(authService.reissueToken(refreshToken))
                                .toResponseEntity();
        }

}
