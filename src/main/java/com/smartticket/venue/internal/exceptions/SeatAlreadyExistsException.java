package com.smartticket.venue.internal.exceptions;

import com.smartticket.common.exception.ConflictException;

public class SeatAlreadyExistsException extends ConflictException {

    public SeatAlreadyExistsException(String rowLabel, String seatNumber) {
        super("Seat already exists in this section: row "
                + rowLabel + ", seat " + seatNumber);
    }
}
