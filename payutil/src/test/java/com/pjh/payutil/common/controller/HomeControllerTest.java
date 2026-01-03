package com.pjh.payutil.common.controller;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;

import com.pjh.payutil.WithMockKakaoUser;
import com.pjh.payutil.common.config.YmlInfo;
import com.pjh.payutil.security.entrypoint.UnAuthenticatedEntryPoint;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

@WebMvcTest(HomeController.class)
@Import(HomeControllerTest.TestSecurityConfig.class)
public class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockKakaoUser
    public void testHomePageWithLogin() throws Exception {

        mockMvc.perform(get("/home"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attributeExists("user"));

    }

    @Test
    public void testHomePageWithoutLogin() throws Exception {

        mockMvc.perform(get("/home"))
                .andExpect(status().is3xxRedirection()) // UnAuthenticatedEntryPoint 작동
                .andExpect(redirectedUrl("https://reimagined-orbit-4jg7r59p5qv6hjxgx-8080.app.github.dev/"));

    }

    @TestConfiguration
    @Import(YmlInfo.class)
    static class TestSecurityConfig {

        @Bean
        public UnAuthenticatedEntryPoint unAuthenticatedEntryPoint(YmlInfo ymlInfo) {
            UnAuthenticatedEntryPoint unAuthenticatedEntryPoint = new UnAuthenticatedEntryPoint(ymlInfo);

            return unAuthenticatedEntryPoint;
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http,
                UnAuthenticatedEntryPoint entryPoint) throws Exception {
            http
                    .csrf(AbstractHttpConfigurer::disable)
                    .httpBasic(AbstractHttpConfigurer::disable)
                    .formLogin(AbstractHttpConfigurer::disable)
                    .exceptionHandling(config -> config
                            .authenticationEntryPoint(entryPoint))
                    .authorizeHttpRequests(authorize -> authorize
                            .requestMatchers("/", "/css/**", "/images/**", "/js/**", "/errorPage")
                            .permitAll()
                            .anyRequest().authenticated());

            return http.build();
        }
    }

}
