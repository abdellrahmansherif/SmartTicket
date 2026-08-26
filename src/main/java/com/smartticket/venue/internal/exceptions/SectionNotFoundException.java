package com.smartticket.venue.internal.exceptions;

import com.smartticket.common.exception.NotFoundException;

import java.util.UUID;

public class SectionNotFoundException extends NotFoundException {

    public SectionNotFoundException(UUID id) {
        super("Section not found: " + id);
    }
}
