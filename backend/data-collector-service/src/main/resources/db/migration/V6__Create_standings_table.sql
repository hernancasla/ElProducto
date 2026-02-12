-- Tabla: standings
-- Descripción: Almacena tabla de posiciones de ligas obtenidas de API-Football
-- Autor: ElProducto Team
-- Fecha: 2025-12-19

CREATE TABLE IF NOT EXISTS standings (
    id BIGSERIAL PRIMARY KEY,

    -- League info
    league_id BIGINT NOT NULL,
    league_name VARCHAR(255),
    league_country VARCHAR(255),
    league_logo VARCHAR(500),
    league_season INTEGER NOT NULL,

    -- Standing info
    rank INTEGER NOT NULL,
    team_id BIGINT NOT NULL,
    team_name VARCHAR(255) NOT NULL,
    team_logo VARCHAR(500),
    points INTEGER,
    goals_diff INTEGER,
    group_name VARCHAR(255),
    form VARCHAR(20),
    status VARCHAR(50),
    description VARCHAR(255),

    -- All matches stats
    all_played INTEGER,
    all_win INTEGER,
    all_draw INTEGER,
    all_lose INTEGER,
    all_goals_for INTEGER,
    all_goals_against INTEGER,

    -- Home matches stats
    home_played INTEGER,
    home_win INTEGER,
    home_draw INTEGER,
    home_lose INTEGER,
    home_goals_for INTEGER,
    home_goals_against INTEGER,

    -- Away matches stats
    away_played INTEGER,
    away_win INTEGER,
    away_draw INTEGER,
    away_lose INTEGER,
    away_goals_for INTEGER,
    away_goals_against INTEGER,

    last_update VARCHAR(50),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Constraint: un equipo solo puede tener una posición por liga y temporada
    CONSTRAINT uk_standings_league_season_team UNIQUE (league_id, league_season, team_id)
);

-- Índices para mejorar el rendimiento de búsquedas
CREATE INDEX idx_standings_league_id ON standings(league_id);
CREATE INDEX idx_standings_league_season ON standings(league_season);
CREATE INDEX idx_standings_league_id_season ON standings(league_id, league_season);
CREATE INDEX idx_standings_team_id ON standings(team_id);
CREATE INDEX idx_standings_rank ON standings(rank);
CREATE INDEX idx_standings_points ON standings(points DESC);

-- Índice para ordenar tabla de posiciones
CREATE INDEX idx_standings_league_season_rank ON standings(league_id, league_season, rank);

-- Comentarios en las columnas
COMMENT ON TABLE standings IS 'Tabla de posiciones de ligas de fútbol disponibles en API-Football';
COMMENT ON COLUMN standings.id IS 'Identificador único autogenerado';
COMMENT ON COLUMN standings.league_id IS 'ID de la liga en API-Football';
COMMENT ON COLUMN standings.league_name IS 'Nombre de la liga';
COMMENT ON COLUMN standings.league_country IS 'País de la liga';
COMMENT ON COLUMN standings.league_logo IS 'URL del logo de la liga';
COMMENT ON COLUMN standings.league_season IS 'Año de la temporada';
COMMENT ON COLUMN standings.rank IS 'Posición en la tabla';
COMMENT ON COLUMN standings.team_id IS 'ID del equipo en API-Football';
COMMENT ON COLUMN standings.team_name IS 'Nombre del equipo';
COMMENT ON COLUMN standings.team_logo IS 'URL del logo del equipo';
COMMENT ON COLUMN standings.points IS 'Puntos totales';
COMMENT ON COLUMN standings.goals_diff IS 'Diferencia de goles';
COMMENT ON COLUMN standings.group_name IS 'Nombre del grupo (para torneos con grupos)';
COMMENT ON COLUMN standings.form IS 'Últimos 5 resultados (ej: WWDLW)';
COMMENT ON COLUMN standings.status IS 'Estado actual (same, up, down)';
COMMENT ON COLUMN standings.description IS 'Descripción del estado (ej: Promotion - Copa Libertadores)';
COMMENT ON COLUMN standings.all_played IS 'Total de partidos jugados';
COMMENT ON COLUMN standings.all_win IS 'Total de victorias';
COMMENT ON COLUMN standings.all_draw IS 'Total de empates';
COMMENT ON COLUMN standings.all_lose IS 'Total de derrotas';
COMMENT ON COLUMN standings.all_goals_for IS 'Total de goles a favor';
COMMENT ON COLUMN standings.all_goals_against IS 'Total de goles en contra';
COMMENT ON COLUMN standings.home_played IS 'Partidos jugados de local';
COMMENT ON COLUMN standings.home_win IS 'Victorias de local';
COMMENT ON COLUMN standings.home_draw IS 'Empates de local';
COMMENT ON COLUMN standings.home_lose IS 'Derrotas de local';
COMMENT ON COLUMN standings.home_goals_for IS 'Goles a favor de local';
COMMENT ON COLUMN standings.home_goals_against IS 'Goles en contra de local';
COMMENT ON COLUMN standings.away_played IS 'Partidos jugados de visitante';
COMMENT ON COLUMN standings.away_win IS 'Victorias de visitante';
COMMENT ON COLUMN standings.away_draw IS 'Empates de visitante';
COMMENT ON COLUMN standings.away_lose IS 'Derrotas de visitante';
COMMENT ON COLUMN standings.away_goals_for IS 'Goles a favor de visitante';
COMMENT ON COLUMN standings.away_goals_against IS 'Goles en contra de visitante';
COMMENT ON COLUMN standings.last_update IS 'Fecha de última actualización desde API-Football';
COMMENT ON COLUMN standings.created_at IS 'Fecha y hora de creación del registro';
COMMENT ON COLUMN standings.updated_at IS 'Fecha y hora de última actualización';