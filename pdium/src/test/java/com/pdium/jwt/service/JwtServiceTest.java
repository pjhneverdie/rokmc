package com.pdium.jwt.service;

import static org.junit.Assert.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.pdium.TestRedisContainerInitializer;
import com.pdium.YamlPropertySourceFactory;
import com.pdium.jwt.config.JwtProperties;
import com.pdium.mother.AuthenticationMother;
import com.pdium.redis.config.RedisAutoSetProperties;
import com.pdium.redis.config.RedisConfig;

@ActiveProfiles("test") // test 프로필 사용
@TestRedisContainerInitializer // 도커 띄우고, test 프로필에 redis 관련 설정 채워 줌
@ExtendWith(SpringExtension.class) // Junit은 스프링을 모름 DI도 모름. Junit한테 스프링 스타일 알려주는 역할
@ContextConfiguration(classes = { RedisConfig.class, JwtService.class }) // @SpringBootTest 너무 무거우
// 실제 환경에서는 @ConfigurationPropertiesScan 쓰는 중
// 테스트 환경은 @EnableConfigurationProperties 사용해야 함
// 일반적인 빈이랑은 달라서 @Import 안 먹힘 @EnableConfigurationProperties 써야 함
@EnableConfigurationProperties({ RedisAutoSetProperties.class, JwtProperties.class })
// @ExtendWith(SpringExtension.class)로 Junit한테 스프링 콘텍스트 가르쳐도 yml은 못 읽음
// 그래서 @TestPropertySource + factory = YamlPropertySourceFactory.class로 추가 학습 시키는 거임
@TestPropertySource(locations = "classpath:application-test.yml", factory = YamlPropertySourceFactory.class)
public class JwtServiceTest {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private StringRedisTemplate redisTemplate;

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

        String savedToken = redisTemplate.opsForValue().get("RT:" + authentication.getName());

        // Then
        assertNotNull(savedToken);
        assertEquals(refreshToken, savedToken);
    }

}
