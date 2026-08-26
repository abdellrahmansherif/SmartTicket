package com.smartticket.inventory.internal.exceptions;

import com.smartticket.common.exception.NotFoundException;

public class EventSeatsNotFoundException extends NotFoundException {

    public EventSeatsNotFoundException() {
        super("One or more event seats were not found");
    }
}
