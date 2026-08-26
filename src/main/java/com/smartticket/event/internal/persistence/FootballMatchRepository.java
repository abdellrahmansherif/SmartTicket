package com.smartticket.event.internal.persistence;

import com.smartticket.event.internal.domain.FootballMatch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FootballMatchRepository extends JpaRepository<FootballMatch, UUID> {

    Optional<FootballMatch> findByEventId(UUID eventId);

    List<FootballMatch> findByCompetitionId(UUID competitionId);

    List<FootballMatch> findByHomeTeamIdOrAwayTeamId(UUID homeTeamId, UUID awayTeamId);
}
