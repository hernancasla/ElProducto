package com.elproducto.datacollector.infrastructure.adapter.http.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO para mapear datos de un jugador desde API-Football
 *
 * Estructura:
 * {
 *   "id": 276,
 *   "name": "N. Fekir",
 *   "firstname": "Nabil",
 *   "lastname": "Fekir",
 *   "age": 28,
 *   "birth": { "date": "1993-07-18", "place": "Lyon", "country": "France" },
 *   "nationality": "France",
 *   "height": "173 cm",
 *   "weight": "72 kg",
 *   "injured": false,
 *   "photo": "https://media.api-sports.io/football/players/276.png"
 * }
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record PlayerDto(
    @JsonProperty("id") Long id,
    @JsonProperty("name") String name,
    @JsonProperty("firstname") String firstname,
    @JsonProperty("lastname") String lastname,
    @JsonProperty("age") Integer age,
    @JsonProperty("birth") PlayerBirthDto birth,
    @JsonProperty("nationality") String nationality,
    @JsonProperty("height") String height,
    @JsonProperty("weight") String weight,
    @JsonProperty("injured") Boolean injured,
    @JsonProperty("photo") String photo
) {}