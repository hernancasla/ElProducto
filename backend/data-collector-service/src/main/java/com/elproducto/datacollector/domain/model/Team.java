package com.elproducto.datacollector.domain.model;

/**
 * Modelo de dominio para Equipo (Team)
 * Representa un equipo de fútbol
 */
public record Team(
    Long apiFootballId,
    String name,
    String code,
    String country,
    Integer founded,
    Boolean national,
    String logo,
    Venue venue
) {
    public Team {
        if (apiFootballId == null || apiFootballId <= 0) {
            throw new IllegalArgumentException("Team API Football ID must be positive");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Team name cannot be null or empty");
        }
        if (country == null || country.isBlank()) {
            throw new IllegalArgumentException("Team country cannot be null or empty");
        }
    }

    @Override
    public String toString() {
        return "Team{id=%d, name='%s', country='%s', national=%s}".formatted(
            apiFootballId, name, country, national
        );
    }
}