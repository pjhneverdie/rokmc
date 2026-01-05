package com.pjh.payutil.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import org.springframework.security.web.SecurityFilterChain;

import com.pjh.payutil.security.entrypoint.UnAuthenticatedEntryPoint;
import com.pjh.payutil.security.oauth2.handler.KakaoLoginSuccessHandler;
import com.pjh.payutil.security.oauth2.service.KakaoOAuth2UserService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

        private final KakaoOAuth2UserService kakaoOAuth2UserService;
        private final KakaoLoginSuccessHandler kakaoLoginSuccessHandler;

        private final UnAuthenticatedEntryPoint unAuthenticatedEntryPoint;

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http
                                .csrf(AbstractHttpConfigurer::disable)
                                .httpBasic(AbstractHttpConfigurer::disable)
                                .formLogin(AbstractHttpConfigurer::disable)
                                .oauth2Login((config) -> config
                                                .userInfoEndpoint(
                                                                (userInfoEndpointConfig) -> userInfoEndpointConfig
                                                                                .userService(kakaoOAuth2UserService))
                                                .successHandler(kakaoLoginSuccessHandler)

                                )
                                .exceptionHandling(
                                                (config) -> config.authenticationEntryPoint(unAuthenticatedEntryPoint))
                                .authorizeHttpRequests((authorize) -> authorize
                                                .requestMatchers("/", "/css/**", "/images/**", "/js/**", "/errorPage", "/feed/redirect/**")
                                                .permitAll()
                                                .anyRequest().authenticated());

                return http.build();
        }
}
