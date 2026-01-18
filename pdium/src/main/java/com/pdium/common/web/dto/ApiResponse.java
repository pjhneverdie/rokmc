package com.pdium.common.web.dto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public interface ApiResponse<T> {

    T value();

    String message();

    record Success<T>(T value, String message) implements ApiResponse<T> {
        public Success(T value) {
            this(value, "성공");
        }

        public ResponseEntity<Success<T>> toResponseEntity() {
            return ResponseEntity.ok(this);
        }
    }

    record Failure(Void value, String message, HttpStatus status) implements ApiResponse<Void> {
        public Failure(String message, HttpStatus status) {
            this(null, message, status);
        }

        public ResponseEntity<Failure> toResponseEntity() {
            return ResponseEntity.status(this.status).body(this);
        }
    }

}