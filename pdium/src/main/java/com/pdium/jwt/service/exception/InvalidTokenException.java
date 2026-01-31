package com.pdium.jwt.service.exception;

import org.springframework.http.HttpStatus;

import com.pdium.common.exception.AppException;

public class InvalidTokenException extends AppException {

    public InvalidTokenException() {
        super("invalid token");
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.UNAUTHORIZED;
    }

}