package com.smartticket.event.internal.web.controller;

import com.smartticket.event.internal.application.CompetitionService;
import com.smartticket.event.internal.application.TeamService;
import com.smartticket.event.internal.web.request.CreateTeamRequest;
import com.smartticket.event.internal.web.request.UpdateTeamRequest;
import com.smartticket.event.internal.web.response.CompetitionResponse;
import com.smartticket.event.internal.web.response.TeamResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;
    private final CompetitionService competitionService;

    @PostMapping("/api/admin/teams")
    @ResponseStatus(HttpStatus.CREATED)
    public TeamResponse createTeam(
            @Valid @RequestBody CreateTeamRequest request
    ) {
        return teamService.create(request);
    }

    @GetMapping("/api/teams")
    public List<TeamResponse> getAllTeams() {
        return teamService.getAll();
    }

    @GetMapping("/api/teams/{id}")
    public TeamResponse getTeamById(@PathVariable UUID id) {
        return teamService.getById(id);
    }

    @PutMapping("/api/admin/teams/{id}")
    public TeamResponse updateTeam(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateTeamRequest request
    ) {
        return teamService.update(id, request);
    }

    @DeleteMapping("/api/admin/teams/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTeam(@PathVariable UUID id) {
        teamService.delete(id);
    }

    @GetMapping("/api/teams/{CompitionID}")
    public List<TeamResponse> getTeamsByCpmpiton(@PathVariable UUID CompitionID) {
        return teamService.getTeamsByCompetition(CompitionID);
    }

    @GetMapping("/api/teams/{TeamID}")
    public List<CompetitionResponse> getCompititonbyTeams(@PathVariable UUID TeamID) {
        return competitionService.getCompetitionsByTeamId(TeamID);
    }
}
