package com.pdium.jwt.repository;

import java.time.Duration;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TokenRepositoy {

    private final StringRedisTemplate stringRedisTemplate;

    private static final String REFRESH_TOKEN_PREFIX = "rt";
    private static final String BLACKLIST_PREFIX = "black_";
    private static final String BLACKLIST_REASON = "logout";

    // 리프레쉬 토큰이 레디스에 있는지 확인하는 메서드
    public boolean isRefreshTokenExist(String email) {
        String key = REFRESH_TOKEN_PREFIX + email;

        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(key));
    }

    // 리프레쉬 토큰 저장 메서드
    public void saveRefreshToken(String email, String refreshToken, long expiration) {
        String key = REFRESH_TOKEN_PREFIX + email;

        stringRedisTemplate.opsForValue().set(key, refreshToken, Duration.ofMillis(expiration));
    }

    // 리프레쉬 토큰 삭제 메서드
    public void deleteRefreshToken(String email) {
        stringRedisTemplate.delete(REFRESH_TOKEN_PREFIX + email);
    }

    // 엑세스 토큰 블랙리스트에 추가 메서드
    public void blackAccessToken(String accessToken, long remainingExpiration) {
        String key = BLACKLIST_PREFIX + accessToken;

        stringRedisTemplate.opsForValue().set(
                key,
                BLACKLIST_REASON,
                Duration.ofMillis(remainingExpiration));
    }

    // 엑세스 토큰이 블랙리스트에 있는지 확인 메서드
    public boolean isBlacked(String accessToken) {
        String key = BLACKLIST_PREFIX + accessToken;

        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(key));
    }

}
