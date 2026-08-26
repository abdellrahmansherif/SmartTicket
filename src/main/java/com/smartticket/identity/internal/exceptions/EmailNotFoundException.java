package com.smartticket.identity.internal.exceptions;

import com.smartticket.common.exception.NotFoundException;

public class EmailNotFoundException extends NotFoundException {
    public EmailNotFoundException()
    {
        super("Email Not Found");
    }
}
