package com.smartticket.payment.internal.exceptions;

import com.smartticket.common.exception.ConflictException;
import com.smartticket.payment.internal.domain.PaymentStatus;

public class PaymentCannotBeCompletedException extends ConflictException {

    public PaymentCannotBeCompletedException(PaymentStatus status) {
        super("Payment cannot be completed from status: " + status);
    }
}
