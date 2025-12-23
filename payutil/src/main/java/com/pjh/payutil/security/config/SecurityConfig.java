package com.pjh.payutil.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import org.springframework.security.web.SecurityFilterChain;

import com.pjh.payutil.security.oauth2.service.KakaoOAuth2UserService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

        private final KakaoOAuth2UserService customOAuth2UserService;

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http
                                .csrf(AbstractHttpConfigurer::disable)
                                .httpBasic(AbstractHttpConfigurer::disable)
                                .formLogin(AbstractHttpConfigurer::disable)
                                .oauth2Login(
                                                (config) -> config
                                                                .userInfoEndpoint(
                                                                                (userInfoEndpointConfig) -> userInfoEndpointConfig
                                                                                                .userService(customOAuth2UserService))
                                                                .successHandler((request, response, authentication) -> {
                                                                        System.out.println("성공성공");
                                                                        System.out.println("성공성공");
                                                                        System.out.println("성공성공");
                                                                        System.out.println("성공성공");
                                                                        response.sendRedirect("/home");
                                                                }))
                                .authorizeHttpRequests((authorize) -> authorize
                                                .requestMatchers("/").permitAll()
                                                .anyRequest().authenticated());

                return http.build();
        }

}
