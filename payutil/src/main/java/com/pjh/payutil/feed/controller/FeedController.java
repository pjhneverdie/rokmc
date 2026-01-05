package com.pjh.payutil.feed.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.pjh.payutil.common.config.YmlInfo;
import com.pjh.payutil.common.exception.ApiResponse;
import com.pjh.payutil.feed.dto.FeedTemplateDTO;
import com.pjh.payutil.feed.dto.FeedTemplateForm;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@RestController
public class FeedController {

    private final YmlInfo ymlInfo;

    private static final String KAKAOPAY_BASE_URL = "https://qr.kakaopay.com";

    private static final String QR_REDIRECT_PATH = "/feed/redirect";

    @PostMapping("/feed/share")
    public ResponseEntity<ApiResponse<FeedTemplateDTO>> share(@Valid @RequestBody FeedTemplateForm FeedTemplateForm) {
        String[] payURLSplits = FeedTemplateForm.getKakaopayURL().split("/");
        String payQrId = payURLSplits[payURLSplits.length - 1];

        FeedTemplateDTO feedTemplateDTO = new FeedTemplateDTO(
                FeedTemplateForm.getContentTitle(),
                FeedTemplateForm.getContentDescription(),
                FeedTemplateForm.getContentImageURL(),
                FeedTemplateForm.getButtonTitle(),
                ymlInfo.getBaseURL() + QR_REDIRECT_PATH + "/" + payQrId);

        return ResponseEntity.ok(new ApiResponse<FeedTemplateDTO>(feedTemplateDTO, null));
    }

    @GetMapping("/feed/redirect/{payQrId}")
    public void redirectKakaoPay(
            @PathVariable("payQrId") String payQrId,
            HttpServletResponse response) throws IOException {
        response.sendRedirect(KAKAOPAY_BASE_URL + "/" + payQrId);
    }

}