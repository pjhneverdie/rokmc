package com.pdium.auth.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.stereotype.Service;

import com.pdium.auth.dto.AuthenticateRequest;
import com.pdium.auth.dto.TokenResponse;
import com.pdium.auth.service.exception.WrongIdOrPasswordException;

import com.pdium.jwt.service.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    public Authentication authenticate(AuthenticateRequest authenticateRequest) {
        Authentication authentication;

        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticateRequest.email(), authenticateRequest.password()));
        } catch (Exception e) {
            throw new WrongIdOrPasswordException();
        }

        return authentication;
    }

    public TokenResponse issueToken(Authentication authentication) {
        return new TokenResponse(jwtService.createAccessToken(authentication),
                jwtService.createRefreshToken(authentication));
    }

}
