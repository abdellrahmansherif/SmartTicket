package com.smartticket.event.internal.exceptions;

import com.smartticket.common.exception.BadRequestException;

public class SameTeamMatchException extends BadRequestException {

    public SameTeamMatchException() {
        super("Home team and away team cannot be the same");
    }
}
