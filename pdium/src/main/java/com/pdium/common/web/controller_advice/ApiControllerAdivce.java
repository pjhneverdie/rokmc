package com.pdium.common.web.controller_advice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.pdium.common.exception.BusinessException;
import com.pdium.common.web.dto.ApiResponse;

@RestControllerAdvice
public class ApiControllerAdivce {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse.Failure> handleCustomException(BusinessException e) {
        return new ApiResponse.Failure(e.getMessage(), e.getHttpStatus()).toResponseEntity();
    }

}
