package com.pjh.payutil.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Getter
@Component
public class YmlInfo {

    private final String baseURL;
    private final String jsKey;

    public YmlInfo(@Value("${server-info.base-url}") String baseURL,
            @Value("${kakao.js-key}") String jsKey) {
        this.baseURL = baseURL;
        this.jsKey = jsKey;
    }

}
