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
            String email,
            String nickname,
            String stringAuthorities) implements CreateTokenDto {
    }

    // 지금은 엑세스, 리프레쉬 토큰 클레임이 똑같지만 나중에 바뀔 수도 있으니까 일단 무조건 넣을 것만 추상화!
}