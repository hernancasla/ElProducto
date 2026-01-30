package com.elproducto.datacollector.domain.model;

import java.time.LocalDateTime;

/**
 * Modelo de dominio para Partido (Fixture)
 * Representa un partido de fútbol con su información completa
 */
public record Fixture(
    Long apiFootballId,
    String referee,
    String timezone,
    LocalDateTime date,
    Long timestamp,
    // Venue info
    Long venueId,
    String venueName,
    String venueCity,
    // Status
    String statusLong,
    String statusShort,
    Integer elapsed,
    // League info
    Long leagueId,
    String leagueName,
    String leagueCountry,
    String leagueLogo,
    Integer leagueSeason,
    String leagueRound,
    // Home team
    Long homeTeamId,
    String homeTeamName,
    String homeTeamLogo,
    Boolean homeTeamWinner,
    // Away team
    Long awayTeamId,
    String awayTeamName,
    String awayTeamLogo,
    Boolean awayTeamWinner,
    // Goals
    Integer goalsHome,
    Integer goalsAway,
    // Score
    Integer scoreHalftimeHome,
    Integer scoreHalftimeAway,
    Integer scoreFulltimeHome,
    Integer scoreFulltimeAway,
    Integer scoreExtratimeHome,
    Integer scoreExtratimeAway,
    Integer scorePenaltyHome,
    Integer scorePenaltyAway
) {
    public Fixture {
        if (apiFootballId == null || apiFootballId <= 0) {
            throw new IllegalArgumentException("Fixture API Football ID must be positive");
        }
        if (homeTeamId == null || homeTeamId <= 0) {
            throw new IllegalArgumentException("Home team ID must be positive");
        }
        if (awayTeamId == null || awayTeamId <= 0) {
            throw new IllegalArgumentException("Away team ID must be positive");
        }
    }

    /**
     * Verifica si el partido ya terminó
     */
    public boolean isFinished() {
        return "FT".equals(statusShort) || "AET".equals(statusShort) || "PEN".equals(statusShort);
    }

    /**
     * Verifica si el partido está en curso
     */
    public boolean isLive() {
        return "1H".equals(statusShort) || "HT".equals(statusShort) ||
               "2H".equals(statusShort) || "ET".equals(statusShort) ||
               "BT".equals(statusShort) || "P".equals(statusShort);
    }

    /**
     * Verifica si el partido está programado (aún no comenzó)
     */
    public boolean isScheduled() {
        return "NS".equals(statusShort) || "TBD".equals(statusShort);
    }

    @Override
    public String toString() {
        return "Fixture{id=%d, %s vs %s, date=%s, status=%s, score=%d-%d}".formatted(
            apiFootballId, homeTeamName, awayTeamName, date, statusShort,
            goalsHome != null ? goalsHome : 0,
            goalsAway != null ? goalsAway : 0
        );
    }
}
