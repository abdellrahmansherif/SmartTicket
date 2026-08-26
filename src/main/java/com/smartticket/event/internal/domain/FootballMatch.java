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
        name = "football_matches",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_football_match_event",
                        columnNames = "event_id"
                )
        }
)
public class FootballMatch {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "event_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_football_match_event")
    )
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "home_team_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_football_match_home_team")
    )
    private Team homeTeam;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "away_team_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_football_match_away_team")
    )
    private Team awayTeam;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "competition_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_football_match_competition")
    )
    private Competition competition;
}
