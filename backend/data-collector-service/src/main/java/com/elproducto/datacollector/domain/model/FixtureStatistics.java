package com.elproducto.datacollector.domain.model;

/**
 * Modelo de dominio para Estadísticas de Partido
 * Representa las estadísticas de un equipo en un partido específico
 */
public record FixtureStatistics(
    Long fixtureId,
    Long teamId,
    String teamName,
    String teamLogo,
    // Estadísticas de tiros
    Integer shotsOnGoal,
    Integer shotsOffGoal,
    Integer totalShots,
    Integer blockedShots,
    Integer shotsInsideBox,
    Integer shotsOutsideBox,
    // Estadísticas de juego
    Integer fouls,
    Integer cornerKicks,
    Integer offsides,
    Integer ballPossession,
    // Tarjetas
    Integer yellowCards,
    Integer redCards,
    // Portero
    Integer goalkeeperSaves,
    // Pases
    Integer totalPasses,
    Integer passesAccurate,
    Integer passesPercentage,
    // Otros
    Integer expectedGoals
) {
    public FixtureStatistics {
        if (fixtureId == null || fixtureId <= 0) {
            throw new IllegalArgumentException("Fixture ID must be positive");
        }
        if (teamId == null || teamId <= 0) {
            throw new IllegalArgumentException("Team ID must be positive");
        }
    }

    @Override
    public String toString() {
        return "FixtureStatistics{fixtureId=%d, team=%s, possession=%d%%, shots=%d, fouls=%d}"
            .formatted(fixtureId, teamName, ballPossession != null ? ballPossession : 0,
                       totalShots != null ? totalShots : 0, fouls != null ? fouls : 0);
    }
}