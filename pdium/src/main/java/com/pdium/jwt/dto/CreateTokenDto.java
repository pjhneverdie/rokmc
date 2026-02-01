package com.pdium.jwt.dto;

public interface CreateTokenDto {

        String email();

        record CreateAccessTokenDto(
                        String email,
                        String nickname,
                        String stringAuthorities) implements CreateTokenDto {
        }

        record CreateRefreshTokenDto(String email) implements CreateTokenDto {
        }

}