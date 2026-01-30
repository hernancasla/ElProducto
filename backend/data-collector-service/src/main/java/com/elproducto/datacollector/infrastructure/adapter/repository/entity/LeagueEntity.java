package com.elproducto.datacollector.infrastructure.adapter.repository.entity;

import com.elproducto.datacollector.domain.model.League;
import com.elproducto.datacollector.domain.model.Season;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Entidad R2DBC para la tabla leagues
 * Representa una liga o competición de fútbol
 */
@Table("leagues")
public class LeagueEntity implements Persistable<Long> {

    @Id
    private Long id;

    @Transient
    private boolean isNew = true;

    @Column("api_football_id")
    private Long apiFootballId;

    @Column("name")
    private String name;

    @Column("type")
    private String type;

    @Column("logo")
    private String logo;

    @Column("country_code")
    private String countryCode;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;

    public LeagueEntity() {
    }

    public LeagueEntity(Long id, Long apiFootballId, String name, String type, String logo,
                        String countryCode, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.apiFootballId = apiFootballId;
        this.name = name;
        this.type = type;
        this.logo = logo;
        this.countryCode = countryCode;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * Convierte un modelo de dominio League a LeagueEntity (nueva entidad)
     */
    public static LeagueEntity fromDomain(League league) {
        LocalDateTime now = LocalDateTime.now();
        LeagueEntity entity = new LeagueEntity(
            null,  // ID autogenerado
            league.apiFootballId(),
            league.name(),
            league.type(),
            league.logo(),
            league.countryCode(),
            now,
            now
        );
        entity.isNew = true;
        return entity;
    }

    /**
     * Convierte esta entidad a un modelo de dominio League
     * @param countryName Nombre del país (debe ser cargado por separado)
     * @param seasons Lista de temporadas (debe ser cargada por separado)
     */
    public League toDomain(String countryName, List<Season> seasons) {
        return new League(
            apiFootballId,
            name,
            type,
            logo,
            countryCode,
            countryName,
            seasons
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

    public Long getApiFootballId() {
        return apiFootballId;
    }

    public void setApiFootballId(Long apiFootballId) {
        this.apiFootballId = apiFootballId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
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