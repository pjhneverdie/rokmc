package com.pjh.payutil.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Getter
@Component
public class ServerInfo {

    private final String baseURL;
    private final int port;

    public ServerInfo(@Value("${server-info.base-url}") String baseURL,
            @Value("${server-info.port}") int port) {
        this.baseURL = baseURL;
        this.port = port;
    }

}
