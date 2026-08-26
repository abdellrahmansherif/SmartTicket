package com.smartticket.inventory.internal.exceptions;

import com.smartticket.common.exception.ConflictException;

import java.util.UUID;

public class EventSeatAlreadyReservedException extends ConflictException {

    public EventSeatAlreadyReservedException(UUID eventSeatId) {
        super("Event seat is already reserved: " + eventSeatId);
    }
}
