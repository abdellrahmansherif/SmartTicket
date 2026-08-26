package com.smartticket.payment.internal.exceptions;

import com.smartticket.common.exception.NotFoundException;

public class PaymentTransactionNotFoundException extends NotFoundException {

    public PaymentTransactionNotFoundException(String transactionId) {
        super("Payment not found for transaction: " + transactionId);
    }
}
