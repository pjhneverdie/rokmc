package com.pdium.jwt.service.exception;

import org.springframework.http.HttpStatus;

import com.pdium.common.exception.AppException;

public class RefreshTokenDoesNotExistException extends AppException {

    public RefreshTokenDoesNotExistException() {
        super("invalid refresh token");
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.UNAUTHORIZED;
    }

}
