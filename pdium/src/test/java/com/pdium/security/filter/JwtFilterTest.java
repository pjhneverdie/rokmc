package com.pdium.security.filter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pdium.WebMvcTestHelper;
import com.pdium.TestSecurityMockConfig;
import com.pdium.common.dto.ApiResponse;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@ActiveProfiles("test")
@Import({ JwtFilterTest.TestController.class, TestSecurityMockConfig.class, WebMvcTestHelper.class })
@WebMvcTest(JwtFilterTest.TestController.class)
public class JwtFilterTest extends TestSecurityMockConfig {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebMvcTestHelper WebMvcTestHelper;

    @RestController
    public static class TestController {

        @GetMapping("/api/protected")
        public ResponseEntity<ApiResponse.Success<String>> protectedApi() {
            return new ApiResponse.Success<>("ok").toResponseEntity();
        }

    }

    // 실제로 필터와 엔트리 포인트가 잘 작동하는지 확인
    @Test
    @DisplayName("정상적인 엑세스 토큰으로 요청을 보냈을 때")
    void testWhenValidAccessTokenIsRequested() throws Exception {
        String token = "accessToken";

        when(jwtService.validateToken(token)).thenReturn(true);
        when(jwtService.toAuthentication(token)).thenReturn(any());

        ApiResponse.Success<String> response = ApiResponse.success("ok");

        mockMvc.perform(get("/api/protected")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().json(WebMvcTestHelper.toJson(response)));
    }

    // 실제로 필터와 엔트리 포인트가 잘 작동하는지 확인
    @Test
    @DisplayName("비정상적인 엑세스 토큰으로 요청을 보냈을 때")
    void testWhenInValidAccessTokenIsRequested() throws Exception {
        String token = "accessToken";

        when(jwtService.validateToken(token)).thenReturn(false);

        ApiResponse.Failure response = ApiResponse.failure("로그인이 필요한 서비스입니다.", HttpStatus.UNAUTHORIZED);

        mockMvc.perform(get("/api/protected")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().is4xxClientError())
                .andExpect(content().json(WebMvcTestHelper.toJson(response)));
    }

}
