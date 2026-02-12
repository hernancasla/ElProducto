package com.elproducto.datacollector.infrastructure.adapter.repository.entity;

import com.elproducto.datacollector.domain.model.Venue;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * Entidad R2DBC para la tabla venues
 * Representa la estructura de datos de estadios en PostgreSQL
 * Implementa Persistable para control explícito de INSERT vs UPDATE
 */
@Table("venues")
public class VenueEntity implements Persistable<Long> {

    @Id
    private Long id;

    @Transient
    private boolean isNew = true;

    @Column("api_football_id")
    private Long apiFootballId;

    @Column("name")
    private String name;

    @Column("address")
    private String address;

    @Column("city")
    private String city;

    @Column("capacity")
    private Integer capacity;

    @Column("surface")
    private String surface;

    @Column("image")
    private String image;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;

    public VenueEntity() {
    }

    public VenueEntity(Long id, Long apiFootballId, String name, String address, String city,
                       Integer capacity, String surface, String image,
                       LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.apiFootballId = apiFootballId;
        this.name = name;
        this.address = address;
        this.city = city;
        this.capacity = capacity;
        this.surface = surface;
        this.image = image;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * Convierte un modelo de dominio Venue a VenueEntity (nueva entidad)
     */
    public static VenueEntity fromDomain(Venue venue) {
        if (venue == null) {
            return null;
        }

        LocalDateTime now = LocalDateTime.now();
        VenueEntity entity = new VenueEntity(
            null,  // ID autogenerado
            venue.apiFootballId(),
            venue.name(),
            venue.address(),
            venue.city(),
            venue.capacity(),
            venue.surface(),
            venue.image(),
            now,
            now
        );
        entity.isNew = true;
        return entity;
    }

    /**
     * Convierte esta entidad a un modelo de dominio Venue
     */
    public Venue toDomain() {
        return new Venue(
            apiFootballId,
            name,
            address,
            city,
            capacity,
            surface,
            image
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getSurface() {
        return surface;
    }

    public void setSurface(String surface) {
        this.surface = surface;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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
        return "VenueEntity{" +
                "id=" + id +
                ", apiFootballId=" + apiFootballId +
                ", name='" + name + '\'' +
                ", city='" + city + '\'' +
                ", capacity=" + capacity +
                '}';
    }
}
