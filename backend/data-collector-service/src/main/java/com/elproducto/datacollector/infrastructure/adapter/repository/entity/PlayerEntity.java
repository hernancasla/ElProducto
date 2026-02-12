package com.elproducto.datacollector.infrastructure.adapter.repository.entity;

import com.elproducto.datacollector.domain.model.Player;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entidad R2DBC para la tabla players
 * Representa la estructura de datos de jugadores en PostgreSQL
 * Implementa Persistable para control explícito de INSERT vs UPDATE
 */
@Table("players")
public class PlayerEntity implements Persistable<Long> {

    @Id
    private Long id;

    @Transient
    private boolean isNew = true;

    @Column("api_football_id")
    private Long apiFootballId;

    @Column("name")
    private String name;

    @Column("first_name")
    private String firstName;

    @Column("last_name")
    private String lastName;

    @Column("age")
    private Integer age;

    @Column("birth_date")
    private LocalDate birthDate;

    @Column("birth_place")
    private String birthPlace;

    @Column("birth_country")
    private String birthCountry;

    @Column("nationality")
    private String nationality;

    @Column("height")
    private String height;

    @Column("weight")
    private String weight;

    @Column("injured")
    private Boolean injured;

    @Column("photo")
    private String photo;

    @Column("team_id")
    private Long teamId;

    @Column("team_name")
    private String teamName;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;

    public PlayerEntity() {
    }

    public PlayerEntity(Long id, Long apiFootballId, String name, String firstName, String lastName,
                        Integer age, LocalDate birthDate, String birthPlace, String birthCountry,
                        String nationality, String height, String weight, Boolean injured, String photo,
                        Long teamId, String teamName, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.apiFootballId = apiFootballId;
        this.name = name;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.birthDate = birthDate;
        this.birthPlace = birthPlace;
        this.birthCountry = birthCountry;
        this.nationality = nationality;
        this.height = height;
        this.weight = weight;
        this.injured = injured;
        this.photo = photo;
        this.teamId = teamId;
        this.teamName = teamName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * Convierte un modelo de dominio Player a PlayerEntity (nueva entidad)
     * @param player Jugador del dominio
     */
    public static PlayerEntity fromDomain(Player player) {
        LocalDateTime now = LocalDateTime.now();
        PlayerEntity entity = new PlayerEntity(
            null,  // ID autogenerado
            player.apiFootballId(),
            player.name(),
            player.firstName(),
            player.lastName(),
            player.age(),
            player.birthDate(),
            player.birthPlace(),
            player.birthCountry(),
            player.nationality(),
            player.height(),
            player.weight(),
            player.injured(),
            player.photo(),
            player.teamId(),
            player.teamName(),
            now,
            now
        );
        entity.isNew = true;
        return entity;
    }

    /**
     * Convierte esta entidad a un modelo de dominio Player
     */
    public Player toDomain() {
        return new Player(
            apiFootballId,
            name,
            firstName,
            lastName,
            age,
            birthDate,
            birthPlace,
            birthCountry,
            nationality,
            height,
            weight,
            injured,
            photo,
            teamId,
            teamName
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public String getBirthCountry() {
        return birthCountry;
    }

    public void setBirthCountry(String birthCountry) {
        this.birthCountry = birthCountry;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public Boolean getInjured() {
        return injured;
    }

    public void setInjured(Boolean injured) {
        this.injured = injured;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
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
        return "PlayerEntity{" +
                "id=" + id +
                ", apiFootballId=" + apiFootballId +
                ", name='" + name + '\'' +
                ", nationality='" + nationality + '\'' +
                ", teamName='" + teamName + '\'' +
                '}';
    }
}