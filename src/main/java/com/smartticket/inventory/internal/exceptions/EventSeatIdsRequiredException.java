package com.smartticket.inventory.internal.exceptions;

import com.smartticket.common.exception.BadRequestException;

public class EventSeatIdsRequiredException extends BadRequestException {

    public EventSeatIdsRequiredException() {
        super("Event seat ids cannot be empty");
    }
}
