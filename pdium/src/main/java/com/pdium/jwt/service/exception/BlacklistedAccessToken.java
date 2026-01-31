package com.pdium.jwt.service.exception;

import org.springframework.http.HttpStatus;

import com.pdium.common.exception.AppException;

public class BlacklistedAccessToken extends AppException {

    public BlacklistedAccessToken() {
        super("invalid access token");
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.UNAUTHORIZED;
    }

}
