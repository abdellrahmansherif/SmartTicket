package com.smartticket.event.internal.exception;

import com.smartticket.common.exception.NotFoundException;

import java.util.UUID;

public class EventNotFoundException extends NotFoundException {
    public EventNotFoundException(UUID message) {
        super("Event not found :" + message);
    }
}
