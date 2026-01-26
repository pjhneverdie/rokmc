package com.pdium.security.filter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pdium.TestContainerInitializer;
import com.pdium.common.dto.ApiResponse;
import com.pdium.jwt.service.JwtService;
import com.pdium.member.dto.MemberPrincipal;
import com.pdium.member.enum_type.MemberRole;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@TestContainerInitializer
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class JwtFilterTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    protected String toJson(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }

    @RestController
    static class TestController {
        @GetMapping("/api/protected")
        public String protectedApi() {
            return "ok";
        }
    }

    @Test
    @DisplayName("정상 토큰으로 필터 거쳐서 내부 컨트롤러 호출 성공")
    void innerControllerTest() throws Exception {
        MemberPrincipal memberPrincipal = MemberPrincipal.fromClaims("email", "nickname", MemberRole.ROLE_PJH);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(memberPrincipal,
                null, memberPrincipal.getAuthorities());

        String token = jwtService.createAccessToken(authentication);

        ApiResponse.Success<String> response = new ApiResponse.Success<>("ok");

        mockMvc.perform(get("/api/test-protected")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().json(toJson(response)));
    }


    
}
