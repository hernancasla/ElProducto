package com.elproducto.api.controller;

import com.elproducto.api.dto.ApiResponse;
import com.elproducto.api.dto.LeagueDTO;
import com.elproducto.api.dto.PageResponse;
import com.elproducto.api.service.LeagueService;
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
    
    private final LeagueService leagueService;
    
    @GetMapping
    @Operation(summary = "Get leagues with filters and pagination")
    public ResponseEntity<PageResponse<LeagueDTO>> getLeagues(
            @Parameter(description = "Country filter") @RequestParam(required = false) String country,
            @Parameter(description = "Season year") @RequestParam(required = false) Integer season,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        Page<LeagueDTO> leagues = leagueService.findLeagues(country, season, pageable);
        return ResponseEntity.ok(PageResponse.of(leagues));
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get league by ID")
    public ResponseEntity<ApiResponse<LeagueDTO>> getLeagueById(
            @Parameter(description = "League ID") @PathVariable Long id) {
        LeagueDTO league = leagueService.findById(id);
        return ResponseEntity.ok(ApiResponse.success(league));
    }
}
