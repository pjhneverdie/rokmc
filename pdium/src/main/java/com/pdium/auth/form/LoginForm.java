package com.pdium.auth.form;

import jakarta.validation.constraints.NotBlank;

public record LoginForm(
        @NotBlank String email, @NotBlank String password) {
}
