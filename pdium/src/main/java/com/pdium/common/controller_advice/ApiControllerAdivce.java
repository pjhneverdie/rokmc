package com.pdium.common.controller_advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.pdium.common.dto.ApiResponse;
import com.pdium.common.exception.AppException;

@RestControllerAdvice
public class ApiControllerAdivce {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse.Failure> handleBusinessException(AppException e) {
        return ApiResponse.createDefaultFailureResponse(e).toResponseEntity();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse.Failure> handleException(Exception e) {
        return ApiResponse
                .createDefaultFailureResponse(e.getClass().getSimpleName(), e.getMessage(),
                        HttpStatus.INTERNAL_SERVER_ERROR)
                .toResponseEntity();
    }

}
