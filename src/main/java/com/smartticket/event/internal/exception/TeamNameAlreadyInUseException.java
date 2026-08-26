package com.smartticket.event.internal.exception;

import com.smartticket.common.exception.ConflictException;

public class TeamNameAlreadyInUseException extends ConflictException {

    public TeamNameAlreadyInUseException(String name) {
        super("Another team already uses this name: " + name);
    }
}
