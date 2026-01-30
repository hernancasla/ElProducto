package com.elproducto.datacollector.infrastructure.adapter.http.dto;

import com.elproducto.datacollector.domain.model.Standing;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO para mapear cada entrada de la tabla de posiciones
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record StandingEntryDto(
    @JsonProperty("rank") Integer rank,
    @JsonProperty("team") StandingTeamDto team,
    @JsonProperty("points") Integer points,
    @JsonProperty("goalsDiff") Integer goalsDiff,
    @JsonProperty("group") String group,
    @JsonProperty("form") String form,
    @JsonProperty("status") String status,
    @JsonProperty("description") String description,
    @JsonProperty("all") StandingStatsDto all,
    @JsonProperty("home") StandingStatsDto home,
    @JsonProperty("away") StandingStatsDto away,
    @JsonProperty("update") String update
) {
    /**
     * Convierte el DTO a modelo de dominio Standing
     * @param leagueId ID de la liga
     * @param leagueName Nombre de la liga
     * @param leagueCountry País de la liga
     * @param leagueLogo Logo de la liga
     * @param leagueSeason Temporada
     */
    public Standing toDomain(Long leagueId, String leagueName, String leagueCountry,
                             String leagueLogo, Integer leagueSeason) {
        return new Standing(
            leagueId,
            leagueName,
            leagueCountry,
            leagueLogo,
            leagueSeason,
            rank,
            team != null ? team.id() : null,
            team != null ? team.name() : null,
            team != null ? team.logo() : null,
            points,
            goalsDiff,
            group,
            form,
            status,
            description,
            // All stats
            all != null ? all.played() : null,
            all != null ? all.win() : null,
            all != null ? all.draw() : null,
            all != null ? all.lose() : null,
            all != null && all.goals() != null ? all.goals().goalsFor() : null,
            all != null && all.goals() != null ? all.goals().goalsAgainst() : null,
            // Home stats
            home != null ? home.played() : null,
            home != null ? home.win() : null,
            home != null ? home.draw() : null,
            home != null ? home.lose() : null,
            home != null && home.goals() != null ? home.goals().goalsFor() : null,
            home != null && home.goals() != null ? home.goals().goalsAgainst() : null,
            // Away stats
            away != null ? away.played() : null,
            away != null ? away.win() : null,
            away != null ? away.draw() : null,
            away != null ? away.lose() : null,
            away != null && away.goals() != null ? away.goals().goalsFor() : null,
            away != null && away.goals() != null ? away.goals().goalsAgainst() : null,
            update
        );
    }
}
