package com.pdium.mother;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import com.pdium.member.dto.MemberPrincipal;
import com.pdium.member.enum_type.MemberRole;

public abstract class AuthenticationMother {

        private AuthenticationMother() {
        }

        public static UsernamePasswordAuthenticationToken createGuestAuthenticationForSecurityContext() {
                MemberPrincipal memberPrincipal = MemberPrincipal.creatMemberPrincipalForSecurityContext(
                                "test@example.com", "testuser", MemberRole.GUEST, "accessToken");

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                memberPrincipal,
                                null,
                                memberPrincipal.getAuthorities());

                return authentication;
        }

        public static UsernamePasswordAuthenticationToken createAdminAuthenticationForSecurityContext() {
                MemberPrincipal memberPrincipal = MemberPrincipal.creatMemberPrincipalForSecurityContext(
                                "test@example.com", "testuser", MemberRole.PJH, "accessToken");

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                memberPrincipal,
                                null,
                                memberPrincipal.getAuthorities());

                return authentication;
        }

}
