package com.smartticket.event.internal.persistence;

import com.smartticket.event.internal.domain.CompetitionTeam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CompetitionTeamRepository extends JpaRepository<CompetitionTeam, UUID>{
    boolean existsByCompetitionIdAndTeamId(UUID competitionId, UUID teamId);

    List<CompetitionTeam> findAllByCompetitionId(UUID competitionId);

    List<CompetitionTeam> findAllByTeamId(UUID teamId);

    void deleteByCompetitionIdAndTeamId(UUID competitionId, UUID teamId);
}
