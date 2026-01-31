package com.pdium.jwt.service;

import java.security.Key;
import java.util.Date;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.pdium.jwt.config.JwtProperties;
import com.pdium.jwt.dto.CreateTokenDto;
import com.pdium.jwt.repository.TokenRepositoy;
import com.pdium.jwt.service.exception.ExpiredTokenException;
import com.pdium.jwt.service.exception.InvalidTokenException;
import com.pdium.jwt.service.exception.BlacklistedAccessToken;
import com.pdium.jwt.service.exception.RefreshTokenDoesNotExistException;
import com.pdium.member.dto.MemberPrincipal;
import com.pdium.member.enum_type.MemberRole;
import com.pdium.security.util.SecurityUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtService {

    private final Key key;
    private final JwtProperties jwtProperties;
    private final TokenRepositoy tokenRepository;
    private final SignatureAlgorithm sigAlgorithm;

    private static final String ROLES_KEY = "roles";
    private static final String NICKNAME_KEY = "nickname";
    private static final String ACCESS_TOKEN_TYPE_KEY = "access";
    private static final String REFRESH_TOKEN_TYPE_KEY = "refresh";
    private static final String TYPE_DISCRIMINATOR_KEY = "token_type";

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 엑세스 토큰 생성 메서드
    public String createAccessToken(CreateTokenDto.CreateAccessTokenDto accessTokenDto) {
        return createToken(accessTokenDto, ACCESS_TOKEN_TYPE_KEY);
    }

    // 리프레시 토큰 생성 메서드
    public String createRefreshToken(CreateTokenDto.CreateRefreshTokenDto refreshTokenDto) {
        String refreshToken = createToken(refreshTokenDto, REFRESH_TOKEN_TYPE_KEY);

        tokenRepository.saveRefreshToken(refreshTokenDto.email(), refreshToken, getRemainingExpiration(refreshToken));

        return refreshToken;
    }

    // 토큰 생성 공통 메서드
    private String createToken(CreateTokenDto dto, String tokenType) {
        long now = System.currentTimeMillis();
        long validity = tokenType.equals(ACCESS_TOKEN_TYPE_KEY)
                ? jwtProperties.accessTokenValidity()
                : jwtProperties.refreshTokenValidity();

        JwtBuilder builder = Jwts.builder()
                .setSubject(dto.email())
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + validity))
                .claim(TYPE_DISCRIMINATOR_KEY, tokenType);

        if (tokenType.equals(ACCESS_TOKEN_TYPE_KEY)) {
            builder.claim(NICKNAME_KEY, dto.nickname());
            builder.claim(ROLES_KEY, dto.stringAuthorities());
        }

        return builder.signWith(key, sigAlgorithm).compact();
    }

    // 모든 토큰 무효화, 엑세스 토큰은 블랙리스트에 등록 / 리프레쉬 토큰은 삭제
    public void nullifyJwt(String email, String accessToken) {
        tokenRepository.deleteRefreshToken(email);
        tokenRepository.blackAccessToken(accessToken, getRemainingExpiration(accessToken));
    }

    // 토큰에서 이메일 추출 메서드
    public String getEmailFromToken(String token) {
        return parseClaims(token).getSubject();
    }

    // 토큰 남은 유효 기간 확인 메서드
    private long getRemainingExpiration(String token) {
        return Math.max(parseClaims(token).getExpiration().getTime() - System.currentTimeMillis(), 0);
    }

    // 토큰 검증 메서드
    public void validateToken(String token) {
        Claims claims;

        // AppException으로 throw
        try {
            claims = parseClaims(token);
        } catch (ExpiredJwtException e) {
            throw new ExpiredTokenException();
        } catch (SignatureException e) {
            throw new InvalidTokenException();
        } catch (MalformedJwtException | UnsupportedJwtException e) {
            throw new InvalidTokenException();
        }

        if (claims.get(TYPE_DISCRIMINATOR_KEY).equals(ACCESS_TOKEN_TYPE_KEY)
                && tokenRepository.isBlacked(token)) {
            throw new BlacklistedAccessToken();
        }

        if (claims.get(TYPE_DISCRIMINATOR_KEY).equals(REFRESH_TOKEN_TYPE_KEY)
                && !tokenRepository.isRefreshTokenExist(getEmailFromToken(token))) {
            throw new RefreshTokenDoesNotExistException();
        }
    }

    // 엑세스 토큰 -> Authentication 변환 메서드 (JwtFilter에서 SecurityContext 설정할 때 사용)
    public Authentication toAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);

        MemberPrincipal memberPrincipal = MemberPrincipal.creatMemberPrincipalForSecurityContext(
                (String) claims.getSubject(), // email
                (String) claims.get(NICKNAME_KEY),
                MemberRole
                        .valueOf(SecurityUtils.convertToAuthorities((String) claims.get(ROLES_KEY)).getFirst()
                                .getAuthority()),
                accessToken);

        return new UsernamePasswordAuthenticationToken(memberPrincipal, null, memberPrincipal.getAuthorities());
    }

}
