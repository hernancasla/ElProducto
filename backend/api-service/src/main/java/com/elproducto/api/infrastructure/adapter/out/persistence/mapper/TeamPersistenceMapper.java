package com.elproducto.api.infrastructure.adapter.out.persistence.mapper;

import com.elproducto.api.domain.model.Team;
import com.elproducto.api.infrastructure.adapter.out.persistence.entity.TeamJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TeamPersistenceMapper {
    Team toDomain(TeamJpaEntity entity);
    TeamJpaEntity toEntity(Team domain);
}
