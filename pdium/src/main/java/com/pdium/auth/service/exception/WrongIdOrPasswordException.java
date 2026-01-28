package com.pdium.auth.service.exception;

import org.springframework.http.HttpStatus;

import com.pdium.common.exception.AppException;

public class WrongIdOrPasswordException extends AppException {

    public WrongIdOrPasswordException() {
        super("please check id or passowrd");
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }

}
