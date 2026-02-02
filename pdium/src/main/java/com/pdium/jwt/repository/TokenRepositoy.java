package com.pdium.jwt.repository;

import java.time.Duration;
import java.util.Date;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TokenRepositoy {

    private final StringRedisTemplate stringRedisTemplate;

    public void saveRefreshToken(String key, String refreshToken, Date expiration) {

        // 토큰이 생성되고 응답이 나가기까지 조금이지만 차이가 있으니까,
        // 만료일에서 응답 직전 시간을 빼어 최대한 싱크로를 맞추는 거임.
        long synchronizedValidity = (expiration.getTime() - System.currentTimeMillis());

        stringRedisTemplate.opsForValue().set(key, refreshToken, Duration.ofMillis(synchronizedValidity));
    }

    public boolean isRefreshTokenExist(String key) {
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(key));
    }

    public void deleteRefreshToken(String key) {
        stringRedisTemplate.delete(key);
    }

    public void blackAccessToken(String key, String reason, Date expiration) {
        // 토큰이 생성되고 응답이 나가기까지 조금이지만 차이가 있으니까,
        // 만료일에서 응답 직전 시간을 빼어 최대한 싱크로를 맞추는 거임.
        long synchronizedValidity = (expiration.getTime() - System.currentTimeMillis());

        stringRedisTemplate.opsForValue().set(
                key,
                reason,
                Duration.ofMillis(synchronizedValidity));
    }

    public boolean isBlacked(String key) {
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(key));
    }

}
