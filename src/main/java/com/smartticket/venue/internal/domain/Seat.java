package com.smartticket.venue.internal.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "seats",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_seat_section_row_number",
                        columnNames = {
                                "section_id",
                                "row_label",
                                "seat_number"
                        }
                )
        }
)
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(
            name = "id",
            nullable = false,
            updatable = false
    )
    private UUID id;

    @Column(
            name = "row_label",
            nullable = false,
            length = 20
    )
    private String rowLabel;

    @Column(
            name = "seat_number",
            nullable = false,
            length = 20
    )
    private String seatNumber;

    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false
    )
    @JoinColumn(
            name = "section_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_seat_section"
            )
    )
    private Section section;
}