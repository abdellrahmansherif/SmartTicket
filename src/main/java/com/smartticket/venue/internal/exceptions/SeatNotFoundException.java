package com.smartticket.venue.internal.exceptions;

import com.smartticket.common.exception.NotFoundException;

import java.util.UUID;

public class SeatNotFoundException extends NotFoundException {

    public SeatNotFoundException(UUID id) {
        super("Seat not found: " + id);
    }
}
