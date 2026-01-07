package com.pjh.payutil.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Getter
@Component
public class YmlInfo {

    private final String baseUrl;
    private final String jsKey;

    public YmlInfo(@Value("${server-info.base-url}") String baseUrl,
            @Value("${kakao.js-key}") String jsKey) {
        this.baseUrl = baseUrl;
        this.jsKey = jsKey;
    }

}
