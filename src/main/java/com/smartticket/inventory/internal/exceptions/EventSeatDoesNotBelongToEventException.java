package com.smartticket.inventory.internal.exceptions;

import com.smartticket.common.exception.BadRequestException;

import java.util.UUID;

public class EventSeatDoesNotBelongToEventException extends BadRequestException {

    public EventSeatDoesNotBelongToEventException(
            UUID eventSeatId,
            UUID eventId
    ) {
        super(
                "Event seat " + eventSeatId
                        + " does not belong to event "
                        + eventId
        );
    }
}
