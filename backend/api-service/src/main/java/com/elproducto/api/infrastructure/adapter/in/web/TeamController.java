package com.elproducto.api.infrastructure.adapter.in.web;

import com.elproducto.api.domain.model.Match;
import com.elproducto.api.domain.model.Team;
import com.elproducto.api.domain.port.in.GetMatchesByTeamUseCase;
import com.elproducto.api.domain.port.in.GetTeamByIdUseCase;
import com.elproducto.api.domain.port.in.GetTeamsUseCase;
import com.elproducto.api.infrastructure.adapter.in.web.dto.ApiResponse;
import com.elproducto.api.infrastructure.adapter.in.web.dto.MatchResponse;
import com.elproducto.api.infrastructure.adapter.in.web.dto.PageResponse;
import com.elproducto.api.infrastructure.adapter.in.web.dto.TeamResponse;
import com.elproducto.api.infrastructure.adapter.in.web.mapper.MatchWebMapper;
import com.elproducto.api.infrastructure.adapter.in.web.mapper.TeamWebMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/teams")
@RequiredArgsConstructor
@Tag(name = "Teams", description = "Endpoints for teams management")
@CrossOrigin(origins = "${app.cors.allowed-origins}")
public class TeamController {

    private final GetTeamsUseCase getTeamsUseCase;
    private final GetTeamByIdUseCase getTeamByIdUseCase;
    private final GetMatchesByTeamUseCase getMatchesByTeamUseCase;
    private final TeamWebMapper teamWebMapper;
    private final MatchWebMapper matchWebMapper;

    @GetMapping
    @Operation(summary = "Get teams with filters and pagination")
    public ResponseEntity<PageResponse<TeamResponse>> getTeams(
            @Parameter(description = "Country filter") @RequestParam(required = false) String country,
            @Parameter(description = "Search by name") @RequestParam(required = false) String search,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        Page<Team> teams = getTeamsUseCase.execute(country, search, pageable);
        return ResponseEntity.ok(PageResponse.of(teams.map(teamWebMapper::toResponse)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get team by ID")
    public ResponseEntity<ApiResponse<TeamResponse>> getTeamById(
            @Parameter(description = "Team ID") @PathVariable Long id) {
        Team team = getTeamByIdUseCase.execute(id);
        return ResponseEntity.ok(ApiResponse.success(teamWebMapper.toResponse(team)));
    }

    @GetMapping("/{id}/matches")
    @Operation(summary = "Get matches for a team")
    public ResponseEntity<ApiResponse<List<MatchResponse>>> getTeamMatches(
            @Parameter(description = "Team ID") @PathVariable Long id,
            @Parameter(description = "Limit results") @RequestParam(defaultValue = "10") Integer limit) {
        List<Match> matches = getMatchesByTeamUseCase.execute(id, limit);
        List<MatchResponse> responses = matches.stream().map(matchWebMapper::toResponse).toList();
        return ResponseEntity.ok(ApiResponse.success(responses));
    }
}
