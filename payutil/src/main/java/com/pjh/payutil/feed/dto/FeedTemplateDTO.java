package com.pjh.payutil.feed.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class FeedTemplateDTO {
    private final String contentTitle;
    private final String contentDescription;
    private final String contentImageURL;
    private final String buttonTitle;
    private final String kakaopayURL;
    private final String webURL;
    private final String jsKey;
}
