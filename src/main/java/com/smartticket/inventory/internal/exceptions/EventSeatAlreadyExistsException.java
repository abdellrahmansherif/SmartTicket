package com.smartticket.inventory.internal.exceptions;

import com.smartticket.common.exception.ConflictException;

public class EventSeatAlreadyExistsException extends ConflictException {

    public EventSeatAlreadyExistsException() {
        super("This seat already exists for this event");
    }
}
