package com.elproducto.datacollector.infrastructure.adapter.repository.entity;

import com.elproducto.datacollector.domain.model.Country;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * Entidad R2DBC para la tabla countries
 * Representa la estructura de datos en PostgreSQL
 * Implementa Persistable para control explícito de INSERT vs UPDATE
 */
@Table("countries")
public class CountryEntity implements Persistable<Long> {

    @Id
    private Long id;

    @Transient
    private boolean isNew = true;

    @Column("name")
    private String name;

    @Column("code")
    private String code;

    @Column("flag")
    private String flag;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;

    public CountryEntity() {
    }

    public CountryEntity(Long id, String name, String code, String flag,
                         LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.flag = flag;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * Convierte un modelo de dominio Country a CountryEntity (nueva entidad)
     */
    public static CountryEntity fromDomain(Country country) {
        LocalDateTime now = LocalDateTime.now();
        CountryEntity entity = new CountryEntity(
                null,  // ID autogenerado
                country.name(),
                country.code(),
                country.flag(),
                now,
                now
        );
        entity.isNew = true;  // Marcar explícitamente como nueva
        return entity;
    }

    /**
     * Convierte esta entidad a un modelo de dominio Country
     */
    public Country toDomain() {
        return new Country(name, code, flag);
    }

    /**
     * Marca esta entidad como no nueva (ya existe en la BD)
     * Debe ser llamado cuando se recupera de la base de datos
     */
    public void markAsNotNew() {
        this.isNew = false;
    }

    /**
     * Implementación de Persistable: determina si esta entidad es nueva
     * @return true si debe hacer INSERT, false si debe hacer UPDATE
     */
    @Override
    public boolean isNew() {
        // Si tiene ID y está marcada como no nueva, es una entidad existente
        return isNew || id == null;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
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
        return "CountryEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", flag='" + flag + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}