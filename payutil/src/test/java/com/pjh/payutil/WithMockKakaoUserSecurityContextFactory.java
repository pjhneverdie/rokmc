package com.pjh.payutil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import com.pjh.payutil.security.oauth2.dto.KakaoUser;

public class WithMockKakaoUserSecurityContextFactory implements WithSecurityContextFactory<WithMockKakaoUser> {

    @Override
    public SecurityContext createSecurityContext(WithMockKakaoUser annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        Map<String, Object> profile = new HashMap<>();
        profile.put("nickname", annotation.nickname());
        profile.put("profile_image_url", annotation.profileImageUrl());

        Map<String, Object> kakaoAccount = new HashMap<>();
        kakaoAccount.put("profile", profile);

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("id", annotation.id());
        attributes.put("kakao_account", kakaoAccount);

        List<GrantedAuthority> authorities = List.of(() -> "ROLE_FREE");

        DefaultOAuth2User defaultUser = new DefaultOAuth2User(
                authorities,
                attributes,
                "id");

        KakaoUser kakaoUser = new KakaoUser(defaultUser);

        Authentication authentication = new OAuth2AuthenticationToken(
                kakaoUser,
                kakaoUser.getAuthorities(),
                "kakao");

        context.setAuthentication(authentication);

        return context;
    }

}
