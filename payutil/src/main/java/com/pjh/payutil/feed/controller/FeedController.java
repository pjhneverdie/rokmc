package com.pjh.payutil.feed.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.pjh.payutil.common.config.YmlInfo;
import com.pjh.payutil.common.exception.ApiResponse;
import com.pjh.payutil.feed.dto.FeedTemplateDTO;
import com.pjh.payutil.feed.dto.FeedTemplateForm;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@RestController
public class FeedController {

    private final YmlInfo ymlInfo;

    @PostMapping("/feed/share")
    public ResponseEntity<ApiResponse<FeedTemplateDTO>> share(@Valid @RequestBody FeedTemplateForm FeedTemplateForm) {

        FeedTemplateDTO feedTemplateDTO = new FeedTemplateDTO(
                FeedTemplateForm.getContentTitle(),
                FeedTemplateForm.getContentDescription(),
                FeedTemplateForm.getContentImageURL(),
                FeedTemplateForm.getButtonTitle(),
                FeedTemplateForm.getKakaopayURL(),
                ymlInfo.getWebURL(),
                ymlInfo.getJsKey());

        return ResponseEntity.ok(new ApiResponse<FeedTemplateDTO>(feedTemplateDTO, null));

    }

}
