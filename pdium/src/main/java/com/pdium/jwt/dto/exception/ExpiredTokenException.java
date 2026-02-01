package com.pdium.jwt.dto.exception;

import org.springframework.http.HttpStatus;

import com.pdium.common.exception.AppException;

public class ExpiredTokenException extends AppException {

    public ExpiredTokenException() {
        super("you are using expired jwt");

    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.UNAUTHORIZED;
    }

}
