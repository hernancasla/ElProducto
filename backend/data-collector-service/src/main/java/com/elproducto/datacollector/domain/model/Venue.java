package com.elproducto.datacollector.domain.model;

/**
 * Modelo de dominio para Estadio/Venue
 * Representa un estadio o recinto deportivo
 */
public record Venue(
    Long apiFootballId,
    String name,
    String address,
    String city,
    Integer capacity,
    String surface,
    String image
) {
    public Venue {
        if (apiFootballId == null || apiFootballId <= 0) {
            throw new IllegalArgumentException("Venue API Football ID must be positive");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Venue name cannot be null or empty");
        }
    }

    @Override
    public String toString() {
        return "Venue{id=%d, name='%s', city='%s', capacity=%d}".formatted(
            apiFootballId, name, city, capacity
        );
    }
}