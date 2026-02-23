package com.elproducto.api.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class MigrationDTO {

    /** Execution order (1, 2, 3…) */
    private Integer installedRank;

    /** Migration version string, e.g. "1", "2" */
    private String version;

    /** Human-readable description from the filename */
    private String description;

    /** Migration type: SQL, JAVA_MIGRATION, BASELINE… */
    private String type;

    /** Script filename, e.g. "V1__create_initial_schema.sql" */
    private String script;

    /**
     * Flyway state:
     * SUCCESS   – applied correctly
     * PENDING   – not yet applied
     * FAILED    – applied but failed
     * MISSING_SUCCESS – applied outside Flyway
     */
    private String state;

    /** When it was applied (null if PENDING) */
    private LocalDateTime installedOn;

    /** Execution time in milliseconds (null if PENDING) */
    private Integer executionTime;
}
