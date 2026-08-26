package com.smartticket.reservation.internal.exceptions;

import com.smartticket.common.exception.ConflictException;

public class ReservationContainsNoSeatsException extends ConflictException {

    public ReservationContainsNoSeatsException() {
        super("Reservation contains no seats");
    }
}
