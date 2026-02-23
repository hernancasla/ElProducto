package com.elproducto.api.infrastructure.config;

import com.elproducto.api.domain.model.League;
import com.elproducto.api.domain.model.Match;
import com.elproducto.api.domain.model.Team;
import com.elproducto.api.infrastructure.adapter.in.web.dto.ApiResponse;
import com.elproducto.api.infrastructure.adapter.in.web.dto.LeagueResponse;
import com.elproducto.api.infrastructure.adapter.in.web.dto.MatchDetailResponse;
import com.elproducto.api.infrastructure.adapter.in.web.dto.MatchResponse;
import com.elproducto.api.infrastructure.adapter.in.web.dto.Metadata;
import com.elproducto.api.infrastructure.adapter.in.web.dto.PageResponse;
import com.elproducto.api.infrastructure.adapter.in.web.dto.ScoreResponse;
import com.elproducto.api.infrastructure.adapter.in.web.dto.TeamResponse;
import com.elproducto.api.infrastructure.adapter.in.web.dto.TeamSummaryResponse;
import com.elproducto.api.infrastructure.adapter.in.web.dto.VenueResponse;
import com.elproducto.api.infrastructure.adapter.out.persistence.entity.LeagueJpaEntity;
import com.elproducto.api.infrastructure.adapter.out.persistence.entity.MatchJpaEntity;
import com.elproducto.api.infrastructure.adapter.out.persistence.entity.TeamJpaEntity;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;

public class NativeRuntimeHints implements RuntimeHintsRegistrar {

    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        // Domain models — Jackson serialization via Redis cache
        hints.reflection().registerType(League.class, MemberCategory.values());
        hints.reflection().registerType(Team.class, MemberCategory.values());
        hints.reflection().registerType(Match.class, MemberCategory.values());

        // JPA Entities — Hibernate accesses fields/methods via reflection
        hints.reflection().registerType(LeagueJpaEntity.class, MemberCategory.values());
        hints.reflection().registerType(TeamJpaEntity.class, MemberCategory.values());
        hints.reflection().registerType(MatchJpaEntity.class, MemberCategory.values());

        // Response DTOs — Jackson serialization
        hints.reflection().registerType(LeagueResponse.class, MemberCategory.values());
        hints.reflection().registerType(TeamResponse.class, MemberCategory.values());
        hints.reflection().registerType(TeamSummaryResponse.class, MemberCategory.values());
        hints.reflection().registerType(MatchResponse.class, MemberCategory.values());
        hints.reflection().registerType(MatchDetailResponse.class, MemberCategory.values());
        hints.reflection().registerType(ScoreResponse.class, MemberCategory.values());
        hints.reflection().registerType(VenueResponse.class, MemberCategory.values());
        hints.reflection().registerType(ApiResponse.class, MemberCategory.values());
        hints.reflection().registerType(PageResponse.class, MemberCategory.values());
        hints.reflection().registerType(Metadata.class, MemberCategory.values());

        // Flyway SQL migration files — must be accessible at runtime in native image
        hints.resources().registerPattern("db/migration/*.sql");
    }
}
