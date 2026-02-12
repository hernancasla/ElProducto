package com.elproducto.datacollector.infrastructure.adapter.http.dto;

import com.elproducto.datacollector.domain.model.Player;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO para mapear cada elemento del array response de players
 *
 * Estructura:
 * {
 *   "player": { ... },
 *   "statistics": [ ... ]
 * }
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record PlayerResponseDto(
    @JsonProperty("player") PlayerDto player,
    @JsonProperty("statistics") List<PlayerStatisticsDto> statistics
) {
    /**
     * Convierte el DTO a modelo de dominio Player
     * Toma la información del primer team en statistics como equipo actual
     */
    public Player toDomain() {
        if (player == null) {
            throw new IllegalStateException("Player data cannot be null");
        }

        // Obtener team del primer statistics si existe
        Long teamId = null;
        String teamName = null;
        if (statistics != null && !statistics.isEmpty() && statistics.get(0).team() != null) {
            teamId = statistics.get(0).team().id();
            teamName = statistics.get(0).team().name();
        }

        // Parsear fecha de nacimiento
        LocalDate birthDate = null;
        String birthPlace = null;
        String birthCountry = null;
        if (player.birth() != null) {
            birthDate = parseBirthDate(player.birth().date());
            birthPlace = player.birth().place();
            birthCountry = player.birth().country();
        }

        return new Player(
            player.id(),
            player.name(),
            player.firstname(),
            player.lastname(),
            player.age(),
            birthDate,
            birthPlace,
            birthCountry,
            player.nationality(),
            player.height(),
            player.weight(),
            player.injured(),
            player.photo(),
            teamId,
            teamName
        );
    }

    private LocalDate parseBirthDate(String dateStr) {
        if (dateStr == null || dateStr.isBlank()) {
            return null;
        }
        try {
            return LocalDate.parse(dateStr);
        } catch (Exception e) {
            return null;
        }
    }
}