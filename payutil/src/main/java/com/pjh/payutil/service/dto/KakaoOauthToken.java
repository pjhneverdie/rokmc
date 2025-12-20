package com.pjh.payutil.service.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Getter
@RequiredArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoOauthToken {
    private final String tokenType;
    private final String accessToken;
    private final String idToken;
    private final Integer expiresIn;
    private final String refreshToken;
    private final Integer refreshTokenExpiresIn;
    private final String scope;
}
