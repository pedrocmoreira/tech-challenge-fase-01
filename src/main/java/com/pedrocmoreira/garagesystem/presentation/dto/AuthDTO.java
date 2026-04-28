package com.pedrocmoreira.garagesystem.presentation.dto;

import jakarta.validation.constraints.NotBlank;

public class AuthDTO {
    public record LoginRequest(
            @NotBlank String username,
            @NotBlank String password
    ) {}

    public record TokenResponse(
            String token,
            String type,
            long expiracaoMs
    ) {}
}

