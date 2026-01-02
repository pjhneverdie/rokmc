package com.pjh.payutil.feed.dto;

import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FeedTemplateForm {
    @NotBlank
    @Size(min = 1, max = 15)
    private final String contentTitle;

    @NotBlank
    @Size(min = 1, max = 20)
    private final String contentDescription;

    @NotBlank
    @URL
    private final String contentImageURL;

    @NotBlank
    @Size(min = 1, max = 10)
    private final String buttonTitle;

    @NotBlank
    @Pattern(regexp = "^https://qr\\.kakaopay\\.com/.+$")
    private final String kakaopayURL;
}
