package com.elproducto.api.infrastructure.adapter.in.web;

import com.elproducto.api.domain.model.League;
import com.elproducto.api.domain.port.in.GetLeagueByIdUseCase;
import com.elproducto.api.domain.port.in.GetLeaguesUseCase;
import com.elproducto.api.infrastructure.adapter.in.web.dto.ApiResponse;
import com.elproducto.api.infrastructure.adapter.in.web.dto.LeagueResponse;
import com.elproducto.api.infrastructure.adapter.in.web.dto.PageResponse;
import com.elproducto.api.infrastructure.adapter.in.web.mapper.LeagueWebMapper;
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

@RestController
@RequestMapping("/api/v1/leagues")
@RequiredArgsConstructor
@Tag(name = "Leagues", description = "Endpoints for leagues management")
@CrossOrigin(origins = "${app.cors.allowed-origins}")
public class LeagueController {

    private final GetLeaguesUseCase getLeaguesUseCase;
    private final GetLeagueByIdUseCase getLeagueByIdUseCase;
    private final LeagueWebMapper leagueWebMapper;

    @GetMapping
    @Operation(summary = "Get leagues with filters and pagination")
    public ResponseEntity<PageResponse<LeagueResponse>> getLeagues(
            @Parameter(description = "Country filter") @RequestParam(required = false) String country,
            @Parameter(description = "Season year") @RequestParam(required = false) Integer season,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        Page<League> leagues = getLeaguesUseCase.execute(country, season, pageable);
        return ResponseEntity.ok(PageResponse.of(leagues.map(leagueWebMapper::toResponse)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get league by ID")
    public ResponseEntity<ApiResponse<LeagueResponse>> getLeagueById(
            @Parameter(description = "League ID") @PathVariable Long id) {
        League league = getLeagueByIdUseCase.execute(id);
        return ResponseEntity.ok(ApiResponse.success(leagueWebMapper.toResponse(league)));
    }
}
