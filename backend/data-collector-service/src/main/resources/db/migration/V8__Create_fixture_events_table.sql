-- Tabla: fixture_events
-- Descripción: Almacena eventos de partidos obtenidos de API-Football (goles, tarjetas, sustituciones)
-- Autor: ElProducto Team
-- Fecha: 2025-12-19

CREATE TABLE IF NOT EXISTS fixture_events (
    id BIGSERIAL PRIMARY KEY,

    -- Fixture reference
    fixture_id BIGINT NOT NULL,

    -- Tiempo del evento
    time_elapsed INTEGER,
    time_extra INTEGER,

    -- Equipo
    team_id BIGINT,
    team_name VARCHAR(255),
    team_logo VARCHAR(500),

    -- Jugador principal
    player_id BIGINT,
    player_name VARCHAR(255),

    -- Jugador secundario (asistencia o jugador sustituido)
    assist_id BIGINT,
    assist_name VARCHAR(255),

    -- Tipo de evento
    type VARCHAR(50),
    detail VARCHAR(100),
    comments TEXT,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Índices para mejorar el rendimiento de búsquedas
CREATE INDEX idx_fixture_events_fixture_id ON fixture_events(fixture_id);
CREATE INDEX idx_fixture_events_team_id ON fixture_events(team_id);
CREATE INDEX idx_fixture_events_type ON fixture_events(type);
CREATE INDEX idx_fixture_events_time ON fixture_events(time_elapsed);

-- Índice compuesto para ordenar cronológicamente
CREATE INDEX idx_fixture_events_fixture_time ON fixture_events(fixture_id, time_elapsed, time_extra);

-- Índice para buscar goles
CREATE INDEX idx_fixture_events_goals ON fixture_events(fixture_id) WHERE type = 'Goal';

-- Comentarios en las columnas
COMMENT ON TABLE fixture_events IS 'Eventos de partidos de fútbol (goles, tarjetas, sustituciones)';
COMMENT ON COLUMN fixture_events.id IS 'Identificador único autogenerado';
COMMENT ON COLUMN fixture_events.fixture_id IS 'ID del partido en API-Football';
COMMENT ON COLUMN fixture_events.time_elapsed IS 'Minuto del evento';
COMMENT ON COLUMN fixture_events.time_extra IS 'Tiempo adicional (ej: +2 en el minuto 45)';
COMMENT ON COLUMN fixture_events.team_id IS 'ID del equipo en API-Football';
COMMENT ON COLUMN fixture_events.team_name IS 'Nombre del equipo';
COMMENT ON COLUMN fixture_events.team_logo IS 'URL del logo del equipo';
COMMENT ON COLUMN fixture_events.player_id IS 'ID del jugador principal en API-Football';
COMMENT ON COLUMN fixture_events.player_name IS 'Nombre del jugador principal';
COMMENT ON COLUMN fixture_events.assist_id IS 'ID del jugador que asiste o es sustituido';
COMMENT ON COLUMN fixture_events.assist_name IS 'Nombre del jugador que asiste o es sustituido';
COMMENT ON COLUMN fixture_events.type IS 'Tipo de evento (Goal, Card, subst, Var)';
COMMENT ON COLUMN fixture_events.detail IS 'Detalle del evento (Normal Goal, Penalty, Yellow Card, Red Card, etc.)';
COMMENT ON COLUMN fixture_events.comments IS 'Comentarios adicionales del evento';
COMMENT ON COLUMN fixture_events.created_at IS 'Fecha y hora de creación del registro';