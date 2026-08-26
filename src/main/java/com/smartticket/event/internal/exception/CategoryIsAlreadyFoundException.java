package com.smartticket.event.internal.exception;

import com.smartticket.common.exception.NotFoundException;

public class CategoryIsAlreadyFoundException extends NotFoundException {
    public CategoryIsAlreadyFoundException(String message) {
        super("Category not found :" + message);
    }
}
