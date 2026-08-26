package com.smartticket.reservation.internal.exceptions;

import com.smartticket.common.exception.ConflictException;

public class ReservationAlreadyCancelledException extends ConflictException {

    public ReservationAlreadyCancelledException() {
        super("Reservation is already cancelled");
    }
}
