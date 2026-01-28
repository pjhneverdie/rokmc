package com.pdium.jwt.service;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.security.core.Authentication;

import com.pdium.jwt.config.JwtProperties;
import com.pdium.mother.AuthenticationMother;

public class JwtServiceTest {

    private JwtService jwtService = new JwtService(new JwtProperties(
            "v9yBEHMcQfTjWnZr4u7xACFJaNdRgUkXp2s5v8yBEGKbPeShVmYq3tv9yBEHMcQfTjdsakjdsajqqhfhfehfhdsd", 3600000,
            3600000));

    @Test
    @DisplayName("엑세스 토큰 생성, 검증, Authentication으로 변환 테스트")
    public void testCreateAccessToken() {
        // Given
        Authentication authentication = AuthenticationMother.createAdminAuthentication();

        // When
        String accessToken = jwtService.createAccessToken(authentication);

        // Then
        assertTrue(jwtService.validateToken(accessToken));
        assertEquals(authentication.getName(), jwtService.toAuthentication(accessToken).getName());
    }

    @Test
    @DisplayName("리프레쉬 토큰 생성, 검증 성공 및 Authentication으로 변환 실패 테스트")
    public void testCreateRefreshToken() {
        // Given
        Authentication authentication = AuthenticationMother.createAdminAuthentication();

        // When
        String refreshToken = jwtService.createRefreshToken(authentication);

        // Then
        assertTrue(jwtService.validateToken(refreshToken));
        assertThrows(IllegalArgumentException.class, () -> {
            jwtService.toAuthentication(refreshToken);
        });
    }

}
