package com.pdium.common.exception;

import org.springframework.http.HttpStatus;

public abstract class AppException extends RuntimeException {
    public abstract HttpStatus getHttpStatus();

    protected AppException(String message) {
        super(message);
    }
}
