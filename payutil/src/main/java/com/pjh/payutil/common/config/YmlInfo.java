package com.pjh.payutil.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Getter
@Component
public class YmlInfo {

    private final String baseURL;
    private final int port;
    private final String webURL;
    private final String jsKey;

    public YmlInfo(@Value("${server-info.base-url}") String baseURL,
            @Value("${server-info.port}") int port,
            @Value("${kakao.web-url}") String webURL,
            @Value("${kakao.js-key}") String jsKey) {
        this.baseURL = baseURL;
        this.port = port;
        this.webURL = webURL;
        this.jsKey = jsKey;
    }

}
