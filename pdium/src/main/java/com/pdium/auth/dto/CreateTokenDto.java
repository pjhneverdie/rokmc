package com.pdium.auth.dto;

public abstract class CreateTokenDto {
    public record CreateTokenRequest(String id, String password) {
    }

    public record CreateTokenResponse(String accessToken, String refreshToken) {
    }
}
