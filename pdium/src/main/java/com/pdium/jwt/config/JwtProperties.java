package com.pdium.jwt.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@ConfigurationProperties(prefix = "jwt")
@RequiredArgsConstructor
public class JwtProperties {

    private final String secret;
    private final long accessTokenValidity;
    private final long refreshTokenValidity;

}
