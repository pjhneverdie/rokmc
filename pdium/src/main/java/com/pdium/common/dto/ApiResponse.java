package com.pdium.common.dto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;

import com.pdium.common.exception.AppException;

public interface ApiResponse<T> {

    T value();

    String message();

    record Success<T>(T value, String message) implements ApiResponse<T> {
        public ResponseEntity<Success<T>> toResponseEntity() {
            return ResponseEntity.ok(this);
        }
    }

    // 일단 실패 시 뭐 응답 딱히 안 필요,, 필요 시 추가
    record Failure(Void value, String exceptionName, String message, HttpStatus status) implements ApiResponse<Void> {
        public Failure {
            Assert.isNull(value, "value should be null");
        }

        public ResponseEntity<Failure> toResponseEntity() {
            return ResponseEntity.status(this.status).body(this);
        }
    }

    static <T> Success<Void> createEmptySuccessResponse() {
        return new Success<>(null, "ok");
    }

    static <T> Success<T> createDefaultSuccessResponse(T value) {
        return new Success<>(value, "ok");
    }

    static Failure createDefaultFailureResponse(AppException e) {
        return new Failure(null, e.getClass().getSimpleName(), e.getMessage(), e.getHttpStatus());
    }

    static Failure createDefaultFailureResponse(String exceptionName, String message, HttpStatus status) {
        return new Failure(null, exceptionName, message, status);
    }

}