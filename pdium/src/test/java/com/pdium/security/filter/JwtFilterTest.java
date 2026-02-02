package com.pdium.security.filter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pdium.WebMvcTestHelper;
import com.pdium.TestSecurityMockConfig;
import com.pdium.common.dto.ApiResponse;
import com.pdium.common.exception.AppException;
import com.pdium.jwt.dto.exception.ExpiredTokenException;
import com.pdium.jwt.dto.exception.InvalidTokenException;
import com.pdium.member.dto.MemberPrincipal;
import com.pdium.mother.AuthenticationMother;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.stream.Stream;

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
        public ResponseEntity<ApiResponse.Success<String>> protectedApi(
                @AuthenticationPrincipal MemberPrincipal memberPrincipal) {
            return ApiResponse.createDefaultSuccessResponse(memberPrincipal.getAccessToken()).toResponseEntity();
        }

    }

    @Test
    void testWhenValidAccessTokenIsRequested() throws Exception {
        // Given
        when(jwtService.validateToken(anyString())).thenReturn("");

        Authentication authentication = AuthenticationMother.createAuthenticationForSecurityContext();
        MemberPrincipal memberPrincipal = (MemberPrincipal) authentication.getPrincipal();
        when(jwtService.toAuthentication(anyString())).thenReturn(authentication);

        // When & Then
        mockMvc.perform(get("/api/protected")
                .header("Authorization", "Bearer accessToken"))
                .andExpect(status().isOk())
                .andExpect(content().json(WebMvcTestHelper.toJson(
                        ApiResponse.createDefaultSuccessResponse(memberPrincipal.getAccessToken()))));
    }

    @ParameterizedTest
    @MethodSource("provideExceptions")
    void testWhenInvalidAccessTokenIsRequested(AppException e) throws Exception {
        // Given
        when(jwtService.validateToken(anyString())).thenThrow(e);

        // When & Then
        mockMvc.perform(get("/api/protected")
                .header("Authorization", "Bearer accessToken"))
                .andExpect(status().is(e.getHttpStatus().value()))
                .andExpect(content().json(WebMvcTestHelper.toJson(
                        ApiResponse.createDefaultFailureResponse(
                                e.getClass().getSimpleName(),
                                e.getMessage(),
                                e.getHttpStatus()))));
    }

    static Stream<Arguments> provideExceptions() {
        return Stream.of(
                Arguments.of(new ExpiredTokenException()),
                Arguments.of(new InvalidTokenException()));
    }

}
