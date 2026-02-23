package com.elproducto.api.controller;

import com.elproducto.api.dto.ApiResponse;
import com.elproducto.api.dto.MigrationDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationInfo;
import org.flywaydb.core.api.output.MigrateResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/admin/migrations")
@RequiredArgsConstructor
@Tag(name = "Admin - Migrations", description = "Flyway migration management. Requires X-Admin-Api-Key header.")
@SecurityRequirement(name = "ApiKeyAuth")
public class AdminMigrationController {

    private final Flyway flyway;

    /**
     * Returns the full migration history from Flyway's schema history table.
     * Includes both applied and pending migrations.
     */
    @GetMapping
    @Operation(summary = "List all migrations", description = "Returns migration history (applied + pending)")
    public ResponseEntity<ApiResponse<List<MigrationDTO>>> getMigrations() {
        MigrationInfo[] infos = flyway.info().all();

        List<MigrationDTO> migrations = Arrays.stream(infos)
                .map(this::toDTO)
                .toList();

        return ResponseEntity.ok(ApiResponse.<List<MigrationDTO>>builder()
                .data(migrations)
                .build());
    }

    /**
     * Triggers Flyway to apply all pending migrations.
     * Safe to call multiple times — Flyway is idempotent (skips already-applied migrations).
     */
    @PostMapping("/run")
    @Operation(summary = "Run pending migrations", description = "Applies all pending migrations. Idempotent.")
    public ResponseEntity<ApiResponse<Map<String, Object>>> runMigrations() {
        log.info("Admin triggered manual migration run");

        int pendingCount = flyway.info().pending().length;

        if (pendingCount == 0) {
            log.info("No pending migrations found");
            return ResponseEntity.ok(ApiResponse.<Map<String, Object>>builder()
                    .data(Map.of(
                            "migrationsExecuted", 0,
                            "message", "No pending migrations"
                    ))
                    .build());
        }

        MigrateResult result = flyway.migrate();
        log.info("Migration run complete: {} migrations applied", result.migrationsExecuted);

        return ResponseEntity.ok(ApiResponse.<Map<String, Object>>builder()
                .data(Map.of(
                        "migrationsExecuted", result.migrationsExecuted,
                        "initialSchemaVersion", result.initialSchemaVersion != null ? result.initialSchemaVersion : "none",
                        "targetSchemaVersion", result.targetSchemaVersion != null ? result.targetSchemaVersion : "none",
                        "success", result.success
                ))
                .build());
    }

    private MigrationDTO toDTO(MigrationInfo info) {
        LocalDateTime installedOn = null;
        if (info.getInstalledOn() != null) {
            installedOn = info.getInstalledOn()
                    .toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
        }

        return MigrationDTO.builder()
                .installedRank(info.getInstalledRank())
                .version(info.getVersion() != null ? info.getVersion().getVersion() : null)
                .description(info.getDescription())
                .type(info.getType() != null ? info.getType().name() : null)
                .script(info.getScript())
                .state(info.getState() != null ? info.getState().name() : null)
                .installedOn(installedOn)
                .executionTime(info.getExecutionTime())
                .build();
    }
}
