package com.elproducto.datacollector.domain.model;

/**
 * Modelo de dominio para Alineación de Partido
 * Representa un jugador en la alineación de un equipo para un partido específico
 */
public record MatchLineup(
    Long fixtureId,
    // Equipo
    Long teamId,
    String teamName,
    String teamLogo,
    String formation,
    // Entrenador
    Long coachId,
    String coachName,
    String coachPhoto,
    // Jugador
    Long playerId,
    String playerName,
    Integer playerNumber,
    String position,      // "G", "D", "M", "F"
    String grid,          // Posición en el campo (ej: "1:1", "2:3")
    boolean isStarter     // true = titular, false = suplente
) {
    public MatchLineup {
        if (fixtureId == null || fixtureId <= 0) {
            throw new IllegalArgumentException("Fixture ID must be positive");
        }
        if (teamId == null || teamId <= 0) {
            throw new IllegalArgumentException("Team ID must be positive");
        }
    }

    /**
     * Verifica si el jugador es portero
     */
    public boolean isGoalkeeper() {
        return "G".equalsIgnoreCase(position);
    }

    /**
     * Verifica si el jugador es defensor
     */
    public boolean isDefender() {
        return "D".equalsIgnoreCase(position);
    }

    /**
     * Verifica si el jugador es mediocampista
     */
    public boolean isMidfielder() {
        return "M".equalsIgnoreCase(position);
    }

    /**
     * Verifica si el jugador es delantero
     */
    public boolean isForward() {
        return "F".equalsIgnoreCase(position);
    }

    /**
     * Obtiene la posición en formato largo
     */
    public String getPositionFull() {
        return switch (position != null ? position.toUpperCase() : "") {
            case "G" -> "Goalkeeper";
            case "D" -> "Defender";
            case "M" -> "Midfielder";
            case "F" -> "Forward";
            default -> "Unknown";
        };
    }

    @Override
    public String toString() {
        return "MatchLineup{#%d %s (%s) - %s}".formatted(
            playerNumber != null ? playerNumber : 0,
            playerName,
            position,
            isStarter ? "Titular" : "Suplente"
        );
    }
}