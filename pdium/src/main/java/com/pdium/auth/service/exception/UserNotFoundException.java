package com.pdium.auth.service.exception;

import org.springframework.http.HttpStatus;

import com.pdium.common.exception.BusinessException;

public class UserNotFoundException extends BusinessException {

    public UserNotFoundException() {
        super("please check id or passowrd");
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }

}
