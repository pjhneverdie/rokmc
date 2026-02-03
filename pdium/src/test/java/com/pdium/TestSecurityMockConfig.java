package com.pdium;

import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.pdium.jwt.service.JwtService;
import com.pdium.member.service.MemberDetailsService;
import com.pdium.security.config.SecurityConfig;
import com.pdium.security.entrypoint.JwtAuthenticationEntryPoint;
import com.pdium.security.filter.JwtFilter;
import com.pdium.security.filter.JwtFilterExceptionHandlingFilter;
import com.pdium.security.handler.JwtAccessDeniedHandler;

// 시큐리티 레이어 슬라이스 테스트용 실제 컨피그 기반 목 컨피그
@Import({ SecurityConfig.class, JwtFilterExceptionHandlingFilter.class, JwtFilter.class, JwtAccessDeniedHandler.class,
        JwtAuthenticationEntryPoint.class })
public class TestSecurityMockConfig {

    @MockitoBean
    public JwtService jwtService;

    @MockitoBean
    public MemberDetailsService memberDetailsService;

}
