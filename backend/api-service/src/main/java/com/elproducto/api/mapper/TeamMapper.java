package com.elproducto.api.mapper;

import com.elproducto.api.dto.TeamDTO;
import com.elproducto.api.dto.TeamSummaryDTO;
import com.elproducto.api.dto.VenueDTO;
import com.elproducto.api.entity.Team;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TeamMapper {
    
    @Mapping(target = "venue", expression = "java(mapVenue(team))")
    TeamDTO toDTO(Team team);
    
    TeamSummaryDTO toSummaryDTO(Team team);
    
    default VenueDTO mapVenue(Team team) {
        if (team.getVenueName() == null) {
            return null;
        }
        return VenueDTO.builder()
                .name(team.getVenueName())
                .city(team.getVenueCity())
                .capacity(team.getVenueCapacity())
                .build();
    }
}
