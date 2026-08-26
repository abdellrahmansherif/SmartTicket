package com.smartticket.identity.internal.web.Dto;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequest(
        @NotBlank(message = "RefreshToken is required")
        String refreshToken
) {
}
