package com.smartticket.event.internal.application;

import com.smartticket.event.internal.exception.CompetitionAlreadyExistsException;
import com.smartticket.event.internal.exception.CompetitionNameAlreadyInUseException;
import com.smartticket.event.internal.exception.CompetitionNotFoundException;
import com.smartticket.event.internal.domain.Competition;
import com.smartticket.event.internal.domain.CompetitionTeam;
import com.smartticket.event.internal.persistence.CompetitionRepository;
import com.smartticket.event.internal.persistence.CompetitionTeamRepository;
import com.smartticket.event.internal.web.request.CreateCompetitionRequest;
import com.smartticket.event.internal.web.request.UpdateCompetitionRequest;
import com.smartticket.event.internal.web.response.CompetitionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompetitionService {

    private final CompetitionRepository competitionRepository;
    private final CompetitionTeamRepository competitionTeamRepository;

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public CompetitionResponse create(CreateCompetitionRequest request) {
        if (competitionRepository.existsByNameIgnoreCase(request.name())) {
            throw new CompetitionAlreadyExistsException(request.name());
        }

        Competition competition = Competition.builder()
                .name(request.name())
                .country(request.country())
                .logoUrl(request.logoUrl())
                .build();

        return toResponse(competitionRepository.save(competition));
    }

    public List<CompetitionResponse> getAll() {
        return competitionRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public CompetitionResponse getById(UUID id) {
        return toResponse(findEntity(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public CompetitionResponse update(
            UUID id,
            UpdateCompetitionRequest request
    ) {
        Competition competition = findEntity(id);

        competitionRepository.findByNameIgnoreCase(request.name())
                .filter(other -> !other.getId().equals(id))
                .ifPresent(other -> {
                    throw new CompetitionNameAlreadyInUseException(request.name());
                });

        competition.setName(request.name());
        competition.setCountry(request.country());
        competition.setLogoUrl(request.logoUrl());

        return toResponse(competitionRepository.save(competition));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void delete(UUID id) {
        Competition competition = findEntity(id);
        competitionRepository.delete(competition);
    }

    public Competition findEntity(UUID id) {
        return competitionRepository.findById(id)
                .orElseThrow(() ->
                        new CompetitionNotFoundException(id)
                );
    }

    private CompetitionResponse toResponse(Competition competition) {
        return new CompetitionResponse(
                competition.getId(),
                competition.getName(),
                competition.getCountry(),
                competition.getLogoUrl()
        );
    }
    public List<CompetitionResponse> getCompetitionsByTeamId(UUID teamId) {

        List<CompetitionTeam> competitionTeams =
                competitionTeamRepository.findAllByTeamId(teamId);

        List<UUID> competitionIds = competitionTeams.stream()
                .map(CompetitionTeam::getCompetitionId)
                .toList();

        return competitionRepository.findAllById(competitionIds)
                .stream()
                .map(this::toResponse)
                .toList();
    }
}
