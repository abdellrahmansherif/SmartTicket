package com.smartticket.order.internal.exceptions;

import com.smartticket.common.exception.ConflictException;

public class ReservationExpiredException extends ConflictException {

    public ReservationExpiredException() {
        super("Reservation has expired");
    }
}
