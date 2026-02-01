package com.pdium.jwt.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.pdium.jwt.config.JwtContants;
import com.pdium.jwt.config.JwtProperties;
import com.pdium.jwt.dto.CreateTokenDto;
import com.pdium.jwt.dto.exception.ExpiredTokenException;
import com.pdium.jwt.dto.exception.InvalidTokenException;
import com.pdium.jwt.repository.TokenRepositoy;
import com.pdium.member.dto.MemberPrincipal;
import com.pdium.member.enum_type.MemberRole;
import com.pdium.security.util.SecurityUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtService {

    private final JwtProvider jwtProvider;
    private final JwtProperties jwtProperties;
    private final TokenRepositoy tokenRepository;

    // 엑세스 토큰 생성 메서드
    public String createAccessToken(CreateTokenDto.CreateAccessTokenDto cTokenDto) {
        Map<String, Object> claims = new HashMap<>();

        claims.put(JwtContants.NICKNAME_KEY, cTokenDto.nickname());
        claims.put(JwtContants.ROLES_KEY, cTokenDto.stringAuthorities());
        claims.put(JwtContants.TYPE_DISCRIMINATOR_KEY, JwtContants.TokenType.ACCESS.getValue());

        return jwtProvider.createToken(cTokenDto.email(), claims, jwtProperties.accessTokenValidity());
    }

    // 리프레시 토큰 생성 및 저장 메서드
    public String createAndSaveRefreshToken(CreateTokenDto.CreateRefreshTokenDto cTokenDto) {
        Map<String, Object> claims = new HashMap<>();

        String sub = cTokenDto.email();
        long refreshTokenValidity = jwtProperties.refreshTokenValidity();

        // 리프레시 토큰은 유저 정보 안 담음.
        claims.put(JwtContants.TYPE_DISCRIMINATOR_KEY, JwtContants.TokenType.REFRESH.getValue());

        return tokenRepository.saveRefreshToken(JwtContants.REFRESH_TOKEN_PREFIX + sub,
                jwtProvider.createToken(sub, claims, refreshTokenValidity),
                refreshTokenValidity);
    }

    // 모든 토큰 무효화 메서드: 엑세스 토큰은 블랙리스트에 등록, 리프레쉬 토큰은 삭제
    public void nullifyJwt(String accessToken) {
        tokenRepository.deleteRefreshToken(JwtContants.REFRESH_TOKEN_PREFIX + getEmailFromToken(accessToken));

        tokenRepository.blackAccessToken(JwtContants.BLACKLIST_PREFIX + accessToken,
                JwtContants.BlackReason.LOGOUT.getValue(),
                getRemainingValidity(accessToken));
    }

    // 토큰 검증 메서드
    public void validateToken(String token) {
        Claims claims;

        // AppException으로 throw
        try {
            claims = jwtProvider.parseClaims(token);
        } catch (ExpiredJwtException e) {
            throw new ExpiredTokenException();
        } catch (SignatureException e) {
            throw new InvalidTokenException();
        } catch (MalformedJwtException | UnsupportedJwtException e) {
            throw new InvalidTokenException();
        }

        // 엑세스 토큰 재활용 방지
        if (claims.get(JwtContants.TYPE_DISCRIMINATOR_KEY).equals(JwtContants.TokenType.ACCESS.getValue())
                && tokenRepository.isBlacked(token)) {
            throw new ExpiredTokenException();
        }

        // 리프레시 토큰 재활용 방지
        if (claims.get(JwtContants.TYPE_DISCRIMINATOR_KEY).equals(JwtContants.TokenType.REFRESH.getValue())
                && !tokenRepository.isRefreshTokenExist(claims.getSubject())) {
            throw new ExpiredTokenException();
        }
    }

    // 토큰에서 이메일 조회 메서드
    public String getEmailFromToken(String token) {
        return jwtProvider.getSubject(token);
    }

    // 토큰 남은 유효기간 조회 메서드
    private long getRemainingValidity(String token) {
        return Math.max(jwtProvider.getValidity(token) - System.currentTimeMillis(), 0);
    }

    // JwtFilter에서 엑세스 토큰으로 시큐리티 컨텍스트에 Authentication 넣을 때 사용!
    public Authentication toAuthentication(String accessToken) {
        Claims claims = jwtProvider.parseClaims(accessToken);

        MemberPrincipal memberPrincipal = MemberPrincipal.creatMemberPrincipalForSecurityContext(
                (String) claims.getSubject(), // email
                (String) claims.get(JwtContants.NICKNAME_KEY),
                MemberRole
                        .valueOf(SecurityUtils.convertToAuthorities((String) claims.get(JwtContants.ROLES_KEY))
                                .getFirst()
                                .getAuthority()),
                accessToken);

        return new UsernamePasswordAuthenticationToken(memberPrincipal, null, memberPrincipal.getAuthorities());
    }

}
