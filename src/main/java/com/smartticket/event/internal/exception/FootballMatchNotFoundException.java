package com.smartticket.event.internal.exception;

import com.smartticket.common.exception.NotFoundException;

import java.util.UUID;

public class FootballMatchNotFoundException extends NotFoundException {

    public FootballMatchNotFoundException(UUID id) {
        super("Football match not found: " + id);
    }
}
