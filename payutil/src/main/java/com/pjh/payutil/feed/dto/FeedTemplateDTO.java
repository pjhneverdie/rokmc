package com.pjh.payutil.feed.dto;

import org.springframework.beans.factory.annotation.Value;

public class FeedTemplateDTO {

    private final String contentTitle;
    private final String contentDescription;
    private final String contentImageURL;
    private final String buttonTitle;
    private final String kakaopayURL;
    private final String webURL;
    private final String jsKey;

    public FeedTemplateDTO(
            String contentTitle,
            String contentDescription,
            String contentImageURL,
            String buttonTitle,
            String kakaopayURL,
            @Value("${kakao.web-url}") String webURL,
            @Value("${kakao.js-key}") String jsKey) {
        this.contentTitle = contentTitle;
        this.contentDescription = contentDescription;
        this.contentImageURL = contentImageURL;
        this.buttonTitle = buttonTitle;
        this.kakaopayURL = kakaopayURL;
        this.webURL = webURL;
        this.jsKey = jsKey;
    }

}
