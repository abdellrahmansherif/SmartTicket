package com.smartticket.order.internal.domain;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "order",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_purchase_order_order_number",
                        columnNames = "order_number"
                )
        }
)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "reservation_id", nullable = false)
    private UUID reservationId;

    @Column(name = "order_number", nullable = false, unique = true)
    private String orderNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Column(
            name = "total_amount",
            nullable = false,
            precision = 12,
            scale = 2
    )
    private BigDecimal totalAmount;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "paid_at")
    private Instant paidAt;

    @Column(name = "cancelled_at")
    private Instant cancelledAt;

    public void markAsPaid() {

        if (status == OrderStatus.PAID) {
            return;
        }

        if (status != OrderStatus.PENDING) {
            throw new IllegalStateException(
                    "Order cannot be paid from status: " + status
            );
        }

        this.status = OrderStatus.PAID;
        this.paidAt = Instant.now();
    }
}