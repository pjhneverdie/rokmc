package com.pdium.jwt.repository;

import java.time.Duration;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TokenRepositoy {

    private final StringRedisTemplate stringRedisTemplate;

    public String saveRefreshToken(String key, String refreshToken, long validity) {
        stringRedisTemplate.opsForValue().set(key, refreshToken, Duration.ofMillis(validity));

        return refreshToken;
    }

    public boolean isRefreshTokenExist(String key) {
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(key));
    }

    public void deleteRefreshToken(String key) {
        stringRedisTemplate.delete(key);
    }

    public void blackAccessToken(String key, String reason, long validity) {
        stringRedisTemplate.opsForValue().set(
                key,
                reason,
                Duration.ofMillis(validity));
    }

    public boolean isBlacked(String key) {
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(key));
    }

}
