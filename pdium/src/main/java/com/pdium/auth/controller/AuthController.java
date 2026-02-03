package com.pdium.auth.controller;

import org.springframework.web.bind.annotation.RestController;

import com.pdium.auth.dto.AuthenticateDto;
import com.pdium.auth.dto.IssuedTokens;
import com.pdium.auth.dto.TokenResponse;
import com.pdium.auth.form.LoginForm;
import com.pdium.auth.service.AuthService;
import com.pdium.common.dto.ApiResponse;
import com.pdium.member.dto.MemberPrincipal;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
        private final AuthService authService;

        private static final String SAME_SITE_LAX = "Lax";
        private static final String REISSUE_PATH = "/api/v1/auth/reissue";
        private static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";

        @PostMapping("/login")
        public ResponseEntity<ApiResponse.Success<TokenResponse>> login(
                        @Valid @RequestBody LoginForm loginForm) {
                Authentication authentication = authService
                                .authenticate(new AuthenticateDto(loginForm.email(), loginForm.password()));

                IssuedTokens issuedTokens = authService.issueTokens((MemberPrincipal) authentication.getPrincipal());

                // 토큰이 생성되고 응답이 나가기까지 조금이지만 차이가 있으니까,
                // 만료일에서 응답 직전 시간을 빼어 최대한 싱크로를 맞추는 거임.
                long synchronizedValidity = (issuedTokens.refreshTokenExpiration().getTime()
                                - System.currentTimeMillis())
                                / 1000;

                ResponseCookie cookie = createRefreshTokenCookie(issuedTokens.refreshToken(), synchronizedValidity);

                return ResponseEntity.ok()
                                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                                .body(ApiResponse.createDefaultSuccessResponse(
                                                new TokenResponse(issuedTokens.accessToken())));
        }

        @PostMapping("/logout")
        public ResponseEntity<ApiResponse.Success<Void>> logout(
                        @AuthenticationPrincipal(expression = "#this != null") MemberPrincipal memberPrincipal) {
                authService.logout(memberPrincipal.getAccessToken());

                ResponseCookie cookie = createRefreshTokenCookie(null, 0);

                return ResponseEntity.ok()
                                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                                .body(ApiResponse.createEmptySuccessResponse());
        }

        @PostMapping("/reissue")
        public ResponseEntity<ApiResponse.Success<TokenResponse>> reissue(
                        @CookieValue(name = REFRESH_TOKEN_COOKIE_NAME, required = true) String refreshToken) {
                IssuedTokens issuedTokens = authService.reissueTokens(refreshToken);

                // 토큰이 생성되고 응답이 나가기까지 조금이지만 차이가 있으니까,
                // 만료일에서 응답 직전 시간을 빼어 최대한 싱크로를 맞추는 거임.
                long synchronizedValidity = (issuedTokens.refreshTokenExpiration().getTime()
                                - System.currentTimeMillis())
                                / 1000;

                ResponseCookie cookie = createRefreshTokenCookie(issuedTokens.refreshToken(), synchronizedValidity);

                return ResponseEntity.ok()
                                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                                .body(ApiResponse.createDefaultSuccessResponse(new TokenResponse(
                                                issuedTokens.accessToken())));
        }

        private ResponseCookie createRefreshTokenCookie(String token, long maxAge) {
                return ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, token)
                                .httpOnly(true)
                                .secure(true)
                                .path(REISSUE_PATH)
                                .maxAge(maxAge)
                                .sameSite(SAME_SITE_LAX)
                                .build();
        }

}
