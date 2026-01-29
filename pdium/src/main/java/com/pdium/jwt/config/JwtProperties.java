package com.pdium.jwt.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(
        String secret,
        long accessTokenValidity,
        long refreshTokenValidity) {
}