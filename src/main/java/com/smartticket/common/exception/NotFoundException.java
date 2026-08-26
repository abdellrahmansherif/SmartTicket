package com.smartticket.common.exception;

public class NotFoundException extends ApplicationException {
    protected NotFoundException(String message) {
        super(message);
    }
}
