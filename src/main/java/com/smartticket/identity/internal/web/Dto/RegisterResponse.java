package com.smartticket.identity.internal.web.Dto;

public record RegisterResponse(
        String accessToken,
        String refreshToken,
        String tokenType,
        long expiresIn,
        UserResponse user
) {
}
