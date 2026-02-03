package com.pdium.auth.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jayway.jsonpath.JsonPath;
import com.pdium.TestMysqlContainerInitializer;
import com.pdium.TestRedisContainerInitializer;
import com.pdium.WebMvcTestHelper;
import com.pdium.auth.form.LoginForm;
import com.pdium.member.domain.Member;
import com.pdium.member.dto.MemberPrincipal;
import com.pdium.member.enum_type.MemberRole;
import com.pdium.member.repository.MemberRepository;

import jakarta.servlet.http.Cookie;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

@TestMysqlContainerInitializer
@TestRedisContainerInitializer
@SpringBootTest
@EnableJpaAuditing
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Import(WebMvcTestHelper.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private WebMvcTestHelper WebMvcTestHelper;

    @Test
    void testLogin() throws JsonProcessingException, Exception {
        String password = "testpass";

        Member member = new Member("test@test.com", "test", passwordEncoder.encode(password),
                MemberRole.GUEST);

        memberRepository.save(member);

        LoginForm loginForm = new LoginForm(member.getEmail(), password);

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(WebMvcTestHelper.toJson(loginForm)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("로그아웃 성공 - SecurityContext의 AccessToken을 사용하여 로그아웃 처리")
    void testLogout() throws Exception {
        // 1. 준비: 회원 가입
        String email = "flow@test.com";
        String password = "password123!";
        memberRepository.save(new Member(email, "flowUser", passwordEncoder.encode(password), MemberRole.GUEST));

        // 2. 로그인 실행 및 응답 추출
        LoginForm loginForm = new LoginForm(email, password);
        MvcResult loginResult = mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(WebMvcTestHelper.toJson(loginForm)))
                .andExpect(status().isOk())
                .andReturn();

        // 3. 응답에서 AccessToken과 RefreshToken 추출
        // JSON Body에서 AccessToken 추출 (JsonPath 활용)
        String responseBody = loginResult.getResponse().getContentAsString();
        String accessToken = JsonPath.read(responseBody, "$.data.accessToken");

        // Cookie에서 RefreshToken 추출
        Cookie refreshTokenCookie = loginResult.getResponse().getCookie("refreshToken");

        // 4. 추출한 AccessToken으로 로그아웃 테스트
        // MemberPrincipal을 직접 만들어서 주입 (SecurityContext 시뮬레이션)
        MemberPrincipal principal = MemberPrincipal.creatMemberPrincipalForSecurityContext(
                email, "flowUser", MemberRole.GUEST, accessToken);

        mockMvc.perform(post("/api/v1/auth/logout")
                .with(user(principal)) // 위에서 받은 토큰을 심은 객체 사용
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(cookie().maxAge("refreshToken", 0));

        // 5. 추출한 RefreshToken 쿠키로 재발급 테스트
        mockMvc.perform(post("/api/v1/auth/reissue")
                .cookie(refreshTokenCookie)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accessToken").exists());
    }

}
