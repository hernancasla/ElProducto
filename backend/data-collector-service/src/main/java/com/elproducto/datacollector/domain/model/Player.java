package com.elproducto.datacollector.domain.model;

import java.time.LocalDate;

/**
 * Modelo de dominio para Jugador (Player)
 * Representa un jugador de fútbol
 */
public record Player(
    Long apiFootballId,
    String name,
    String firstName,
    String lastName,
    Integer age,
    LocalDate birthDate,
    String birthPlace,
    String birthCountry,
    String nationality,
    String height,
    String weight,
    Boolean injured,
    String photo,
    Long teamId,
    String teamName
) {
    public Player {
        if (apiFootballId == null || apiFootballId <= 0) {
            throw new IllegalArgumentException("Player API Football ID must be positive");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Player name cannot be null or empty");
        }
    }

    @Override
    public String toString() {
        return "Player{id=%d, name='%s', nationality='%s', team='%s'}".formatted(
            apiFootballId, name, nationality, teamName
        );
    }
}