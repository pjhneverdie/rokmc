package com.pdium.auth.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pdium.TestMysqlContainerInitializer;
import com.pdium.TestRedisContainerInitializer;
import com.pdium.WebMvcTestHelper;
import com.pdium.auth.form.LoginForm;
import com.pdium.member.domain.Member;
import com.pdium.member.enum_type.MemberRole;
import com.pdium.member.repository.MemberRepository;

// AuthService 단위 테스트 의미가 없음. AuthControllerTest로 통합 테스트
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

        Member member = new Member("test@test.com", "test", passwordEncoder.encode(password), MemberRole.ROLE_GUEST);

        memberRepository.save(member);

        LoginForm loginForm = new LoginForm(member.getEmail(), password);

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(WebMvcTestHelper.toJson(loginForm)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.value.accessToken").value("access-token"))
                .andExpect(jsonPath("$.value.refreshToken").value("refresh-token")).andDo(print());
    }

}
