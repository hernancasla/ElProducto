package com.elproducto.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeagueDTO {
    private Long id;
    private String name;
    private String type;
    private String logo;
    private String country;
    private Integer season;
}
