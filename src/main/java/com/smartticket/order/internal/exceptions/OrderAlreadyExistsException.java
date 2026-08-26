package com.smartticket.order.internal.exceptions;

import com.smartticket.common.exception.ConflictException;

import java.util.UUID;

public class OrderAlreadyExistsException extends ConflictException {

    public OrderAlreadyExistsException(UUID reservationId) {
        super("An order already exists for reservation: " + reservationId);
    }
}
