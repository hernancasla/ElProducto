package com.elproducto.api.infrastructure.adapter.in.web.mapper;

import com.elproducto.api.domain.model.Team;
import com.elproducto.api.infrastructure.adapter.in.web.dto.TeamResponse;
import com.elproducto.api.infrastructure.adapter.in.web.dto.TeamSummaryResponse;
import com.elproducto.api.infrastructure.adapter.in.web.dto.VenueResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TeamWebMapper {

    @Mapping(target = "venue", expression = "java(mapVenue(team))")
    TeamResponse toResponse(Team team);

    TeamSummaryResponse toSummaryResponse(Team team);

    default VenueResponse mapVenue(Team team) {
        if (team.getVenueName() == null) {
            return null;
        }
        return VenueResponse.builder()
                .name(team.getVenueName())
                .city(team.getVenueCity())
                .capacity(team.getVenueCapacity())
                .build();
    }
}
