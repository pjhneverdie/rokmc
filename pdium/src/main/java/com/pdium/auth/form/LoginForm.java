package com.pdium.auth.form;

import com.pdium.auth.dto.CreateTokenDto;

import jakarta.validation.constraints.NotBlank;

public record LoginForm(
        @NotBlank String email, @NotBlank String password) {

    public CreateTokenDto.CreateTokenRequest toCreateTokenRequest() {
        return new CreateTokenDto.CreateTokenRequest(this.email, this.password);
    }

}
