package com.smartticket.reservation.internal.exceptions;

import com.smartticket.common.exception.NotFoundException;

import java.util.UUID;

public class ReservationEventNotFoundException extends NotFoundException {

    public ReservationEventNotFoundException(UUID eventId) {
        super("Event not found with id: " + eventId);
    }
}
