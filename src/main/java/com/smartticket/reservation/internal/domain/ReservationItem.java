package com.smartticket.reservation.internal.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reservation_item")
public class ReservationItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "reservation_id",
            nullable = false
    )
    private Reservation reservation;

    @Column(name = "event_seat_id", nullable = false)
    private UUID eventSeatId;

    @Column(name = "price", nullable = false, precision = 12, scale = 2)
    private BigDecimal price;
}
