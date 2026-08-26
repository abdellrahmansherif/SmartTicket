package com.smartticket.event.internal.exception;

import com.smartticket.common.exception.BadRequestException;

public class InvalidMatchDateRangeException extends BadRequestException {

    public InvalidMatchDateRangeException() {
        super("endsAt must be after startsAt");
    }
}
