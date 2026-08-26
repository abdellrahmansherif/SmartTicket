package com.smartticket.venue.internal.exceptions;

import com.smartticket.common.exception.ConflictException;

public class SectionAlreadyExistsException extends ConflictException {

    public SectionAlreadyExistsException(String name) {
        super("Section already exists in this venue: " + name);
    }
}
