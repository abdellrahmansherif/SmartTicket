package com.smartticket.venue.internal.exceptions;

import com.smartticket.common.exception.NotFoundException;

import java.util.UUID;

public class VenueNotFoundException extends NotFoundException {

    public VenueNotFoundException(UUID id) {
        super("Venue not found: " + id);
    }
}
