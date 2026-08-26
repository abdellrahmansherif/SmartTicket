package com.smartticket.inventory.internal.exceptions;

import com.smartticket.common.exception.ConflictException;

import java.util.UUID;

public class EventSeatHoldExpiredException extends ConflictException {

    public EventSeatHoldExpiredException(UUID eventSeatId) {
        super("Hold has expired for event seat: " + eventSeatId);
    }
}
