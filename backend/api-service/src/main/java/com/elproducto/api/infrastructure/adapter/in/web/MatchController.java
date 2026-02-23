package com.elproducto.api.infrastructure.adapter.in.web;

import com.elproducto.api.domain.model.Match;
import com.elproducto.api.domain.port.in.GetLiveMatchesUseCase;
import com.elproducto.api.domain.port.in.GetMatchByIdUseCase;
import com.elproducto.api.domain.port.in.GetMatchesUseCase;
import com.elproducto.api.infrastructure.adapter.in.web.dto.ApiResponse;
import com.elproducto.api.infrastructure.adapter.in.web.dto.MatchDetailResponse;
import com.elproducto.api.infrastructure.adapter.in.web.dto.MatchResponse;
import com.elproducto.api.infrastructure.adapter.in.web.dto.PageResponse;
import com.elproducto.api.infrastructure.adapter.in.web.mapper.MatchWebMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/matches")
@RequiredArgsConstructor
@Tag(name = "Matches", description = "Endpoints for matches management")
@CrossOrigin(origins = "${app.cors.allowed-origins}")
public class MatchController {

    private final GetMatchesUseCase getMatchesUseCase;
    private final GetMatchByIdUseCase getMatchByIdUseCase;
    private final GetLiveMatchesUseCase getLiveMatchesUseCase;
    private final MatchWebMapper matchWebMapper;

    @GetMapping
    @Operation(summary = "Get matches with filters and pagination")
    public ResponseEntity<PageResponse<MatchResponse>> getMatches(
            @Parameter(description = "League ID") @RequestParam(required = false) Long leagueId,
            @Parameter(description = "Team ID") @RequestParam(required = false) Long teamId,
            @Parameter(description = "Date (YYYY-MM-DD)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @Parameter(description = "Status (live, finished, NS, etc)") @RequestParam(required = false) String status,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort by") @RequestParam(defaultValue = "date") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "DESC") String direction) {

        Sort.Direction sortDirection = "ASC".equalsIgnoreCase(direction) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));

        Page<Match> matches = getMatchesUseCase.execute(leagueId, teamId, date, status, pageable);
        return ResponseEntity.ok(PageResponse.of(matches.map(matchWebMapper::toResponse)));
    }

    @GetMapping("/live")
    @Operation(summary = "Get live matches")
    public ResponseEntity<ApiResponse<List<MatchResponse>>> getLiveMatches() {
        List<Match> liveMatches = getLiveMatchesUseCase.execute();
        List<MatchResponse> responses = liveMatches.stream().map(matchWebMapper::toResponse).toList();
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get match by ID")
    public ResponseEntity<ApiResponse<MatchDetailResponse>> getMatchById(
            @Parameter(description = "Match ID") @PathVariable Long id) {
        Match match = getMatchByIdUseCase.execute(id);
        return ResponseEntity.ok(ApiResponse.success(matchWebMapper.toDetailResponse(match)));
    }
}
