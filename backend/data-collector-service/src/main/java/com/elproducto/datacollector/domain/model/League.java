package com.elproducto.datacollector.domain.model;

import java.util.List;

/**
 * Modelo de dominio para Liga (League)
 * Representa una liga o competición de fútbol
 */
public record League(
    Long apiFootballId,
    String name,
    String type,
    String logo,
    String countryCode,
    String countryName,
    List<Season> seasons
) {
    public League {
        if (apiFootballId == null || apiFootballId <= 0) {
            throw new IllegalArgumentException("League API Football ID must be positive");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("League name cannot be null or empty");
        }
        if (type == null || type.isBlank()) {
            throw new IllegalArgumentException("League type cannot be null or empty");
        }
        if (countryCode == null || countryCode.isBlank()) {
            throw new IllegalArgumentException("League country code cannot be null or empty");
        }
        if (seasons == null) {
            throw new IllegalArgumentException("League seasons cannot be null");
        }
    }

    @Override
    public String toString() {
        return "League{id=%d, name='%s', type='%s', country='%s', seasons=%d}".formatted(
            apiFootballId, name, type, countryCode, seasons.size()
        );
    }
}