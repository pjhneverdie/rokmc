package com.pdium.member.dto;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.pdium.member.domain.Member;
import com.pdium.member.enum_type.MemberRole;

public class MemberPrincipal implements UserDetails {

    private final String email;

    private final String nickname;

    private final String password;

    private final MemberRole role;

    private final String accessToken;

    private MemberPrincipal(String email, String nickname, String password, MemberRole role, String accessToken) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.role = role;
        this.accessToken = accessToken;
    }

    public static MemberPrincipal creatMemberPrincipalForReIssue(Member member) {
        return new MemberPrincipal(member.getEmail(), member.getNickname(), null, member.getRole(),
                null);
    }

    public static MemberPrincipal creatMemberPrincipalForAuthenticate(Member member) {
        return new MemberPrincipal(member.getEmail(), member.getNickname(), member.getPassword(), member.getRole(),
                null);
    }

    public static MemberPrincipal creatMemberPrincipalForSecurityContext(String email, String nickname, MemberRole role,
            String accessToken) {
        return new MemberPrincipal(email, nickname, null, role, accessToken);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(
                new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    // Authentication.getName() 했을 때 나오는 값.
    @Override
    public String getUsername() {
        return this.email;
    }

    public String getNickname() {
        return this.nickname;
    }

    public String getAccessToken() {
        return this.accessToken;
    }

}
