package com.pdium.jwt.service;

import static org.junit.Assert.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.pdium.TestRedisContainerInitializer;
import com.pdium.YamlPropertySourceFactory;
import com.pdium.jwt.config.JwtConfig;
import com.pdium.jwt.config.JwtProperties;
import com.pdium.jwt.repository.TokenRepositoy;
import com.pdium.redis.config.RedisProperties;
import com.pdium.redis.config.RedisConfig;

@TestRedisContainerInitializer // 도커 + redis 관련 설정 자바 시스템 프로퍼티에 채워 줌
@ExtendWith(SpringExtension.class) // Junit에서 스프링 테스트 콘텍스트를 사용하게 해 줌
// @SpringBootTest 너무 무거우
@ContextConfiguration(classes = { RedisConfig.class, JwtConfig.class, JwtProvider.class, TokenRepositoy.class,
        JwtService.class })
// 테스트 환경은 @ConfigurationPropertiesScan이 없음
// @EnableConfigurationProperties를 명시해서 빈에 넣어야 함
@EnableConfigurationProperties({ RedisProperties.class, JwtProperties.class })
// 테스트에서 @ConfigurationProperties 클래스에 값을 채우려면 TestPropertySource 써야 함
@TestPropertySource(locations = "classpath:application-test.yml", factory = YamlPropertySourceFactory.class)
public class JwtServiceTest {

    @Autowired
    private JwtService jwtService;

    @Test
    public void testContext() {
        assertNotNull(jwtService);
    }
}
