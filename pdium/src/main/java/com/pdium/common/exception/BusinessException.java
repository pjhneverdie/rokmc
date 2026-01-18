package com.pdium.common.exception;

import org.springframework.http.HttpStatus;

public abstract class BusinessException extends RuntimeException {
    public abstract HttpStatus getHttpStatus();

    protected BusinessException(String message) {
        super(message);
    }
}
