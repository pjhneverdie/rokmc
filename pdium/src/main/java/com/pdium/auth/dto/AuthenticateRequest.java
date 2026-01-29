package com.pdium.auth.dto;

public record AuthenticateRequest(String email, String password) {
}