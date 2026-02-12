package com.elproducto.datacollector.infrastructure.adapter.http.dto;

import com.elproducto.datacollector.domain.model.Venue;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO para mapear la información de venue desde API-Football
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record VenueDto(
    @JsonProperty("id") Long id,
    @JsonProperty("name") String name,
    @JsonProperty("address") String address,
    @JsonProperty("city") String city,
    @JsonProperty("capacity") Integer capacity,
    @JsonProperty("surface") String surface,
    @JsonProperty("image") String image
) {
    /**
     * Convierte este DTO a un modelo de dominio Venue
     */
    public Venue toDomain() {
        // Si no hay ID válido, retornamos null (algunos equipos no tienen venue)
        if (id == null || id <= 0) {
            return null;
        }

        return new Venue(
            id,
            name,
            address,
            city,
            capacity,
            surface,
            image
        );
    }
}