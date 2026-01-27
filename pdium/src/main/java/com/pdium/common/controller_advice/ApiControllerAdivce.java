package com.pdium.common.controller_advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.pdium.common.dto.ApiResponse;
import com.pdium.common.exception.BusinessException;

@RestControllerAdvice
public class ApiControllerAdivce {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse.Failure> handleBusinessException(BusinessException e) {
        return new ApiResponse.Failure(e.getMessage(), e.getHttpStatus()).toResponseEntity();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse.Failure> handleException(Exception e) {
        return new ApiResponse.Failure(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR).toResponseEntity();
    }

}
