package com.smartticket.order.internal.exceptions;

import org.springframework.security.access.AccessDeniedException;

public class OrderReservationAccessDeniedException extends AccessDeniedException {

    public OrderReservationAccessDeniedException() {
        super("You cannot access an order for this reservation");
    }
}
