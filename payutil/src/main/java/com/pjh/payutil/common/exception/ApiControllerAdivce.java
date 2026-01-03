package com.pjh.payutil.common.exception;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.pjh.payutil.feed.controller.FeedController;

@RestControllerAdvice(basePackageClasses = FeedController.class)
public class ApiControllerAdivce {

    @ExceptionHandler(Exception.class)
    ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        
        return new ResponseEntity<>(
                new ApiResponse<>(
                        null,
                        "알 수 없는 예외 발생, 개발자에게 문의하세요. pjhneverdie@gmail.com"),
                HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse<Void>> handleValidationException(MethodArgumentNotValidException e) {

        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        FieldError fieldError = fieldErrors.get(fieldErrors.size() - 1);
        String fieldName = fieldError.getField();
        Object rejectedValue = fieldError.getRejectedValue();

        String message = fieldName + " 필드의 입력값[ " + rejectedValue + " ]이 유효하지 않습니다.";

        return new ResponseEntity<>(new ApiResponse<>(null, message), HttpStatus.BAD_REQUEST);

    }

}
