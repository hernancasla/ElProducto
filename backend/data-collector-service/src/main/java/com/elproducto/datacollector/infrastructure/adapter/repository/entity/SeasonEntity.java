package com.elproducto.datacollector.infrastructure.adapter.repository.entity;

import com.elproducto.datacollector.domain.model.Season;
import com.elproducto.datacollector.domain.model.SeasonCoverage;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entidad R2DBC para la tabla seasons
 * Representa una temporada de una liga
 */
@Table("seasons")
public class SeasonEntity implements Persistable<Long> {

    @Id
    private Long id;

    @Transient
    private boolean isNew = true;

    @Column("league_id")
    private Long leagueId;

    @Column("year")
    private Integer year;

    @Column("start_date")
    private LocalDate startDate;

    @Column("end_date")
    private LocalDate endDate;

    @Column("is_current")
    private boolean isCurrent;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;

    public SeasonEntity() {
    }

    public SeasonEntity(Long id, Long leagueId, Integer year, LocalDate startDate, LocalDate endDate,
                        boolean isCurrent, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.leagueId = leagueId;
        this.year = year;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isCurrent = isCurrent;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * Convierte un modelo de dominio Season a SeasonEntity (nueva entidad)
     */
    public static SeasonEntity fromDomain(Long leagueId, Season season) {
        LocalDateTime now = LocalDateTime.now();
        SeasonEntity entity = new SeasonEntity(
            null,  // ID autogenerado
            leagueId,
            season.year(),
            season.startDate(),
            season.endDate(),
            season.current(),
            now,
            now
        );
        entity.isNew = true;
        return entity;
    }

    /**
     * Convierte esta entidad a un modelo de dominio Season
     * @param coverage Cobertura de la temporada (debe ser cargada por separado)
     */
    public Season toDomain(SeasonCoverage coverage) {
        return new Season(
            year,
            startDate,
            endDate,
            isCurrent,
            coverage
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

    public Long getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(Long leagueId) {
        this.leagueId = leagueId;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public boolean isCurrent() {
        return isCurrent;
    }

    public void setCurrent(boolean current) {
        isCurrent = current;
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