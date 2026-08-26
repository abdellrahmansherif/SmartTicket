package com.smartticket.order.internal.exceptions;

import com.smartticket.common.exception.ConflictException;

public class ReservationNotOrderableException extends ConflictException {

    public ReservationNotOrderableException(String status) {
        super("Reservation is not available for ordering. Current status: " + status);
    }
}
