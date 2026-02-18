package com.elproducto.api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "matches")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Match {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private LocalDateTime date;
    
    private Long timestamp;
    
    private String timezone;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "league_id")
    private League league;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "home_team_id")
    private Team homeTeam;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "away_team_id")
    private Team awayTeam;
    
    @Column(name = "home_goals")
    private Integer homeGoals;
    
    @Column(name = "away_goals")
    private Integer awayGoals;
    
    @Column(name = "halftime_home")
    private Integer halftimeHome;
    
    @Column(name = "halftime_away")
    private Integer halftimeAway;
    
    @Column(name = "fulltime_home")
    private Integer fulltimeHome;
    
    @Column(name = "fulltime_away")
    private Integer fulltimeAway;
    
    @Column(name = "status_long")
    private String statusLong;
    
    @Column(name = "status_short")
    private String statusShort;
    
    private Integer elapsed;
    
    @Column(name = "venue_name")
    private String venueName;
    
    @Column(name = "venue_city")
    private String venueCity;
    
    @Column(name = "referee_name")
    private String refereeName;
    
    @Column(name = "api_id", unique = true)
    private Long apiId;
}
