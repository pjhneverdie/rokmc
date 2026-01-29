package com.pdium.redis.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

// spring-boot-docker-compose가 알아서 넣을 거야.
@ConfigurationProperties(prefix = "spring.data.redis")
public record RedisAutoSetProperties(String host, int port) {
}