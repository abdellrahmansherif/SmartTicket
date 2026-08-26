package com.smartticket.reservation.internal.exceptions;

import com.smartticket.common.exception.NotFoundException;

import java.util.UUID;

public class ReservationNotFoundException extends NotFoundException {

    public ReservationNotFoundException(UUID reservationId) {
        super("Reservation not found with id: " + reservationId);
    }
}
