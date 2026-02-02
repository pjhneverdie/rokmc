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

// Real SecurityConfig-based configuration for filter testing.
// Look how this is alike real one
@Import({ SecurityConfig.class, JwtFilter.class, JwtFilterExceptionHandlingFilter.class,
        JwtAuthenticationEntryPoint.class, JwtAccessDeniedHandler.class })
public class TestSecurityMockConfig {

    // This class is designed to test Security-related logic.
    // The actual implementation of JwtService is irrelevant;
    // the focus is on how filters respond to various JwtService return values.
    @MockitoBean
    protected JwtService jwtService;

    @MockitoBean
    protected MemberDetailsService memberDetailsService;

}
