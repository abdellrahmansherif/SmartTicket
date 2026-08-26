package com.smartticket.event.internal.exception;

import com.smartticket.common.exception.ConflictException;

public class CompetitionAlreadyExistsException extends ConflictException {

    public CompetitionAlreadyExistsException(String name) {
        super("Competition already exists: " + name);
    }
}
