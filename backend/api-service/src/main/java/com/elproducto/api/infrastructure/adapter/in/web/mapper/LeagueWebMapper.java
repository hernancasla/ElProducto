package com.elproducto.api.infrastructure.adapter.in.web.mapper;

import com.elproducto.api.domain.model.League;
import com.elproducto.api.infrastructure.adapter.in.web.dto.LeagueResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface LeagueWebMapper {
    LeagueResponse toResponse(League league);
}
