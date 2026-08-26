package com.smartticket.event.internal.domain;

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
        name = "competitions",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_competition_name", columnNames = "name")
        }
)
public class Competition {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "country", length = 100)
    private String country;

    @Column(name = "logo_url", length = 500)
    private String logoUrl;
}
