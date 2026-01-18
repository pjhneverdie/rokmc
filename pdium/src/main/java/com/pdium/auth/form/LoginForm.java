package com.pdium.auth.form;

import com.pdium.auth.dto.CreateTokenDto;

import jakarta.validation.constraints.NotBlank;

public record LoginForm(
        @NotBlank String id, @NotBlank String password) {

    public CreateTokenDto.CreateTokenRequest toCreateTokenRequest() {
        return new CreateTokenDto.CreateTokenRequest(this.id, this.password);
    }

}
