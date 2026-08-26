package com.smartticket.event.internal.application;

import com.smartticket.event.internal.domain.CompetitionTeam;
import com.smartticket.event.internal.domain.Team;
import com.smartticket.event.internal.exception.CompetitionNotFoundException;
import com.smartticket.event.internal.exception.TeamAlreadyExistsException;
import com.smartticket.event.internal.exception.TeamNameAlreadyInUseException;
import com.smartticket.event.internal.exception.TeamNotFoundException;
import com.smartticket.event.internal.persistence.CompetitionRepository;
import com.smartticket.event.internal.persistence.CompetitionTeamRepository;
import com.smartticket.event.internal.persistence.TeamRepository;
import com.smartticket.event.internal.web.request.CreateTeamRequest;
import com.smartticket.event.internal.web.request.UpdateTeamRequest;
import com.smartticket.event.internal.web.response.TeamResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamService {

    private final CompetitionRepository competitionRepository;
    private final TeamRepository teamRepository;
    private final CompetitionTeamRepository competitionTeamRepository;

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public TeamResponse create(CreateTeamRequest request) {

        if (teamRepository.existsByNameIgnoreCase(request.name())) {
            throw new TeamAlreadyExistsException(request.name());
        }

        Team team = Team.builder()
                .name(request.name())
                .shortName(request.shortName())
                .country(request.country())
                .logoUrl(request.logoUrl())
                .build();

        Team savedTeam = teamRepository.save(team);

        return toResponse(savedTeam);
    }


    public List<TeamResponse> getAll() {

        return teamRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }


    public TeamResponse getById(UUID id) {
        return toResponse(findEntity(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public TeamResponse update(
            UUID id,
            UpdateTeamRequest request
    ) {

        Team team = findEntity(id);

        teamRepository
                .findByNameIgnoreCase(request.name())
                .filter(existingTeam ->
                        !existingTeam.getId().equals(id)
                )
                .ifPresent(existingTeam -> {
                    throw new TeamNameAlreadyInUseException(
                            request.name()
                    );
                });

        team.setName(request.name());
        team.setShortName(request.shortName());
        team.setCountry(request.country());
        team.setLogoUrl(request.logoUrl());

        return toResponse(team);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void delete(UUID id) {

        Team team = findEntity(id);

        teamRepository.delete(team);
    }


    public Team findEntity(UUID id) {

        return teamRepository.findById(id)
                .orElseThrow(() ->
                        new TeamNotFoundException(id)
                );
    }


    public List<TeamResponse> getTeamsByCompetition(
            UUID competitionId
    ) {

        if (!competitionRepository.existsById(competitionId)) {
            throw new CompetitionNotFoundException(competitionId);
        }

        List<UUID> teamIds =
                competitionTeamRepository
                        .findAllByCompetitionId(competitionId)
                        .stream()
                        .map(CompetitionTeam::getTeamId)
                        .toList();

        return teamRepository
                .findAllById(teamIds)
                .stream()
                .map(this::toResponse)
                .toList();
    }


    private TeamResponse toResponse(Team team) {

        return new TeamResponse(
                team.getId(),
                team.getName(),
                team.getShortName(),
                team.getCountry(),
                team.getLogoUrl()
        );
    }
}
