package com.elproducto.datacollector.domain.model;

/**
 * Modelo de dominio para Cobertura de Temporada (SeasonCoverage)
 * Representa qué datos están disponibles en API-Football para una temporada específica
 */
public record SeasonCoverage(
    boolean fixturesEvents,
    boolean fixturesLineups,
    boolean fixturesStatistics,
    boolean playersStatistics,
    boolean standings,
    boolean players,
    boolean topScorers,
    boolean topAssists,
    boolean topCards,
    boolean injuries,
    boolean predictions,
    boolean odds
) {
    @Override
    public String toString() {
        return "SeasonCoverage{" +
                "fixturesEvents=" + fixturesEvents +
                ", fixturesLineups=" + fixturesLineups +
                ", fixturesStatistics=" + fixturesStatistics +
                ", playersStatistics=" + playersStatistics +
                ", standings=" + standings +
                ", players=" + players +
                ", topScorers=" + topScorers +
                ", topAssists=" + topAssists +
                ", topCards=" + topCards +
                ", injuries=" + injuries +
                ", predictions=" + predictions +
                ", odds=" + odds +
                '}';
    }
}