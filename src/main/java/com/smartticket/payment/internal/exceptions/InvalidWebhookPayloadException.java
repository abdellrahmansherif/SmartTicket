package com.smartticket.payment.internal.exceptions;

import com.smartticket.common.exception.BadRequestException;

public class InvalidWebhookPayloadException extends BadRequestException {

    public InvalidWebhookPayloadException() {
        super("Invalid webhook payload");
    }
}
