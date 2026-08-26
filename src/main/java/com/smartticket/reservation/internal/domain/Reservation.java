package com.smartticket.reservation.internal.domain;

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
        name = "reservations"
)
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "event_id",nullable = false)
    private UUID eventID;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private ReservationStatus status;

    @Column(name = "total_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
    @PrePersist
    protected void onCreate() {
        Instant now = Instant.now();

        this.createdAt = now;

        if (this.status == null) {
            this.status = ReservationStatus.PENDING;
        }
    }

    public void confirm() {

        if (status == ReservationStatus.CONFIRMED) {
            return;
        }

        if (status != ReservationStatus.PENDING) {
            throw new IllegalStateException(
                    "Reservation cannot be confirmed from status: "
                            + status
            );
        }

        this.status = ReservationStatus.CONFIRMED;
    }
}
