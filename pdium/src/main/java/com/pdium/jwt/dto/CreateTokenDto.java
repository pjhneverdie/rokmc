package com.pdium.jwt.dto;

import java.util.Date;

public interface CreateTokenDto {

        String email();

        Date issuedAt();

        record CreateAccessTokenDto(
                        String email,
                        String nickname,
                        String stringAuthorities,
                        Date issuedAt) implements CreateTokenDto {
        }

        record CreateRefreshTokenDto(String email, Date issuedAt) implements CreateTokenDto {
        }

}