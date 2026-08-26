package com.smartticket.payment.internal.exceptions;

import com.smartticket.common.exception.BadRequestException;

public class WebhookTransactionIdMissingException extends BadRequestException {

    public WebhookTransactionIdMissingException() {
        super("Transaction id is missing from webhook payload");
    }
}
