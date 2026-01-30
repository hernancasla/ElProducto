-- Tabla: leagues
-- Descripción: Almacena información de ligas/competiciones obtenidas de API-Football
-- Autor: ElProducto Team
-- Fecha: 2025-12-15

CREATE TABLE IF NOT EXISTS leagues (
    id BIGSERIAL PRIMARY KEY,
    api_football_id BIGINT NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL,
    logo VARCHAR(500),
    country_code VARCHAR(10) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Foreign key a la tabla countries
    CONSTRAINT fk_leagues_country FOREIGN KEY (country_code)
        REFERENCES countries(code) ON DELETE RESTRICT ON UPDATE CASCADE
);

-- Índices para mejorar el rendimiento de búsquedas
CREATE INDEX idx_leagues_api_football_id ON leagues(api_football_id);
CREATE INDEX idx_leagues_country_code ON leagues(country_code);
CREATE INDEX idx_leagues_name ON leagues(name);
CREATE INDEX idx_leagues_type ON leagues(type);

-- Comentarios en las columnas
COMMENT ON TABLE leagues IS 'Catálogo de ligas/competiciones disponibles en API-Football';
COMMENT ON COLUMN leagues.id IS 'Identificador único autogenerado';
COMMENT ON COLUMN leagues.api_football_id IS 'ID de la liga en API-Football (usado para consultas a la API)';
COMMENT ON COLUMN leagues.name IS 'Nombre de la liga/competición';
COMMENT ON COLUMN leagues.type IS 'Tipo de competición (League, Cup, etc.)';
COMMENT ON COLUMN leagues.logo IS 'URL del logo de la liga';
COMMENT ON COLUMN leagues.country_code IS 'Código ISO del país al que pertenece la liga';
COMMENT ON COLUMN leagues.created_at IS 'Fecha y hora de creación del registro';
COMMENT ON COLUMN leagues.updated_at IS 'Fecha y hora de última actualización';


-- Tabla: seasons
-- Descripción: Almacena información de temporadas de cada liga
-- Autor: ElProducto Team
-- Fecha: 2025-12-15

CREATE TABLE IF NOT EXISTS seasons (
    id BIGSERIAL PRIMARY KEY,
    league_id BIGINT NOT NULL,
    year INTEGER NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    is_current BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Foreign key a la tabla leagues
    CONSTRAINT fk_seasons_league FOREIGN KEY (league_id)
        REFERENCES leagues(id) ON DELETE CASCADE ON UPDATE CASCADE,

    -- No puede haber dos temporadas del mismo año para la misma liga
    CONSTRAINT uk_seasons_league_year UNIQUE (league_id, year)
);

-- Índices para mejorar el rendimiento de búsquedas
CREATE INDEX idx_seasons_league_id ON seasons(league_id);
CREATE INDEX idx_seasons_year ON seasons(year);
CREATE INDEX idx_seasons_current ON seasons(is_current) WHERE is_current = true;

-- Comentarios en las columnas
COMMENT ON TABLE seasons IS 'Temporadas de cada liga/competición';
COMMENT ON COLUMN seasons.id IS 'Identificador único autogenerado';
COMMENT ON COLUMN seasons.league_id IS 'ID de la liga a la que pertenece esta temporada';
COMMENT ON COLUMN seasons.year IS 'Año de la temporada';
COMMENT ON COLUMN seasons.start_date IS 'Fecha de inicio de la temporada';
COMMENT ON COLUMN seasons.end_date IS 'Fecha de finalización de la temporada';
COMMENT ON COLUMN seasons.is_current IS 'Indica si es la temporada actual';
COMMENT ON COLUMN seasons.created_at IS 'Fecha y hora de creación del registro';
COMMENT ON COLUMN seasons.updated_at IS 'Fecha y hora de última actualización';


-- Tabla: season_coverage
-- Descripción: Almacena información sobre qué datos están disponibles para cada temporada
-- Autor: ElProducto Team
-- Fecha: 2025-12-15

CREATE TABLE IF NOT EXISTS season_coverage (
    id BIGSERIAL PRIMARY KEY,
    season_id BIGINT NOT NULL UNIQUE,
    fixtures_events BOOLEAN NOT NULL DEFAULT false,
    fixtures_lineups BOOLEAN NOT NULL DEFAULT false,
    fixtures_statistics BOOLEAN NOT NULL DEFAULT false,
    players_statistics BOOLEAN NOT NULL DEFAULT false,
    standings BOOLEAN NOT NULL DEFAULT false,
    players BOOLEAN NOT NULL DEFAULT false,
    top_scorers BOOLEAN NOT NULL DEFAULT false,
    top_assists BOOLEAN NOT NULL DEFAULT false,
    top_cards BOOLEAN NOT NULL DEFAULT false,
    injuries BOOLEAN NOT NULL DEFAULT false,
    predictions BOOLEAN NOT NULL DEFAULT false,
    odds BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Foreign key a la tabla seasons
    CONSTRAINT fk_coverage_season FOREIGN KEY (season_id)
        REFERENCES seasons(id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Índice para búsqueda por season
CREATE INDEX idx_coverage_season_id ON season_coverage(season_id);

-- Comentarios en las columnas
COMMENT ON TABLE season_coverage IS 'Información de cobertura de datos disponibles por temporada';
COMMENT ON COLUMN season_coverage.id IS 'Identificador único autogenerado';
COMMENT ON COLUMN season_coverage.season_id IS 'ID de la temporada';
COMMENT ON COLUMN season_coverage.fixtures_events IS 'Indica si hay eventos disponibles para los partidos';
COMMENT ON COLUMN season_coverage.fixtures_lineups IS 'Indica si hay alineaciones disponibles';
COMMENT ON COLUMN season_coverage.fixtures_statistics IS 'Indica si hay estadísticas de partidos disponibles';
COMMENT ON COLUMN season_coverage.players_statistics IS 'Indica si hay estadísticas de jugadores disponibles';
COMMENT ON COLUMN season_coverage.standings IS 'Indica si hay tablas de posiciones disponibles';
COMMENT ON COLUMN season_coverage.players IS 'Indica si hay información de jugadores disponible';
COMMENT ON COLUMN season_coverage.top_scorers IS 'Indica si hay goleadores disponibles';
COMMENT ON COLUMN season_coverage.top_assists IS 'Indica si hay asistencias disponibles';
COMMENT ON COLUMN season_coverage.top_cards IS 'Indica si hay tarjetas disponibles';
COMMENT ON COLUMN season_coverage.injuries IS 'Indica si hay información de lesiones disponible';
COMMENT ON COLUMN season_coverage.predictions IS 'Indica si hay predicciones disponibles';
COMMENT ON COLUMN season_coverage.odds IS 'Indica si hay cuotas de apuestas disponibles';
COMMENT ON COLUMN season_coverage.created_at IS 'Fecha y hora de creación del registro';
COMMENT ON COLUMN season_coverage.updated_at IS 'Fecha y hora de última actualización';