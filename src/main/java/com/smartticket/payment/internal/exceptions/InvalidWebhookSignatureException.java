package com.smartticket.payment.internal.exceptions;

import com.smartticket.common.exception.BadRequestException;

public class InvalidWebhookSignatureException extends BadRequestException {

    public InvalidWebhookSignatureException() {
        super("Invalid webhook signature");
    }
}
