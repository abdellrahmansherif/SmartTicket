package com.smartticket.order.internal.web.dto;

import com.smartticket.order.internal.domain.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record OrderResponse(

        UUID id,

        UUID reservationId,

        String orderNumber,

        OrderStatus status,

        BigDecimal totalAmount,

        Instant createdAt,

        Instant paidAt,

        Instant cancelledAt

) {
}