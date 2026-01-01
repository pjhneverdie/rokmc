package com.pjh.payutil.feed.dto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FeedTemplateForm {
    private final String contentTitle;
    private final String contentDescription;
    private final String contentImageURL;
    private final String buttonTitle;
    private final String kakaopayURL;
}
