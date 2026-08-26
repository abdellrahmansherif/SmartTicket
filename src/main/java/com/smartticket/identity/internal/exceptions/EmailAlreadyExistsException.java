package com.smartticket.identity.internal.exceptions;

import com.smartticket.common.exception.ConflictException;

public class EmailAlreadyExistsException extends ConflictException {

    public EmailAlreadyExistsException() {
        super("Email already exists" );
    }
}
