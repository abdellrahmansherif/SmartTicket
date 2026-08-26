package com.smartticket.inventory.internal.exceptions;

import com.smartticket.common.exception.BadRequestException;

public class InvalidSeatHoldExpirationException extends BadRequestException {

    public InvalidSeatHoldExpirationException() {
        super("Hold expiration must be in the future");
    }
}
