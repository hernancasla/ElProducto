package com.elproducto.api.config;

import com.elproducto.api.dto.LeagueDTO;
import com.elproducto.api.dto.MatchDTO;
import com.elproducto.api.dto.MatchDetailDTO;
import com.elproducto.api.dto.MigrationDTO;
import com.elproducto.api.dto.ScoreDTO;
import com.elproducto.api.dto.TeamDTO;
import com.elproducto.api.dto.TeamSummaryDTO;
import com.elproducto.api.dto.VenueDTO;
import com.elproducto.api.entity.League;
import com.elproducto.api.entity.Match;
import com.elproducto.api.entity.Team;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;

/**
 * Registers reflection and resource hints required for GraalVM Native Image.
 *
 * Spring Boot's AOT processing handles most of the Spring Framework internals,
 * but domain-specific classes (entities, DTOs) need explicit registration
 * when used via reflection (e.g., Jackson serialization, Hibernate proxies).
 */
public class NativeRuntimeHints implements RuntimeHintsRegistrar {

    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        // JPA Entities — Hibernate accesses fields/methods via reflection
        hints.reflection().registerType(Match.class, MemberCategory.values());
        hints.reflection().registerType(Team.class, MemberCategory.values());
        hints.reflection().registerType(League.class, MemberCategory.values());

        // DTOs — Jackson serializes/deserializes via reflection
        hints.reflection().registerType(MatchDTO.class, MemberCategory.values());
        hints.reflection().registerType(MatchDetailDTO.class, MemberCategory.values());
        hints.reflection().registerType(TeamDTO.class, MemberCategory.values());
        hints.reflection().registerType(TeamSummaryDTO.class, MemberCategory.values());
        hints.reflection().registerType(LeagueDTO.class, MemberCategory.values());
        hints.reflection().registerType(ScoreDTO.class, MemberCategory.values());
        hints.reflection().registerType(VenueDTO.class, MemberCategory.values());
        hints.reflection().registerType(MigrationDTO.class, MemberCategory.values());

        // Flyway SQL migration files — must be accessible at runtime in native image
        hints.resources().registerPattern("db/migration/*.sql");
    }
}
