package com.pdium.jwt.service.exception;

import org.springframework.http.HttpStatus;

import com.pdium.common.exception.AppException;

public class BlacklistedAccessTokenException extends AppException {

    public BlacklistedAccessTokenException() {
        super("invalid access token");
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.UNAUTHORIZED;
    }

}
