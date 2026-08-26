package com.smartticket.inventory.internal.exceptions;

import com.smartticket.common.exception.ConflictException;

import java.util.UUID;

public class EventSeatNotAvailableException extends ConflictException {

    public EventSeatNotAvailableException(UUID eventSeatId) {
        super("Event seat is not available: " + eventSeatId);
    }
}
