package com.smartticket.common.exception;

public class BadRequestException extends ApplicationException {
    protected BadRequestException(String message) {
        super(message);
    }
}
