package com.elproducto.api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "teams")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Team {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    private String code;
    
    private String logo;
    
    private String country;
    
    private Integer founded;
    
    @Column(name = "venue_name")
    private String venueName;
    
    @Column(name = "venue_city")
    private String venueCity;
    
    @Column(name = "venue_capacity")
    private Integer venueCapacity;
    
    @Column(name = "api_id", unique = true)
    private Long apiId;
}
