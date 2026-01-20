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

    private final MemberRole role;

    public MemberPrincipal(Member member) {
        this.email = member.getEmail();
        this.nickname = member.getNickname();
        this.role = member.getRole();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(
                new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return null;
    }

    // Authentication.getName() 했을 때 나오는 값.
    @Override
    public String getUsername() {
        return this.email;
    }

    public String getNickname() {
        return this.nickname;
    }

}
