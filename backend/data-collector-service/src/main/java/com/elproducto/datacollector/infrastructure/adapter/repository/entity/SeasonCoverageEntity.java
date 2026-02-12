package com.elproducto.datacollector.infrastructure.adapter.repository.entity;

import com.elproducto.datacollector.domain.model.SeasonCoverage;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * Entidad R2DBC para la tabla season_coverage
 * Representa la cobertura de datos disponibles para una temporada
 */
@Table("season_coverage")
public class SeasonCoverageEntity implements Persistable<Long> {

    @Id
    private Long id;

    @Transient
    private boolean isNew = true;

    @Column("season_id")
    private Long seasonId;

    @Column("fixtures_events")
    private boolean fixturesEvents;

    @Column("fixtures_lineups")
    private boolean fixturesLineups;

    @Column("fixtures_statistics")
    private boolean fixturesStatistics;

    @Column("players_statistics")
    private boolean playersStatistics;

    @Column("standings")
    private boolean standings;

    @Column("players")
    private boolean players;

    @Column("top_scorers")
    private boolean topScorers;

    @Column("top_assists")
    private boolean topAssists;

    @Column("top_cards")
    private boolean topCards;

    @Column("injuries")
    private boolean injuries;

    @Column("predictions")
    private boolean predictions;

    @Column("odds")
    private boolean odds;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;

    public SeasonCoverageEntity() {
    }

    public SeasonCoverageEntity(Long id, Long seasonId, boolean fixturesEvents, boolean fixturesLineups,
                                 boolean fixturesStatistics, boolean playersStatistics, boolean standings,
                                 boolean players, boolean topScorers, boolean topAssists, boolean topCards,
                                 boolean injuries, boolean predictions, boolean odds,
                                 LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.seasonId = seasonId;
        this.fixturesEvents = fixturesEvents;
        this.fixturesLineups = fixturesLineups;
        this.fixturesStatistics = fixturesStatistics;
        this.playersStatistics = playersStatistics;
        this.standings = standings;
        this.players = players;
        this.topScorers = topScorers;
        this.topAssists = topAssists;
        this.topCards = topCards;
        this.injuries = injuries;
        this.predictions = predictions;
        this.odds = odds;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * Convierte un modelo de dominio SeasonCoverage a SeasonCoverageEntity (nueva entidad)
     */
    public static SeasonCoverageEntity fromDomain(Long seasonId, SeasonCoverage coverage) {
        LocalDateTime now = LocalDateTime.now();
        SeasonCoverageEntity entity = new SeasonCoverageEntity(
            null,  // ID autogenerado
            seasonId,
            coverage.fixturesEvents(),
            coverage.fixturesLineups(),
            coverage.fixturesStatistics(),
            coverage.playersStatistics(),
            coverage.standings(),
            coverage.players(),
            coverage.topScorers(),
            coverage.topAssists(),
            coverage.topCards(),
            coverage.injuries(),
            coverage.predictions(),
            coverage.odds(),
            now,
            now
        );
        entity.isNew = true;
        return entity;
    }

    /**
     * Convierte esta entidad a un modelo de dominio SeasonCoverage
     */
    public SeasonCoverage toDomain() {
        return new SeasonCoverage(
            fixturesEvents,
            fixturesLineups,
            fixturesStatistics,
            playersStatistics,
            standings,
            players,
            topScorers,
            topAssists,
            topCards,
            injuries,
            predictions,
            odds
        );
    }

    /**
     * Marca esta entidad como no nueva (ya existe en la BD)
     */
    public void markAsNotNew() {
        this.isNew = false;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return isNew || id == null;
    }

    // Getters y Setters
    public void setId(Long id) {
        this.id = id;
    }

    public Long getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(Long seasonId) {
        this.seasonId = seasonId;
    }

    public boolean isFixturesEvents() {
        return fixturesEvents;
    }

    public void setFixturesEvents(boolean fixturesEvents) {
        this.fixturesEvents = fixturesEvents;
    }

    public boolean isFixturesLineups() {
        return fixturesLineups;
    }

    public void setFixturesLineups(boolean fixturesLineups) {
        this.fixturesLineups = fixturesLineups;
    }

    public boolean isFixturesStatistics() {
        return fixturesStatistics;
    }

    public void setFixturesStatistics(boolean fixturesStatistics) {
        this.fixturesStatistics = fixturesStatistics;
    }

    public boolean isPlayersStatistics() {
        return playersStatistics;
    }

    public void setPlayersStatistics(boolean playersStatistics) {
        this.playersStatistics = playersStatistics;
    }

    public boolean isStandings() {
        return standings;
    }

    public void setStandings(boolean standings) {
        this.standings = standings;
    }

    public boolean isPlayers() {
        return players;
    }

    public void setPlayers(boolean players) {
        this.players = players;
    }

    public boolean isTopScorers() {
        return topScorers;
    }

    public void setTopScorers(boolean topScorers) {
        this.topScorers = topScorers;
    }

    public boolean isTopAssists() {
        return topAssists;
    }

    public void setTopAssists(boolean topAssists) {
        this.topAssists = topAssists;
    }

    public boolean isTopCards() {
        return topCards;
    }

    public void setTopCards(boolean topCards) {
        this.topCards = topCards;
    }

    public boolean isInjuries() {
        return injuries;
    }

    public void setInjuries(boolean injuries) {
        this.injuries = injuries;
    }

    public boolean isPredictions() {
        return predictions;
    }

    public void setPredictions(boolean predictions) {
        this.predictions = predictions;
    }

    public boolean isOdds() {
        return odds;
    }

    public void setOdds(boolean odds) {
        this.odds = odds;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}