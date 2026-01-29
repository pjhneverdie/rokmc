package com.pdium.jwt.service;

import java.security.Key;
import java.time.Duration;
import java.util.Date;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.pdium.jwt.config.JwtProperties;
import com.pdium.member.dto.MemberPrincipal;
import com.pdium.member.enum_type.MemberRole;
import com.pdium.security.util.SecurityUtils;

import io.jsonwebtoken.Claims;
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

    private static final String NICKNAME_KEY = "nickname";
    private static final String ROLES_KEY = "roles";
    private static final String ACCESS_TOKEN_TYPE_KEY = "access";
    private static final String REFRESH_TOKEN_TYPE_KEY = "refresh";
    private static final String TYPE_DISCRIMINATOR_KEY = "token_type";

    private static final String REFRESH_TOKEN_PREFIX = "RT:";

    private final StringRedisTemplate stringRedisTemplate;

    public JwtService(JwtProperties jwtProperties, StringRedisTemplate stringRedisTemplate) {
        this.jwtProperties = jwtProperties;
        this.stringRedisTemplate = stringRedisTemplate;
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.secret()));
    }

    public String createAccessToken(Authentication authentication) {
        return createToken(authentication, ACCESS_TOKEN_TYPE_KEY);
    }

    public String createRefreshToken(Authentication authentication) {
        return createToken(authentication, REFRESH_TOKEN_TYPE_KEY);
    }

    private String createToken(Authentication authentication, String tokenType) {
        long now = System.currentTimeMillis();

        long validity = tokenType.equals(ACCESS_TOKEN_TYPE_KEY)
                ? jwtProperties.accessTokenValidity()
                : jwtProperties.refreshTokenValidity();

        JwtBuilder builder = Jwts.builder()
                .setSubject(authentication.getName())
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + validity))
                .claim(TYPE_DISCRIMINATOR_KEY, tokenType);

        if (tokenType.equals(ACCESS_TOKEN_TYPE_KEY)) {
            builder.claim(NICKNAME_KEY, ((MemberPrincipal) authentication.getPrincipal()).getNickname());
            builder.claim(ROLES_KEY, SecurityUtils.getStringAuthorities(authentication.getAuthorities()));
        }

        String token = builder.signWith(key, sigAlgorithm).compact();

        if (tokenType.equals(REFRESH_TOKEN_TYPE_KEY)) {
            stringRedisTemplate.opsForValue().set(
                    REFRESH_TOKEN_PREFIX + authentication.getName(),
                    token,
                    Duration.ofMillis(validity));
        }

        return token;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Authentication toAuthentication(String accessToken) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(accessToken)
                .getBody();

        Assert.isTrue(claims.get(TYPE_DISCRIMINATOR_KEY).equals(ACCESS_TOKEN_TYPE_KEY),
                "엑세스 토큰에만 써라 진혁아~");

        MemberPrincipal memberPrincipal = MemberPrincipal.fromClaims((String) claims.getSubject(),
                (String) claims.get(NICKNAME_KEY), MemberRole
                        .valueOf(SecurityUtils.convertToAuthorities((String) claims.get(ROLES_KEY)).getFirst()
                                .getAuthority()));

        return new UsernamePasswordAuthenticationToken(memberPrincipal, null, memberPrincipal.getAuthorities());
    }

}
