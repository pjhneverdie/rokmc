package com.pdium.mother;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import com.pdium.member.dto.MemberPrincipal;
import com.pdium.member.enum_type.MemberRole;

public abstract class AuthenticationMother {

    private AuthenticationMother() {
    }

    public static MemberPrincipal createAdminMember() {
        return MemberPrincipal.creatMemberPrincipalForSecurityContext("admin@example.com", "AdminUser",
                MemberRole.ROLE_PJH, "");
    }

    public static MemberPrincipal createGuestMember() {
        return MemberPrincipal.creatMemberPrincipalForSecurityContext("test@example.com", "TestUser",
                MemberRole.ROLE_GUEST, "");
    }

    public static UsernamePasswordAuthenticationToken createAdminAuthentication() {
        MemberPrincipal memberPrincipal = createAdminMember();
        return new UsernamePasswordAuthenticationToken(
                memberPrincipal,
                null,
                memberPrincipal.getAuthorities());
    }

    public static UsernamePasswordAuthenticationToken createGuestAuthentication() {
        MemberPrincipal memberPrincipal = createGuestMember();
        return new UsernamePasswordAuthenticationToken(
                memberPrincipal,
                null,
                memberPrincipal.getAuthorities());
    }

}
