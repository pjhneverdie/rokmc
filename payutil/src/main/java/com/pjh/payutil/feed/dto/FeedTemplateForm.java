package com.pjh.payutil.feed.dto;

import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FeedTemplateForm {
    @NotBlank
    @Size(min = 1, max = 15)
    private String contentTitle;

    @NotBlank
    @Size(min = 1, max = 20)
    private String contentDescription;

    @NotBlank
    @URL
    private String contentImageUrl;

    @NotBlank
    @Size(min = 1, max = 10)
    private String buttonTitle;

    @NotBlank
    @Pattern(regexp = "^https://qr\\.kakaopay\\.com/.+$")
    private String kakaopayUrl;
}
