package com.elproducto.api.infrastructure.adapter.in.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamResponse {
    private Long id;
    private String name;
    private String code;
    private String logo;
    private String country;
    private Integer founded;
    private VenueResponse venue;
}
