-- Tabla: match_lineups
-- Descripcion: Almacena alineaciones de partidos obtenidas de API-Football
-- Autor: ElProducto Team
-- Fecha: 2025-12-19

CREATE TABLE IF NOT EXISTS match_lineups (
    id BIGSERIAL PRIMARY KEY,

    -- Fixture reference
    fixture_id BIGINT NOT NULL,

    -- Equipo
    team_id BIGINT NOT NULL,
    team_name VARCHAR(255),
    team_logo VARCHAR(500),
    formation VARCHAR(20),

    -- Entrenador
    coach_id BIGINT,
    coach_name VARCHAR(255),
    coach_photo VARCHAR(500),

    -- Jugador
    player_id BIGINT,
    player_name VARCHAR(255),
    player_number INTEGER,
    position VARCHAR(10),
    grid VARCHAR(10),
    is_starter BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Indices para mejorar el rendimiento de busquedas
CREATE INDEX idx_match_lineups_fixture_id ON match_lineups(fixture_id);
CREATE INDEX idx_match_lineups_team_id ON match_lineups(team_id);
CREATE INDEX idx_match_lineups_player_id ON match_lineups(player_id);

-- Indice compuesto para buscar alineacion de un equipo en un partido
CREATE INDEX idx_match_lineups_fixture_team ON match_lineups(fixture_id, team_id);

-- Indice para buscar titulares
CREATE INDEX idx_match_lineups_starters ON match_lineups(fixture_id, team_id, is_starter);

-- Indice para buscar por posicion
CREATE INDEX idx_match_lineups_position ON match_lineups(fixture_id, position);

-- Comentarios en las columnas
COMMENT ON TABLE match_lineups IS 'Alineaciones de partidos de futbol (titulares y suplentes)';
COMMENT ON COLUMN match_lineups.id IS 'Identificador unico autogenerado';
COMMENT ON COLUMN match_lineups.fixture_id IS 'ID del partido en API-Football';
COMMENT ON COLUMN match_lineups.team_id IS 'ID del equipo en API-Football';
COMMENT ON COLUMN match_lineups.team_name IS 'Nombre del equipo';
COMMENT ON COLUMN match_lineups.team_logo IS 'URL del logo del equipo';
COMMENT ON COLUMN match_lineups.formation IS 'Formacion tactica del equipo (ej: 4-3-3)';
COMMENT ON COLUMN match_lineups.coach_id IS 'ID del entrenador en API-Football';
COMMENT ON COLUMN match_lineups.coach_name IS 'Nombre del entrenador';
COMMENT ON COLUMN match_lineups.coach_photo IS 'URL de la foto del entrenador';
COMMENT ON COLUMN match_lineups.player_id IS 'ID del jugador en API-Football';
COMMENT ON COLUMN match_lineups.player_name IS 'Nombre del jugador';
COMMENT ON COLUMN match_lineups.player_number IS 'Numero de camiseta del jugador';
COMMENT ON COLUMN match_lineups.position IS 'Posicion del jugador (G, D, M, F)';
COMMENT ON COLUMN match_lineups.grid IS 'Posicion en el campo (ej: 1:1, 2:3)';
COMMENT ON COLUMN match_lineups.is_starter IS 'TRUE si es titular, FALSE si es suplente';
COMMENT ON COLUMN match_lineups.created_at IS 'Fecha y hora de creacion del registro';