package com.elproducto.api.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "leagues")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeagueJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String type;

    private String logo;

    private String country;

    private Integer season;

    @Column(name = "api_id", unique = true)
    private Long apiId;
}
