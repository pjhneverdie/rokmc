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
import com.pdium.jwt.dto.DeleteRefreshTokenRequest;
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

    private final Key key;
    private final JwtProperties jwtProperties;
    private final SignatureAlgorithm sigAlgorithm = SignatureAlgorithm.HS512;

    private static final String ROLES_KEY = "roles";
    private static final String NICKNAME_KEY = "nickname";
    private static final String ACCESS_TOKEN_TYPE_KEY = "access";
    private static final String REFRESH_TOKEN_TYPE_KEY = "refresh";
    private static final String TYPE_DISCRIMINATOR_KEY = "token_type";

    private static final String REFRESH_TOKEN_PREFIX = "rt";
    private static final String BLACKLIST_PREFIX = "black_";
    private static final String BLACKLIST_REASON = "logout";

    private final StringRedisTemplate stringRedisTemplate;

    public JwtService(JwtProperties jwtProperties, StringRedisTemplate stringRedisTemplate) {
        this.jwtProperties = jwtProperties;
        this.stringRedisTemplate = stringRedisTemplate;
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.secret()));
    }

    // 토큰 생성 공통 메서드
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

        return builder.signWith(key, sigAlgorithm).compact();
    }

    // 엑세스 토큰 생성 메서드
    public String createAccessToken(Authentication authentication) {
        return createToken(authentication, ACCESS_TOKEN_TYPE_KEY);
    }

    // 리프레쉬 토큰 생성 및 저장 메서드
    public String createRefreshToken(Authentication authentication) {
        String refreshToken = createToken(authentication, REFRESH_TOKEN_TYPE_KEY);

        stringRedisTemplate.opsForValue().set(REFRESH_TOKEN_PREFIX + authentication.getName(), refreshToken,
                Duration.ofMillis(jwtProperties.refreshTokenValidity()));

        return refreshToken;
    }

    // 리프레쉬 토큰 삭제 및 엑세스 토큰 블랙리스트 등록 메서드
    public void deleteRefreshToken(DeleteRefreshTokenRequest deleteRefreshTokenRequest) {
        stringRedisTemplate.delete(REFRESH_TOKEN_PREFIX + deleteRefreshTokenRequest.email());

        long expiration = getRemainingExpiration(deleteRefreshTokenRequest.accessToken());

        stringRedisTemplate.opsForValue().set(
                BLACKLIST_PREFIX + deleteRefreshTokenRequest.accessToken(),
                BLACKLIST_REASON,
                Duration.ofMillis(expiration));
    }

    // 토큰 남은 유효 기간 확인 메서드
    private long getRemainingExpiration(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        long now = System.currentTimeMillis();
        long diff = claims.getExpiration().getTime() - now;

        return Math.max(diff, 0);
    }

    // 토큰 변조 및 유효 기간 검증 메서드
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // JwtFilter에서 SecurityContext 설정할 때 사용
    public Authentication toAuthentication(String accessToken) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(accessToken)
                .getBody();

        MemberPrincipal memberPrincipal = MemberPrincipal.creatMemberPrincipalForSecurityContext(
                (String) claims.getSubject(),
                (String) claims.get(NICKNAME_KEY),
                MemberRole
                        .valueOf(SecurityUtils.convertToAuthorities((String) claims.get(ROLES_KEY)).getFirst()
                                .getAuthority()),
                accessToken);

        return new UsernamePasswordAuthenticationToken(memberPrincipal, null, memberPrincipal.getAuthorities());
    }

}
