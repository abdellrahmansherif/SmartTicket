package com.smartticket.event.internal.web.controller;

import com.smartticket.event.internal.application.FootballMatchService;
import com.smartticket.event.internal.web.request.CreateFootballMatchRequest;
import com.smartticket.event.internal.web.request.UpdateFootballMatchRequest;
import com.smartticket.event.internal.web.response.FootballMatchResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class FootballMatchController {

    private final FootballMatchService footballMatchService;

    @PostMapping("/api/admin/football-matches")
    @ResponseStatus(HttpStatus.CREATED)
    public FootballMatchResponse createFootballMatch(
            @Valid @RequestBody CreateFootballMatchRequest request
    ) {
        return footballMatchService.create(request);
    }

    @GetMapping("/api/football-matches")
    public List<FootballMatchResponse> getAllFootballMatches(
            @RequestParam(required = false) UUID competitionId,
            @RequestParam(required = false) UUID teamId
    ) {
        if (competitionId != null) {
            return footballMatchService.getByCompetition(competitionId);
        }

        if (teamId != null) {
            return footballMatchService.getByTeam(teamId);
        }

        return footballMatchService.getAll();
    }

    @GetMapping("/api/football-matches/{id}")
    public FootballMatchResponse getFootballMatchById(
            @PathVariable UUID id
    ) {
        return footballMatchService.getById(id);
    }

    @PutMapping("/api/admin/football-matches/{id}")
    public FootballMatchResponse updateFootballMatch(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateFootballMatchRequest request
    ) {
        return footballMatchService.update(id, request);
    }

    @DeleteMapping("/api/admin/football-matches/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFootballMatch(@PathVariable UUID id) {
        footballMatchService.delete(id);
    }
}
