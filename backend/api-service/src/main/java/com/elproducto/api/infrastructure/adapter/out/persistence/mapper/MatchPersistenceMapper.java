package com.elproducto.api.infrastructure.adapter.out.persistence.mapper;

import com.elproducto.api.domain.model.Match;
import com.elproducto.api.infrastructure.adapter.out.persistence.entity.MatchJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {LeaguePersistenceMapper.class, TeamPersistenceMapper.class})
public interface MatchPersistenceMapper {
    Match toDomain(MatchJpaEntity entity);
    MatchJpaEntity toEntity(Match domain);
}
