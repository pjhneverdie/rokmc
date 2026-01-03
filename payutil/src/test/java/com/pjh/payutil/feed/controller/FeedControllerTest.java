package com.pjh.payutil.feed.controller;

import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pjh.payutil.WithMockKakaoUser;
import com.pjh.payutil.common.config.YmlInfo;
import com.pjh.payutil.common.exception.ApiResponse;
import com.pjh.payutil.feed.dto.FeedTemplateDTO;
import com.pjh.payutil.feed.dto.FeedTemplateForm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(FeedController.class)
@Import({ YmlInfo.class, FeedControllerTest.TestSecurityConfig.class })
public class FeedControllerTest {
    @Autowired
    private YmlInfo ymlInfo;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockKakaoUser
    void testShare() throws Exception {
        FeedTemplateForm feedTemplateForm = new FeedTemplateForm(
                "테스트 제목",
                "테스트 설명",
                "https://example.com/image.png",
                "버튼",
                "https://qr.kakaopay.com/abcd1234");

        FeedTemplateDTO feedTemplateDTO = new FeedTemplateDTO(
                feedTemplateForm.getContentTitle(),
                feedTemplateForm.getContentDescription(),
                feedTemplateForm.getContentImageURL(),
                feedTemplateForm.getButtonTitle(),
                feedTemplateForm.getKakaopayURL(),
                ymlInfo.getWebURL(),
                ymlInfo.getJsKey());

        mockMvc.perform(post("/feed/share")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(feedTemplateForm)))
                .andExpect(status().isOk())
                .andExpect(content().json(toJson(createApiResponse(feedTemplateDTO))));
    }

    private <T> ApiResponse<T> createApiResponse(T responseBody) {
        return new ApiResponse<>(responseBody, null);
    }

    private String toJson(Object obj) throws Exception {
        return objectMapper.writeValueAsString(obj);
    }

    @TestConfiguration
    static class TestSecurityConfig {

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http
                    .csrf(AbstractHttpConfigurer::disable)
                    .httpBasic(AbstractHttpConfigurer::disable)
                    .formLogin(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests(authorize -> authorize
                            .requestMatchers("/", "/css/**", "/images/**", "/js/**", "/errorPage")
                            .permitAll()
                            .anyRequest().authenticated());

            return http.build();
        }
    }
}
