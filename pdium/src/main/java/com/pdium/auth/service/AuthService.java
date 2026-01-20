package com.pdium.auth.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.pdium.auth.dto.CreateTokenDto;
import com.pdium.auth.service.exception.WrongIdOrPasswordException;
import com.pdium.jwt.service.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public CreateTokenDto.CreateTokenResponse createToken(CreateTokenDto.CreateTokenRequest creatCreateTokenRequest) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                creatCreateTokenRequest.email(), creatCreateTokenRequest.password());

        Authentication authentication;

        try {
            authentication = authenticationManager.authenticate(authenticationToken);
        } catch (Exception e) {
            throw new WrongIdOrPasswordException();
        }

        String accessToken = jwtService.createAccessToken(authentication);

        String refreshToken = jwtService.createRefreshToken(authentication);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return new CreateTokenDto.CreateTokenResponse(accessToken, refreshToken);
    }

}
