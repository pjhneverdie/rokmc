package com.pdium;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.GenericContainer;

public class TestRedisContainerExtension implements BeforeAllCallback {

    private static final GenericContainer<?> REDIS = new GenericContainer<>("redis:7.4-alpine")
            .withExposedPorts(6379);

    @Override
    public void beforeAll(ExtensionContext context) {
        if (!REDIS.isRunning()) {
            REDIS.start();

            System.setProperty("spring.data.redis.host", REDIS.getHost());
            System.setProperty("spring.data.redis.port", REDIS.getMappedPort(6379).toString());
        }
    }

}
