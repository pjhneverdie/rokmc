package com.pdium.jwt.dto;

public interface CreateTokenDto {
    String email();

    String nickname();

    String stringAuthorities();

    // 엑세스 토큰용
    record CreateAccessTokenDto(
            String email,
            String nickname,
            String stringAuthorities) implements CreateTokenDto {
    }

    // 리프레쉬 토큰용
    record CreateRefreshTokenDto(
            String email) implements CreateTokenDto {
        @Override
        public String nickname() {
            return null;
        }

        @Override
        public String stringAuthorities() {
            return null;
        }
    }
}