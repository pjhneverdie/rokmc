package com.pdium.security.dto.exception;

import org.springframework.http.HttpStatus;

import com.pdium.common.exception.AppException;

public class UnAuthorizedException extends AppException {

    public UnAuthorizedException() {
        super("login please");
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.UNAUTHORIZED;
    }

}
