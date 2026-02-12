package com.elproducto.datacollector.domain.model;

/**
 * Modelo de dominio para Evento de Partido
 * Representa un evento dentro de un partido (gol, tarjeta, sustitución, etc.)
 */
public record FixtureEvent(
    Long fixtureId,
    // Tiempo
    Integer timeElapsed,
    Integer timeExtra,
    // Equipo
    Long teamId,
    String teamName,
    String teamLogo,
    // Jugador principal
    Long playerId,
    String playerName,
    // Jugador secundario (asistencia o jugador sustituido)
    Long assistId,
    String assistName,
    // Tipo de evento
    String type,       // "Goal", "Card", "subst", "Var"
    String detail,     // "Normal Goal", "Penalty", "Yellow Card", "Red Card", etc.
    String comments
) {
    public FixtureEvent {
        if (fixtureId == null || fixtureId <= 0) {
            throw new IllegalArgumentException("Fixture ID must be positive");
        }
    }

    /**
     * Verifica si el evento es un gol
     */
    public boolean isGoal() {
        return "Goal".equalsIgnoreCase(type);
    }

    /**
     * Verifica si el evento es una tarjeta
     */
    public boolean isCard() {
        return "Card".equalsIgnoreCase(type);
    }

    /**
     * Verifica si el evento es una sustitución
     */
    public boolean isSubstitution() {
        return "subst".equalsIgnoreCase(type);
    }

    /**
     * Verifica si el evento es una tarjeta roja
     */
    public boolean isRedCard() {
        return isCard() && "Red Card".equalsIgnoreCase(detail);
    }

    /**
     * Verifica si el evento es una tarjeta amarilla
     */
    public boolean isYellowCard() {
        return isCard() && "Yellow Card".equalsIgnoreCase(detail);
    }

    /**
     * Obtiene el minuto completo del evento (ej: "45+2")
     */
    public String getFullTime() {
        if (timeExtra != null && timeExtra > 0) {
            return timeElapsed + "+" + timeExtra;
        }
        return String.valueOf(timeElapsed);
    }

    @Override
    public String toString() {
        return "FixtureEvent{%s' %s - %s (%s)}".formatted(
            getFullTime(), type, playerName, detail
        );
    }
}