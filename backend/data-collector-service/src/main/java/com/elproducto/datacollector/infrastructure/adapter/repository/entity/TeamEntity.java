package com.elproducto.datacollector.infrastructure.adapter.repository.entity;

import com.elproducto.datacollector.domain.model.Team;
import com.elproducto.datacollector.domain.model.Venue;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * Entidad R2DBC para la tabla teams
 * Representa la estructura de datos de equipos en PostgreSQL
 * Implementa Persistable para control explícito de INSERT vs UPDATE
 */
@Table("teams")
public class TeamEntity implements Persistable<Long> {

    @Id
    private Long id;

    @Transient
    private boolean isNew = true;

    @Column("api_football_id")
    private Long apiFootballId;

    @Column("name")
    private String name;

    @Column("code")
    private String code;

    @Column("country")
    private String country;

    @Column("founded")
    private Integer founded;

    @Column("national")
    private Boolean national;

    @Column("logo")
    private String logo;

    @Column("venue_id")
    private Long venueId;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;

    public TeamEntity() {
    }

    public TeamEntity(Long id, Long apiFootballId, String name, String code, String country,
                      Integer founded, Boolean national, String logo, Long venueId,
                      LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.apiFootballId = apiFootballId;
        this.name = name;
        this.code = code;
        this.country = country;
        this.founded = founded;
        this.national = national;
        this.logo = logo;
        this.venueId = venueId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * Convierte un modelo de dominio Team a TeamEntity (nueva entidad)
     * @param team Equipo del dominio
     * @param venueId ID del venue en la base de datos (puede ser null)
     */
    public static TeamEntity fromDomain(Team team, Long venueId) {
        LocalDateTime now = LocalDateTime.now();
        TeamEntity entity = new TeamEntity(
            null,  // ID autogenerado
            team.apiFootballId(),
            team.name(),
            team.code(),
            team.country(),
            team.founded(),
            team.national(),
            team.logo(),
            venueId,
            now,
            now
        );
        entity.isNew = true;
        return entity;
    }

    /**
     * Convierte esta entidad a un modelo de dominio Team
     * @param venue Objeto Venue del dominio (debe ser cargado por separado)
     */
    public Team toDomain(Venue venue) {
        return new Team(
            apiFootballId,
            name,
            code,
            country,
            founded,
            national,
            logo,
            venue
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getFounded() {
        return founded;
    }

    public void setFounded(Integer founded) {
        this.founded = founded;
    }

    public Boolean getNational() {
        return national;
    }

    public void setNational(Boolean national) {
        this.national = national;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Long getVenueId() {
        return venueId;
    }

    public void setVenueId(Long venueId) {
        this.venueId = venueId;
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

    @Override
    public String toString() {
        return "TeamEntity{" +
                "id=" + id +
                ", apiFootballId=" + apiFootballId +
                ", name='" + name + '\'' +
                ", country='" + country + '\'' +
                ", national=" + national +
                '}';
    }
}