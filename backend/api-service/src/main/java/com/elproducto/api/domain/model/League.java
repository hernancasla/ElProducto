package com.elproducto.api.domain.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class League {
    Long id;
    String name;
    String type;
    String logo;
    String country;
    Integer season;
}
