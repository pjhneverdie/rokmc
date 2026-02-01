package com.pdium.jwt.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.pdium.jwt.config.JwtContants;
import com.pdium.jwt.config.JwtProperties;
import com.pdium.jwt.dto.CreateTokenDto;
import com.pdium.jwt.repository.TokenRepositoy;
import com.pdium.jwt.service.exception.BlacklistedAccessTokenException;
import com.pdium.jwt.service.exception.ExpiredTokenException;
import com.pdium.jwt.service.exception.InvalidTokenException;
import com.pdium.jwt.service.exception.RefreshTokenDoesNotExistException;
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

        String sub = cTokenDto.email();
        long accessTokenValidity = jwtProperties.accessTokenValidity();

        return jwtProvider.createToken(sub, claims, accessTokenValidity);
    }

    // 리프레시 토큰 생성 및 저장 메서드
    public String createAndSaveRefreshToken(CreateTokenDto.CreateRefreshTokenDto cTokenDto) {
        Map<String, Object> claims = new HashMap<>();

        claims.put(JwtContants.NICKNAME_KEY, cTokenDto.nickname());
        claims.put(JwtContants.ROLES_KEY, cTokenDto.stringAuthorities());
        claims.put(JwtContants.TYPE_DISCRIMINATOR_KEY, JwtContants.TokenType.REFRESH.getValue());

        String sub = cTokenDto.email();
        long refreshTokenValidity = jwtProperties.refreshTokenValidity();

        String refreshToken = jwtProvider.createToken(sub, claims, refreshTokenValidity);

        tokenRepository.saveRefreshToken(sub, refreshToken, jwtProvider.getRemainingValidity(refreshToken));

        return refreshToken;
    }

    // 모든 토큰 무효화 메서드: 엑세스 토큰은 블랙리스트에 등록, 리프레쉬 토큰은 삭제
    public void nullifyJwt(String email, String accessToken) {
        tokenRepository.deleteRefreshToken(JwtContants.REFRESH_TOKEN_PREFIX + email);

        tokenRepository.blackAccessToken(JwtContants.BLACKLIST_PREFIX + accessToken,
                JwtContants.BlackReason.LOGOUT.getValue(),
                jwtProvider.getRemainingValidity(accessToken));
    }

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

        if (claims.get(JwtContants.TYPE_DISCRIMINATOR_KEY).equals(JwtContants.TokenType.ACCESS.getValue())
                && tokenRepository.isBlacked(token)) {
            throw new BlacklistedAccessTokenException();
        }

        if (claims.get(JwtContants.TYPE_DISCRIMINATOR_KEY).equals(JwtContants.TokenType.REFRESH.getValue())
                && !tokenRepository.isRefreshTokenExist(jwtProvider.getEmailFromToken(token))) {
            throw new RefreshTokenDoesNotExistException();
        }
    }

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
