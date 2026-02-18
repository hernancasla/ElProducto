package com.elproducto.api.controller;

import com.elproducto.api.dto.ApiResponse;
import com.elproducto.api.dto.MatchDTO;
import com.elproducto.api.dto.PageResponse;
import com.elproducto.api.dto.TeamDTO;
import com.elproducto.api.service.MatchService;
import com.elproducto.api.service.TeamService;
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
    
    private final TeamService teamService;
    private final MatchService matchService;
    
    @GetMapping
    @Operation(summary = "Get teams with filters and pagination")
    public ResponseEntity<PageResponse<TeamDTO>> getTeams(
            @Parameter(description = "Country filter") @RequestParam(required = false) String country,
            @Parameter(description = "Search by name") @RequestParam(required = false) String search,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        Page<TeamDTO> teams = teamService.findTeams(country, search, pageable);
        return ResponseEntity.ok(PageResponse.of(teams));
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get team by ID")
    public ResponseEntity<ApiResponse<TeamDTO>> getTeamById(
            @Parameter(description = "Team ID") @PathVariable Long id) {
        TeamDTO team = teamService.findById(id);
        return ResponseEntity.ok(ApiResponse.success(team));
    }
    
    @GetMapping("/{id}/matches")
    @Operation(summary = "Get matches for a team")
    public ResponseEntity<ApiResponse<List<MatchDTO>>> getTeamMatches(
            @Parameter(description = "Team ID") @PathVariable Long id,
            @Parameter(description = "Limit results") @RequestParam(defaultValue = "10") Integer limit) {
        List<MatchDTO> matches = matchService.findMatchesByTeam(id, limit);
        return ResponseEntity.ok(ApiResponse.success(matches));
    }
}
