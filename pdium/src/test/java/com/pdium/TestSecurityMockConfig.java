package com.pdium;

import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.pdium.jwt.service.JwtService;
import com.pdium.member.service.MemberDetailsService;
import com.pdium.security.config.SecurityConfig;
import com.pdium.security.entrypoint.JwtAuthenticationEntryPoint;
import com.pdium.security.filter.JwtFilter;

// SecurityConfig 그대로 목킹
@Import({ SecurityConfig.class, JwtFilter.class, JwtAuthenticationEntryPoint.class })
public class TestSecurityMockConfig {

    @MockitoBean
    protected JwtService jwtService;

    @MockitoBean
    protected MemberDetailsService memberDetailsService;

}
