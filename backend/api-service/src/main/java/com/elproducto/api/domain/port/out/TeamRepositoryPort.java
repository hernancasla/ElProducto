package com.elproducto.api.domain.port.out;

import com.elproducto.api.domain.model.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface TeamRepositoryPort {
    Page<Team> findAll(String country, String search, Pageable pageable);
    Optional<Team> findById(Long id);
}
