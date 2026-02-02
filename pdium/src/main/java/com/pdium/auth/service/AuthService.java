package com.pdium.auth.service;

import java.util.Date;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.stereotype.Service;

import com.pdium.auth.dto.AuthenticateDto;
import com.pdium.auth.dto.IssuedTokens;
import com.pdium.auth.service.exception.WrongIdOrPasswordException;
import com.pdium.common.exception.AppException;
import com.pdium.jwt.dto.CreateTokenDto;
import com.pdium.jwt.dto.RefreshTokenNExpiration;
import com.pdium.jwt.service.JwtService;
import com.pdium.member.dto.MemberPrincipal;
import com.pdium.member.service.MemberDetailsService;
import com.pdium.security.util.SecurityUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtService jwtService;

    private final MemberDetailsService memberDetailsService;

    private final AuthenticationManager authenticationManager;

    /**
     * 로그인 메서드
     */
    public Authentication authenticate(AuthenticateDto authenticateRequest) {
        Authentication authentication;

        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticateRequest.email(), authenticateRequest.password()));
        } catch (Exception e) {
            throw new WrongIdOrPasswordException();
        }

        return authentication;
    }

    /**
     * 로그아웃 메서드
     */
    public void logout(String accessToken) {
        jwtService.nullifyJwt(accessToken);
    }

    /**
     * 엑세스, 리프레시 토큰 발급 메서드
     */
    public IssuedTokens issueTokens(MemberPrincipal memberPrincipal) {
        long now = System.currentTimeMillis();
        String email = memberPrincipal.getUsername();
        String nickanme = memberPrincipal.getNickname();
        String stringAuthorities = SecurityUtils.getStringAuthorities(memberPrincipal.getAuthorities());

        CreateTokenDto.CreateAccessTokenDto createAccessTokenDto = new CreateTokenDto.CreateAccessTokenDto(
                email,
                nickanme, stringAuthorities, new Date(now));

        CreateTokenDto.CreateRefreshTokenDto createRefreshTokenDto = new CreateTokenDto.CreateRefreshTokenDto(
                email, new Date(now));

        String accessToken = jwtService.createAccessToken(createAccessTokenDto);
        RefreshTokenNExpiration refreshTokenNExpiration = jwtService.createAndSaveRefreshToken(createRefreshTokenDto);

        return new IssuedTokens(
                accessToken,
                refreshTokenNExpiration.refresh(),
                refreshTokenNExpiration.expiration());
    }

    /**
     * 엑세스, 리프레시 토큰 재발급 메서드
     */
    public IssuedTokens reissueTokens(String refreshToken) {
        String sub;

        try {
            sub = jwtService.validateToken(refreshToken);
        } catch (AppException e) {
            throw e;
        }

        // 유저 정보가 바뀌었을 수도 있음.
        // 엑세스 토큰에 유저 정보를 담는 한, 재발급 시 항상 db를 조회해야 함.
        MemberPrincipal memberPrincipal = (MemberPrincipal) memberDetailsService.loadUserByUsername(sub);

        return issueTokens(memberPrincipal);
    }

}
