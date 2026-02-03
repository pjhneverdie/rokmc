package com.pdium.security.handler;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.pdium.TestSecurityMockConfig;
import com.pdium.WebMvcTestHelper;
import com.pdium.common.dto.ApiResponse;
import com.pdium.member.controller.MemberController;
import com.pdium.mother.AuthenticationMother;
import com.pdium.security.dto.exception.InSufficientRoleException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@Import({ TestSecurityMockConfig.class, WebMvcTestHelper.class })
@WebMvcTest(MemberController.class)
public class JwtAccessDeniedHandlerTest extends TestSecurityMockConfig {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebMvcTestHelper WebMvcTestHelper;

    @Test
    void testRequestWithInSufficientRole() throws Exception {
        // Given
        // 1. 엑세스 토큰 검증 성공.
        when(jwtService.validateToken(anyString())).thenReturn("");

        // 2. 인증은 됐으나, GUEST 권한으로 인가.
        Authentication authentication = AuthenticationMother.createGuestAuthenticationForSecurityContext();

        // 3. 엑세스 토큰 클레임으로 Authentication 생성해서 SecurityContext에 설정.
        when(jwtService.toAuthentication(anyString())).thenReturn(authentication);

        // When & Then
        // 권한 부족 예외 발생하면, JwtAccessDeniedHandler가 잡아서 예외 응답을 생성.
        ApiResponse.Failure expectedResponse = ApiResponse
                .createDefaultFailureResponse(new InSufficientRoleException());

        mockMvc.perform(get(MemberController.TEST_REQUEST_PATH)
                .header("Authorization", "Bearer accessToken"))
                .andExpect(status().is4xxClientError())
                .andExpect(content().json(WebMvcTestHelper.toJson(expectedResponse)));
    }

}
