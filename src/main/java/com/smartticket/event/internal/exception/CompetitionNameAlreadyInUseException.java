package com.smartticket.event.internal.exception;

import com.smartticket.common.exception.ConflictException;

public class CompetitionNameAlreadyInUseException extends ConflictException {

    public CompetitionNameAlreadyInUseException(String name) {
        super("Another competition already uses this name: " + name);
    }
}
