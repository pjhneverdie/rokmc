package com.pdium.security.entrypoint;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.pdium.TestSecurityMockConfig;
import com.pdium.WebMvcTestHelper;
import com.pdium.common.dto.ApiResponse;
import com.pdium.member.controller.MemberController;
import com.pdium.security.dto.exception.UnAuthorizedException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@Import({ TestSecurityMockConfig.class, WebMvcTestHelper.class })
@WebMvcTest(MemberController.class)
public class JwtAuthenticationEntryPointTest extends TestSecurityMockConfig {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebMvcTestHelper WebMvcTestHelper;

    @Test
    void testRequestWithoutLogin() throws Exception {
        // When & Then
        // 로그인 보내면 UnAuthorizedException 예외 응답.
        ApiResponse.Failure expectedResponse = ApiResponse
                .createDefaultFailureResponse(new UnAuthorizedException());

        mockMvc.perform(get(MemberController.TEST_REQUEST_PATH))
                .andExpect(status().is4xxClientError())
                .andExpect(content().json(WebMvcTestHelper.toJson(expectedResponse)));
    }

}
