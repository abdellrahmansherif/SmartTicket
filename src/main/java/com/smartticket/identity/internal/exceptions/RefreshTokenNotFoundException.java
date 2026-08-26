package com.smartticket.identity.internal.exceptions;

import com.smartticket.common.exception.NotFoundException;

public class RefreshTokenNotFoundException extends NotFoundException {
    public RefreshTokenNotFoundException()
    {
        super("Refresh Token Not Found");
    }
}
