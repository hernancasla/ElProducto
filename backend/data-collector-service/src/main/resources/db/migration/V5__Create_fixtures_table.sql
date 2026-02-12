-- Tabla: fixtures
-- Descripción: Almacena información de partidos obtenidos de API-Football
-- Autor: ElProducto Team
-- Fecha: 2025-12-19

CREATE TABLE IF NOT EXISTS fixtures (
    id BIGSERIAL PRIMARY KEY,
    api_football_id BIGINT NOT NULL UNIQUE,
    referee VARCHAR(255),
    timezone VARCHAR(50),
    fixture_date TIMESTAMP,
    timestamp BIGINT,

    -- Venue info
    venue_id BIGINT,
    venue_name VARCHAR(255),
    venue_city VARCHAR(255),

    -- Status
    status_long VARCHAR(100),
    status_short VARCHAR(20),
    elapsed INTEGER,

    -- League info
    league_id BIGINT,
    league_name VARCHAR(255),
    league_country VARCHAR(255),
    league_logo VARCHAR(500),
    league_season INTEGER,
    league_round VARCHAR(100),

    -- Home team
    home_team_id BIGINT NOT NULL,
    home_team_name VARCHAR(255) NOT NULL,
    home_team_logo VARCHAR(500),
    home_team_winner BOOLEAN,

    -- Away team
    away_team_id BIGINT NOT NULL,
    away_team_name VARCHAR(255) NOT NULL,
    away_team_logo VARCHAR(500),
    away_team_winner BOOLEAN,

    -- Goals
    goals_home INTEGER,
    goals_away INTEGER,

    -- Score halftime
    score_halftime_home INTEGER,
    score_halftime_away INTEGER,

    -- Score fulltime
    score_fulltime_home INTEGER,
    score_fulltime_away INTEGER,

    -- Score extratime
    score_extratime_home INTEGER,
    score_extratime_away INTEGER,

    -- Score penalty
    score_penalty_home INTEGER,
    score_penalty_away INTEGER,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Índices para mejorar el rendimiento de búsquedas
CREATE INDEX idx_fixtures_api_football_id ON fixtures(api_football_id);
CREATE INDEX idx_fixtures_fixture_date ON fixtures(fixture_date);
CREATE INDEX idx_fixtures_league_id ON fixtures(league_id);
CREATE INDEX idx_fixtures_league_season ON fixtures(league_season);
CREATE INDEX idx_fixtures_league_id_season ON fixtures(league_id, league_season);
CREATE INDEX idx_fixtures_home_team_id ON fixtures(home_team_id);
CREATE INDEX idx_fixtures_away_team_id ON fixtures(away_team_id);
CREATE INDEX idx_fixtures_status_short ON fixtures(status_short);
CREATE INDEX idx_fixtures_venue_id ON fixtures(venue_id);

-- Índice parcial para partidos en vivo
CREATE INDEX idx_fixtures_live ON fixtures(status_short)
    WHERE status_short IN ('1H', 'HT', '2H', 'ET', 'BT', 'P');

-- Índice parcial para partidos programados
CREATE INDEX idx_fixtures_scheduled ON fixtures(fixture_date)
    WHERE status_short IN ('NS', 'TBD');

-- Comentarios en las columnas
COMMENT ON TABLE fixtures IS 'Catálogo de partidos de fútbol disponibles en API-Football';
COMMENT ON COLUMN fixtures.id IS 'Identificador único autogenerado';
COMMENT ON COLUMN fixtures.api_football_id IS 'ID del partido en API-Football (usado para consultas a la API)';
COMMENT ON COLUMN fixtures.referee IS 'Nombre del árbitro del partido';
COMMENT ON COLUMN fixtures.timezone IS 'Zona horaria del partido';
COMMENT ON COLUMN fixtures.fixture_date IS 'Fecha y hora del partido';
COMMENT ON COLUMN fixtures.timestamp IS 'Unix timestamp del partido';
COMMENT ON COLUMN fixtures.venue_id IS 'ID del estadio en API-Football';
COMMENT ON COLUMN fixtures.venue_name IS 'Nombre del estadio';
COMMENT ON COLUMN fixtures.venue_city IS 'Ciudad del estadio';
COMMENT ON COLUMN fixtures.status_long IS 'Estado del partido (descripción larga)';
COMMENT ON COLUMN fixtures.status_short IS 'Estado del partido (código: NS, 1H, HT, 2H, FT, etc.)';
COMMENT ON COLUMN fixtures.elapsed IS 'Minutos transcurridos del partido';
COMMENT ON COLUMN fixtures.league_id IS 'ID de la liga en API-Football';
COMMENT ON COLUMN fixtures.league_name IS 'Nombre de la liga';
COMMENT ON COLUMN fixtures.league_country IS 'País de la liga';
COMMENT ON COLUMN fixtures.league_logo IS 'URL del logo de la liga';
COMMENT ON COLUMN fixtures.league_season IS 'Año de la temporada';
COMMENT ON COLUMN fixtures.league_round IS 'Jornada o fase del torneo';
COMMENT ON COLUMN fixtures.home_team_id IS 'ID del equipo local en API-Football';
COMMENT ON COLUMN fixtures.home_team_name IS 'Nombre del equipo local';
COMMENT ON COLUMN fixtures.home_team_logo IS 'URL del logo del equipo local';
COMMENT ON COLUMN fixtures.home_team_winner IS 'Indica si el equipo local ganó';
COMMENT ON COLUMN fixtures.away_team_id IS 'ID del equipo visitante en API-Football';
COMMENT ON COLUMN fixtures.away_team_name IS 'Nombre del equipo visitante';
COMMENT ON COLUMN fixtures.away_team_logo IS 'URL del logo del equipo visitante';
COMMENT ON COLUMN fixtures.away_team_winner IS 'Indica si el equipo visitante ganó';
COMMENT ON COLUMN fixtures.goals_home IS 'Goles del equipo local (resultado final)';
COMMENT ON COLUMN fixtures.goals_away IS 'Goles del equipo visitante (resultado final)';
COMMENT ON COLUMN fixtures.score_halftime_home IS 'Goles del local al entretiempo';
COMMENT ON COLUMN fixtures.score_halftime_away IS 'Goles del visitante al entretiempo';
COMMENT ON COLUMN fixtures.score_fulltime_home IS 'Goles del local al final del tiempo reglamentario';
COMMENT ON COLUMN fixtures.score_fulltime_away IS 'Goles del visitante al final del tiempo reglamentario';
COMMENT ON COLUMN fixtures.score_extratime_home IS 'Goles del local en tiempo extra';
COMMENT ON COLUMN fixtures.score_extratime_away IS 'Goles del visitante en tiempo extra';
COMMENT ON COLUMN fixtures.score_penalty_home IS 'Goles del local en penales';
COMMENT ON COLUMN fixtures.score_penalty_away IS 'Goles del visitante en penales';
COMMENT ON COLUMN fixtures.created_at IS 'Fecha y hora de creación del registro';
COMMENT ON COLUMN fixtures.updated_at IS 'Fecha y hora de última actualización';