package com.smartticket.inventory.internal.exceptions;

import com.smartticket.common.exception.NotFoundException;

import java.util.UUID;

public class EventSeatNotFoundException extends NotFoundException {

    public EventSeatNotFoundException(UUID id) {
        super("Event seat not found: " + id);
    }
}
