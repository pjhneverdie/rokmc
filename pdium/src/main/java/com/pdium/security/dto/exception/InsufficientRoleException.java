package com.pdium.security.dto.exception;

import org.springframework.http.HttpStatus;

import com.pdium.common.exception.AppException;

public class InsufficientRoleException extends AppException {

    public InsufficientRoleException() {
        super("check your role");
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.UNAUTHORIZED;
    }
    
}