package com.elproducto.api.mapper;

import com.elproducto.api.dto.LeagueDTO;
import com.elproducto.api.entity.League;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface LeagueMapper {
    
    LeagueDTO toDTO(League league);
    
    League toEntity(LeagueDTO leagueDTO);
}
