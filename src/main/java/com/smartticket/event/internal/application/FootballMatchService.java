package com.smartticket.event.internal.application;

import com.smartticket.event.internal.domain.*;
import com.smartticket.event.internal.exception.FootballMatchNotFoundException;
import com.smartticket.event.internal.exception.InvalidMatchDateRangeException;
import com.smartticket.event.internal.exceptions.SameTeamMatchException;
import com.smartticket.event.internal.persistence.EventRepository;
import com.smartticket.event.internal.persistence.FootballMatchRepository;
import com.smartticket.event.internal.web.request.CreateFootballMatchRequest;
import com.smartticket.event.internal.web.request.UpdateFootballMatchRequest;
import com.smartticket.event.internal.web.response.CompetitionResponse;
import com.smartticket.event.internal.web.response.FootballMatchResponse;
import com.smartticket.event.internal.web.response.TeamResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FootballMatchService {

    private final FootballMatchRepository footballMatchRepository;
    private final EventRepository eventRepository;
    private final TeamService teamService;
    private final CompetitionService competitionService;

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public FootballMatchResponse create(CreateFootballMatchRequest request) {

        validateTeams(
                request.homeTeamId(),
                request.awayTeamId()
        );

        validateDates(
                request.startsAt(),
                request.endsAt()
        );

        Team homeTeam =
                teamService.findEntity(request.homeTeamId());

        Team awayTeam =
                teamService.findEntity(request.awayTeamId());

        Competition competition =
                competitionService.findEntity(request.competitionId());

        Event event = Event.builder()
                .name(request.name())
                .description(request.description())
                .venueId(request.venueId())
                .startsAt(request.startsAt())
                .endsAt(request.endsAt())
                .status(EventStatus.SCHEDULED)
                .build();

        Event savedEvent = eventRepository.save(event);

        FootballMatch footballMatch = FootballMatch.builder()
                .event(savedEvent)
                .homeTeam(homeTeam)
                .awayTeam(awayTeam)
                .competition(competition)
                .build();

        FootballMatch savedMatch =
                footballMatchRepository.save(footballMatch);

        return toResponse(savedMatch);
    }


    public List<FootballMatchResponse> getAll() {

        return footballMatchRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }


    public FootballMatchResponse getById(UUID id) {
        return toResponse(findEntity(id));
    }


    public List<FootballMatchResponse> getByCompetition(
            UUID competitionId
    ) {

        competitionService.findEntity(competitionId);

        return footballMatchRepository
                .findByCompetitionId(competitionId)
                .stream()
                .map(this::toResponse)
                .toList();
    }


    public List<FootballMatchResponse> getByTeam(UUID teamId) {

        teamService.findEntity(teamId);

        return footballMatchRepository
                .findByHomeTeamIdOrAwayTeamId(
                        teamId,
                        teamId
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public FootballMatchResponse update(
            UUID id,
            UpdateFootballMatchRequest request
    ) {

        FootballMatch footballMatch = findEntity(id);

        validateTeams(
                request.homeTeamId(),
                request.awayTeamId()
        );

        validateDates(
                request.startsAt(),
                request.endsAt()
        );

        Team homeTeam =
                teamService.findEntity(request.homeTeamId());

        Team awayTeam =
                teamService.findEntity(request.awayTeamId());

        Competition competition =
                competitionService.findEntity(request.competitionId());

        Event event = footballMatch.getEvent();

        event.setName(request.name());
        event.setDescription(request.description());
        event.setVenueId(request.venueId());
        event.setStartsAt(request.startsAt());
        event.setEndsAt(request.endsAt());
        event.setStatus(request.status());

        footballMatch.setHomeTeam(homeTeam);
        footballMatch.setAwayTeam(awayTeam);
        footballMatch.setCompetition(competition);

        return toResponse(footballMatch);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void delete(UUID id) {

        FootballMatch footballMatch = findEntity(id);
        Event event = footballMatch.getEvent();

        footballMatchRepository.delete(footballMatch);
        footballMatchRepository.flush();

        eventRepository.delete(event);
    }


    private FootballMatch findEntity(UUID id) {

        return footballMatchRepository.findById(id)
                .orElseThrow(() ->
                        new FootballMatchNotFoundException(id)
                );
    }


    private void validateTeams(
            UUID homeTeamId,
            UUID awayTeamId
    ) {

        if (homeTeamId.equals(awayTeamId)) {
            throw new SameTeamMatchException();
        }
    }


    private void validateDates(
            LocalDateTime startsAt,
            LocalDateTime endsAt
    ) {

        if (endsAt != null && !endsAt.isAfter(startsAt)) {
            throw new InvalidMatchDateRangeException();
        }
    }


    private FootballMatchResponse toResponse(
            FootballMatch match
    ) {

        Event event = match.getEvent();

        TeamResponse homeTeam = new TeamResponse(
                match.getHomeTeam().getId(),
                match.getHomeTeam().getName(),
                match.getHomeTeam().getShortName(),
                match.getHomeTeam().getCountry(),
                match.getHomeTeam().getLogoUrl()
        );

        TeamResponse awayTeam = new TeamResponse(
                match.getAwayTeam().getId(),
                match.getAwayTeam().getName(),
                match.getAwayTeam().getShortName(),
                match.getAwayTeam().getCountry(),
                match.getAwayTeam().getLogoUrl()
        );

        CompetitionResponse competition =
                new CompetitionResponse(
                        match.getCompetition().getId(),
                        match.getCompetition().getName(),
                        match.getCompetition().getCountry(),
                        match.getCompetition().getLogoUrl()
                );

        return new FootballMatchResponse(
                match.getId(),
                event.getId(),
                event.getName(),
                event.getDescription(),
                event.getVenueId(),
                event.getStartsAt(),
                event.getEndsAt(),
                event.getStatus(),
                homeTeam,
                awayTeam,
                competition
        );
    }
}
