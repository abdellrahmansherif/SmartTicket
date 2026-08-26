package com.smartticket.identity.internal.exceptions;

import com.smartticket.common.exception.UnauthorizedException;

public class RefreshTokenExpiredException extends UnauthorizedException {
   public RefreshTokenExpiredException()
   {
       super("Refresh Token Expired Exception");
   }
}
