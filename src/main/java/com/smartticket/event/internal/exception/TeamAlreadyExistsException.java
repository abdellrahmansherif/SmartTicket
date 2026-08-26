package com.smartticket.event.internal.exception;

import com.smartticket.common.exception.ConflictException;

public class TeamAlreadyExistsException extends ConflictException {

    public TeamAlreadyExistsException(String name) {
        super("Team already exists: " + name);
    }
}
