package com.elproducto.api.infrastructure.adapter.out.persistence.mapper;

import com.elproducto.api.domain.model.League;
import com.elproducto.api.infrastructure.adapter.out.persistence.entity.LeagueJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface LeaguePersistenceMapper {
    League toDomain(LeagueJpaEntity entity);
    LeagueJpaEntity toEntity(League domain);
}
