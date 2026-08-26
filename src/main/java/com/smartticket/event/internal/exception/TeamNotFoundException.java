package com.smartticket.event.internal.exception;

import com.smartticket.common.exception.NotFoundException;

import java.util.UUID;

public class TeamNotFoundException extends NotFoundException {

    public TeamNotFoundException(UUID id) {
        super("Team not found: " + id);
    }
}
