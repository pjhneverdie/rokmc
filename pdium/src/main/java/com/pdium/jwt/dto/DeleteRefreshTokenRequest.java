package com.pdium.jwt.dto;

import org.springframework.util.Assert;

public record DeleteRefreshTokenRequest(String email, String accessToken) {

    public DeleteRefreshTokenRequest {
        Assert.notNull(email, "email should not be null");
        Assert.notNull(accessToken, "accessToken should not be null");
    }

}
