package com.pdium.jwt.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public record JwtProperties(
                @Value("${jwt.secret}") String secret,
                @Value("${jwt.access-token-validity}") long accessTokenValidity,
                @Value("${jwt.refresh-token-validity}") long refreshTokenValidity) {
}