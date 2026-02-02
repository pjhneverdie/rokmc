package com.pdium.mother;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import com.pdium.member.dto.MemberPrincipal;
import com.pdium.member.enum_type.MemberRole;

public abstract class AuthenticationMother {

    private AuthenticationMother() {
    }

    public static UsernamePasswordAuthenticationToken createAuthenticationForSecurityContext() {
        MemberPrincipal memberPrincipal = MemberPrincipal.creatMemberPrincipalForSecurityContext(
                "test@example.com", "testuser", MemberRole.ROLE_GUEST, "accessToken");

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                memberPrincipal,
                null,
                memberPrincipal.getAuthorities());

        return authentication;
    }

}
