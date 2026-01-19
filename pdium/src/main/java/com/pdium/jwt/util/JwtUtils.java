package com.pdium.jwt.util;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import com.pdium.security.service.MemberPrincipal;
import com.pdium.security.util.SecurityUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class JwtUtils {

    public static Authentication getAuthentication(Claims claims, String rolesKey) {
        String stringAuthorities = (String) claims.get(rolesKey);

        MemberPrincipal memberPrincipal = new MemberPrincipal();

        return new UsernamePasswordAuthenticationToken(memberPrincipal, null,
                SecurityUtils.convertToAuthorities(stringAuthorities));
    }

}
