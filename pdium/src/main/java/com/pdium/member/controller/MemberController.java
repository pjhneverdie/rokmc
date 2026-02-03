package com.pdium.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pdium.common.dto.ApiResponse;
import com.pdium.member.dto.MemberPrincipal;

@RestController
public class MemberController {

    public static String TEST_REQUEST_PATH = "/test/protected";

    // 걍 테스트용.
    @GetMapping("/test/protected")
    public ResponseEntity<ApiResponse.Success<String>> protectedApi(
            @AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        return ApiResponse.createDefaultSuccessResponse(memberPrincipal.getAccessToken()).toResponseEntity();
    }

}
