package com.pdium.jwt.service;

import static org.junit.Assert.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.pdium.TestRedisContainerInitializer;
import com.pdium.YamlPropertySourceFactory;
import com.pdium.jwt.config.JwtProperties;
import com.pdium.mother.AuthenticationMother;
import com.pdium.redis.config.RedisProperties;
import com.pdium.redis.config.RedisConfig;

@TestRedisContainerInitializer // 도커 + redis 관련 설정 자바 시스템 프로퍼티에 채워 줌
@ExtendWith(SpringExtension.class) // Junit에서 스프링 테스트 콘텍스트를 사용하게 해 줌
@ContextConfiguration(classes = { RedisConfig.class, JwtService.class }) // @SpringBootTest 너무 무거우
// 테스트 환경은 @ConfigurationPropertiesScan가 없음
// @EnableConfigurationProperties를 명시해서 빈에 넣어야 함
@EnableConfigurationProperties({ RedisProperties.class, JwtProperties.class })
// 왜인지는 모르겠지만 @ActiveProfiles가 안 들음.. TestPropertySource 써야 함
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
    @DisplayName("리프레쉬 토큰 생성, 저장, 검증 성공 및 Authentication으로 변환 실패 테스트")
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

        String savedToken = redisTemplate.opsForValue().get("RT" + authentication.getName());

        // Then
        assertNotNull(savedToken);
        assertEquals(refreshToken, savedToken);
    }

}
