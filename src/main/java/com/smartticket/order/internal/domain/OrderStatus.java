package com.smartticket.order.internal.domain;

public enum OrderStatus {

    PENDING,
    PAYMENT_PENDING,
    PAID,
    CANCELLED,
    EXPIRED,
    PAYMENT_FAILED
}