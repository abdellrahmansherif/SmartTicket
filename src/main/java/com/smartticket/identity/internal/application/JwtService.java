package com.smartticket.identity.internal.application;

import com.smartticket.identity.internal.domain.User;

public interface JwtService {

    String generateAccessToken(User user);

    long getAccessTokenExpirationSeconds();

    String generateRefreshToken();

    long getRefreshTokenExpirationSeconds();
}
