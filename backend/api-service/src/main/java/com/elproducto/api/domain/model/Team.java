package com.elproducto.api.domain.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Team {
    Long id;
    String name;
    String code;
    String logo;
    String country;
    Integer founded;
    String venueName;
    String venueCity;
    Integer venueCapacity;
}
