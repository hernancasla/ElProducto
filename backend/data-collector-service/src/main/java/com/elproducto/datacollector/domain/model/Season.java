package com.elproducto.datacollector.domain.model;

import java.time.LocalDate;

/**
 * Modelo de dominio para Temporada (Season)
 * Representa una temporada de una liga/competición
 */
public record Season(
    Integer year,
    LocalDate startDate,
    LocalDate endDate,
    boolean current,
    SeasonCoverage coverage
) {
    public Season {
        if (year == null || year < 1900 || year > 2100) {
            throw new IllegalArgumentException("Season year must be between 1900 and 2100");
        }
        if (startDate == null) {
            throw new IllegalArgumentException("Season start date cannot be null");
        }
        if (endDate == null) {
            throw new IllegalArgumentException("Season end date cannot be null");
        }
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException(
                "Season end date must be after or equal to start date. Start: " + startDate + ", End: " + endDate
            );
        }
        if (coverage == null) {
            throw new IllegalArgumentException("Season coverage cannot be null");
        }
    }

    @Override
    public String toString() {
        return "Season{year=%d, start=%s, end=%s, current=%s}".formatted(
            year, startDate, endDate, current
        );
    }
}