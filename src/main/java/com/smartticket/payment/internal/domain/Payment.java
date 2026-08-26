package com.smartticket.payment.internal.domain;
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
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID orderId;

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    @Column(nullable = false)
    private Instant createdAt;

    private Instant paidAt;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
    }
    public void markAsPaid() {

        if (status == PaymentStatus.SUCCESS) {
            return;
        }

        if (status != PaymentStatus.PENDING) {
            throw new IllegalStateException(
                    "Payment cannot be completed from status: " + status
            );
        }

        this.status = PaymentStatus.SUCCESS;
        this.paidAt = Instant.now();
    }
}
