package com.elproducto.datacollector.infrastructure.adapter.http.dto;

import com.elproducto.datacollector.domain.model.Team;
import com.elproducto.datacollector.domain.model.Venue;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO para mapear cada elemento del array "response" de la respuesta de teams de API-Football
 * Contiene la información completa del equipo y su venue
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record TeamResponseDto(
    @JsonProperty("team") TeamDto team,
    @JsonProperty("venue") VenueDto venue
) {
    /**
     * Convierte este DTO a un modelo de dominio Team
     */
    public Team toDomain() {
        if (team == null) {
            return null;
        }

        // Convertir venue a dominio (puede ser null)
        Venue venueDomain = venue != null ? venue.toDomain() : null;

        return new Team(
            team.id(),
            team.name(),
            team.code(),
            team.country(),
            team.founded(),
            team.national(),
            team.logo(),
            venueDomain
        );
    }
}