package com.smartticket.event.internal.exception;

import com.smartticket.common.exception.NotFoundException;

import java.util.UUID;

public class CompetitionNotFoundException extends NotFoundException {

    public CompetitionNotFoundException(UUID id) {
        super("Competition not found: " + id);
    }
}
