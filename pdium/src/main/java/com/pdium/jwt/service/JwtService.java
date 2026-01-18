package com.pdium.jwt.service;

import java.security.Key;
import java.util.Date;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.pdium.jwt.config.JwtProperties;
import com.pdium.security.util.SecurityUtil;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtService {

    private final JwtProperties jwtProperties;

    private final Key key;
    private final SignatureAlgorithm sigAlgorithm = SignatureAlgorithm.HS512;

    private static final String ROLES_KEY = "roles";
    private static final String ACCESS_TOKEN_TYPE_KEY = "access";
    private static final String REFRESH_TOKEN_TYPE_KEY = "refresh";
    private static final String TYPE_DISCRIMINATOR_KEY = "token_type";

    public JwtService(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.getSecret()));
    }

    public String createAccessToken(Authentication authentication) {
        return createToken(authentication, ACCESS_TOKEN_TYPE_KEY);
    }

    public String createRefreshToken(Authentication authentication) {
        return createToken(authentication, REFRESH_TOKEN_TYPE_KEY);
    }

    private String createToken(Authentication authentication, String tokenType) {
        long now = System.currentTimeMillis();

        long validity = (tokenType == ACCESS_TOKEN_TYPE_KEY)
                ? jwtProperties.getAccessTokenValidity()
                : jwtProperties.getRefreshTokenValidity();

        JwtBuilder builder = Jwts.builder()
                .setSubject(authentication.getName())
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + validity))
                .claim(TYPE_DISCRIMINATOR_KEY, tokenType)
                .signWith(key, sigAlgorithm);

        if (tokenType == ACCESS_TOKEN_TYPE_KEY) {
            builder.claim(ROLES_KEY, SecurityUtil.getAuthorities(authentication));
        }

        return builder.compact();
    }

}
