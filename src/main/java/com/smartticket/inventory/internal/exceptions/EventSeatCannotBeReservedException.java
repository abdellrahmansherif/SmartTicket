package com.smartticket.inventory.internal.exceptions;

import com.smartticket.common.exception.ConflictException;
import com.smartticket.inventory.internal.domain.EventSeatStatus;

import java.util.UUID;

public class EventSeatCannotBeReservedException extends ConflictException {

    public EventSeatCannotBeReservedException(
            UUID eventSeatId,
            EventSeatStatus status
    ) {
        super(
                "Event seat " + eventSeatId
                        + " cannot be reserved from status "
                        + status
        );
    }
}
