package com.smartticket.identity.internal.web.Dto;

public record RefreshTokenResponse (
        String accessToken,
        String tokenType,
        long expiresIn){
}
