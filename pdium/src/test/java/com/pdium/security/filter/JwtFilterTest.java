package com.pdium.security.filter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pdium.WebMvcTestHelper;
import com.pdium.TestSecurityMockConfig;
import com.pdium.common.dto.ApiResponse;
import com.pdium.mother.AuthenticationMother;
import com.pdium.security.entrypoint.JwtAuthenticationEntryPoint;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
            return ApiResponse.createDefaultSuccessResponse("ok").toResponseEntity();
        }

    }

    @Test
    @DisplayName("정상적인 엑세스 토큰으로 요청을 보냈을 때")
    void testWhenValidAccessTokenIsRequested() throws Exception {
        // Given
        String token = "accessToken";

        // When
        when(jwtService.validateToken(token)).thenReturn(true);
        when(jwtService.toAuthentication(token)).thenReturn(AuthenticationMother.createAdminAuthentication());

        // Then
        mockMvc.perform(get("/api/protected")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().json(WebMvcTestHelper.toJson(ApiResponse.createDefaultSuccessResponse("ok"))));
    }

    @Test
    @DisplayName("비정상적인 엑세스 토큰으로 요청을 보냈을 때")
    void testWhenInValidAccessTokenIsRequested() throws Exception {
        // Given
        String token = "accessToken";

        // When
        when(jwtService.validateToken(token)).thenReturn(false);

        // Then
        JwtAuthenticationEntryPoint.UnAuthorizedException ex = new JwtAuthenticationEntryPoint.UnAuthorizedException();

        mockMvc.perform(get("/api/protected")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().is4xxClientError())
                .andExpect(content().json(WebMvcTestHelper.toJson(ApiResponse.createDefaultFailureResponse(
                        ex.getClass().getSimpleName(),
                        ex.getMessage(),
                        ex.getHttpStatus()))));
    }

}
