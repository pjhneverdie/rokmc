package com.pdium.security.filter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.pdium.WebMvcTestHelper;
import com.pdium.TestSecurityMockConfig;
import com.pdium.common.dto.ApiResponse;
import com.pdium.common.exception.AppException;
import com.pdium.jwt.dto.exception.ExpiredTokenException;
import com.pdium.jwt.dto.exception.InvalidTokenException;
import com.pdium.member.controller.MemberController;
import com.pdium.member.dto.MemberPrincipal;
import com.pdium.mother.AuthenticationMother;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.stream.Stream;

@ActiveProfiles("test")
@Import({ TestSecurityMockConfig.class, WebMvcTestHelper.class })
@WebMvcTest(MemberController.class)
public class JwtFilterTest extends TestSecurityMockConfig {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebMvcTestHelper WebMvcTestHelper;

    @Test
    void testWhenValidAccessTokenIsRequested() throws Exception {
        // Given
        // 1. 엑세스 토큰 검증 성공.
        when(jwtService.validateToken(anyString())).thenReturn("");

        Authentication authentication = AuthenticationMother.createAdminAuthenticationForSecurityContext();
        // 2. 엑세스 토큰 클레임으로 Authentication 생성해서 SecurityContext에 설정.
        when(jwtService.toAuthentication(anyString())).thenReturn(authentication);

        // When & Then
        // 컨트롤러에 성공적으로 요청 전달 완료 및 컨트롤러에서 SecurityContext 사용.
        MemberPrincipal memberPrincipal = (MemberPrincipal) authentication.getPrincipal();
        ApiResponse.Success<String> expectedResponse = ApiResponse
                .createDefaultSuccessResponse(memberPrincipal.getAccessToken());

        mockMvc.perform(get(MemberController.TEST_REQUEST_PATH)
                .header("Authorization", "Bearer accessToken"))
                .andExpect(status().isOk())
                .andExpect(content().json(WebMvcTestHelper.toJson(expectedResponse)));
    }

    @ParameterizedTest
    @MethodSource("provideExceptions")
    void testWhenInvalidAccessTokenIsRequested(AppException e) throws Exception {
        // Given
        // 1. 엑세스 토큰 검증 실패.
        when(jwtService.validateToken(anyString())).thenThrow(e);

        // When & Then
        // JwtFilterExceptionHandlingFilter가 예외 잡아서 401 응답 반환.
        ApiResponse.Failure expectedResponse = ApiResponse.createDefaultFailureResponse(e);

        mockMvc.perform(get(MemberController.TEST_REQUEST_PATH)
                .header("Authorization", "Bearer accessToken"))
                .andExpect(status().is(e.getHttpStatus().value()))
                .andExpect(content().json(WebMvcTestHelper.toJson(expectedResponse)));
    }

    static Stream<Arguments> provideExceptions() {
        return Stream.of(
                Arguments.of(new ExpiredTokenException()),
                Arguments.of(new InvalidTokenException()));
    }

}
