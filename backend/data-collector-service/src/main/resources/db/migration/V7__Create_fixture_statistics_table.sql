-- Tabla: fixture_statistics
-- Descripción: Almacena estadísticas de partidos obtenidas de API-Football
-- Autor: ElProducto Team
-- Fecha: 2025-12-19

CREATE TABLE IF NOT EXISTS fixture_statistics (
    id BIGSERIAL PRIMARY KEY,

    -- Fixture reference
    fixture_id BIGINT NOT NULL,

    -- Team info
    team_id BIGINT NOT NULL,
    team_name VARCHAR(255),
    team_logo VARCHAR(500),

    -- Estadísticas de tiros
    shots_on_goal INTEGER,
    shots_off_goal INTEGER,
    total_shots INTEGER,
    blocked_shots INTEGER,
    shots_inside_box INTEGER,
    shots_outside_box INTEGER,

    -- Estadísticas de juego
    fouls INTEGER,
    corner_kicks INTEGER,
    offsides INTEGER,
    ball_possession INTEGER,

    -- Tarjetas
    yellow_cards INTEGER,
    red_cards INTEGER,

    -- Portero
    goalkeeper_saves INTEGER,

    -- Pases
    total_passes INTEGER,
    passes_accurate INTEGER,
    passes_percentage INTEGER,

    -- Otros
    expected_goals INTEGER,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Constraint: un equipo solo puede tener una estadística por partido
    CONSTRAINT uk_fixture_statistics_fixture_team UNIQUE (fixture_id, team_id)
);

-- Índices para mejorar el rendimiento de búsquedas
CREATE INDEX idx_fixture_statistics_fixture_id ON fixture_statistics(fixture_id);
CREATE INDEX idx_fixture_statistics_team_id ON fixture_statistics(team_id);

-- Comentarios en las columnas
COMMENT ON TABLE fixture_statistics IS 'Estadísticas de partidos de fútbol disponibles en API-Football';
COMMENT ON COLUMN fixture_statistics.id IS 'Identificador único autogenerado';
COMMENT ON COLUMN fixture_statistics.fixture_id IS 'ID del partido en API-Football';
COMMENT ON COLUMN fixture_statistics.team_id IS 'ID del equipo en API-Football';
COMMENT ON COLUMN fixture_statistics.team_name IS 'Nombre del equipo';
COMMENT ON COLUMN fixture_statistics.team_logo IS 'URL del logo del equipo';
COMMENT ON COLUMN fixture_statistics.shots_on_goal IS 'Tiros al arco';
COMMENT ON COLUMN fixture_statistics.shots_off_goal IS 'Tiros fuera del arco';
COMMENT ON COLUMN fixture_statistics.total_shots IS 'Total de tiros';
COMMENT ON COLUMN fixture_statistics.blocked_shots IS 'Tiros bloqueados';
COMMENT ON COLUMN fixture_statistics.shots_inside_box IS 'Tiros dentro del área';
COMMENT ON COLUMN fixture_statistics.shots_outside_box IS 'Tiros fuera del área';
COMMENT ON COLUMN fixture_statistics.fouls IS 'Total de faltas';
COMMENT ON COLUMN fixture_statistics.corner_kicks IS 'Total de corners';
COMMENT ON COLUMN fixture_statistics.offsides IS 'Total de offside';
COMMENT ON COLUMN fixture_statistics.ball_possession IS 'Porcentaje de posesión del balón';
COMMENT ON COLUMN fixture_statistics.yellow_cards IS 'Total de tarjetas amarillas';
COMMENT ON COLUMN fixture_statistics.red_cards IS 'Total de tarjetas rojas';
COMMENT ON COLUMN fixture_statistics.goalkeeper_saves IS 'Atajadas del portero';
COMMENT ON COLUMN fixture_statistics.total_passes IS 'Total de pases';
COMMENT ON COLUMN fixture_statistics.passes_accurate IS 'Pases precisos';
COMMENT ON COLUMN fixture_statistics.passes_percentage IS 'Porcentaje de pases precisos';
COMMENT ON COLUMN fixture_statistics.expected_goals IS 'Goles esperados (xG * 100 para mantener precisión)';
COMMENT ON COLUMN fixture_statistics.created_at IS 'Fecha y hora de creación del registro';
COMMENT ON COLUMN fixture_statistics.updated_at IS 'Fecha y hora de última actualización';