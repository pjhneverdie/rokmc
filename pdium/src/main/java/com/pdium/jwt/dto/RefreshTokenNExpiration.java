package com.pdium.jwt.dto;

import java.util.Date;

public record RefreshTokenNExpiration(String refresh, Date expiration) {
}
