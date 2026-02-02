package com.pdium.auth.dto;

import java.util.Date;

public record IssuedTokens(String accessToken, String refreshToken, Date refreshTokenExpiration) {

}
