package com.elproducto.datacollector.domain.model;

/**
 * Modelo de dominio para Posición en Tabla (Standing)
 * Representa la posición de un equipo en la tabla de una liga
 */
public record Standing(
    // League info
    Long leagueId,
    String leagueName,
    String leagueCountry,
    String leagueLogo,
    Integer leagueSeason,
    // Standing info
    Integer rank,
    Long teamId,
    String teamName,
    String teamLogo,
    Integer points,
    Integer goalsDiff,
    String group,
    String form,
    String status,
    String description,
    // All matches stats
    Integer allPlayed,
    Integer allWin,
    Integer allDraw,
    Integer allLose,
    Integer allGoalsFor,
    Integer allGoalsAgainst,
    // Home matches stats
    Integer homePlayed,
    Integer homeWin,
    Integer homeDraw,
    Integer homeLose,
    Integer homeGoalsFor,
    Integer homeGoalsAgainst,
    // Away matches stats
    Integer awayPlayed,
    Integer awayWin,
    Integer awayDraw,
    Integer awayLose,
    Integer awayGoalsFor,
    Integer awayGoalsAgainst,
    // Update timestamp
    String lastUpdate
) {
    public Standing {
        if (leagueId == null || leagueId <= 0) {
            throw new IllegalArgumentException("League ID must be positive");
        }
        if (teamId == null || teamId <= 0) {
            throw new IllegalArgumentException("Team ID must be positive");
        }
        if (rank == null || rank <= 0) {
            throw new IllegalArgumentException("Rank must be positive");
        }
    }

    /**
     * Verifica si el equipo está en zona de promoción/ascenso
     */
    public boolean isInPromotionZone() {
        return status != null && status.toLowerCase().contains("promotion");
    }

    /**
     * Verifica si el equipo está en zona de descenso
     */
    public boolean isInRelegationZone() {
        return status != null && status.toLowerCase().contains("relegation");
    }

    /**
     * Calcula el porcentaje de victorias
     */
    public double getWinPercentage() {
        if (allPlayed == null || allPlayed == 0) return 0.0;
        return (allWin != null ? allWin : 0) * 100.0 / allPlayed;
    }

    @Override
    public String toString() {
        return "Standing{rank=%d, team='%s', points=%d, played=%d, W/D/L=%d/%d/%d, GD=%d}".formatted(
            rank, teamName, points, allPlayed, allWin, allDraw, allLose, goalsDiff
        );
    }
}