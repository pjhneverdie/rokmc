package com.pdium.auth.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.stereotype.Service;

import com.pdium.auth.dto.AuthenticateDto;
import com.pdium.auth.dto.LogoutDto;
import com.pdium.auth.dto.TokenResponse;
import com.pdium.auth.service.exception.WrongIdOrPasswordException;
import com.pdium.common.exception.AppException;
import com.pdium.jwt.dto.CreateTokenDto;
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

    public void logout(LogoutDto logoutDto) {
        jwtService.nullifyJwt(logoutDto.email(), logoutDto.accessToken());
    }

    public TokenResponse issueToken(MemberPrincipal memberPrincipal) {
        CreateTokenDto.CreateAccessTokenDto createAccessTokenDto = new CreateTokenDto.CreateAccessTokenDto(
                memberPrincipal.getUsername(),
                memberPrincipal.getNickname(), SecurityUtils.getStringAuthorities(memberPrincipal.getAuthorities()));

        CreateTokenDto.CreateRefreshTokenDto createRefreshTokenDto = new CreateTokenDto.CreateRefreshTokenDto(
                memberPrincipal.getUsername());

        return new TokenResponse(
                jwtService.createAccessToken(createAccessTokenDto),
                jwtService.createRefreshToken(createRefreshTokenDto));
    }

    public TokenResponse reissueToken(String refreshToken) {
        try {
            jwtService.validateToken(refreshToken);
        } catch (AppException e) {
            throw e;
        }

        MemberPrincipal memberPrincipal = (MemberPrincipal) memberDetailsService
                .loadUserByUsername(jwtService.getEmailFromToken(refreshToken));

        return issueToken(memberPrincipal);
    }

}
