-- Tabla: venues
-- Descripción: Almacena información de estadios/recintos obtenidos de API-Football
-- Autor: ElProducto Team
-- Fecha: 2025-12-18

CREATE TABLE IF NOT EXISTS venues (
    id BIGSERIAL PRIMARY KEY,
    api_football_id BIGINT NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    address VARCHAR(500),
    city VARCHAR(255),
    capacity INTEGER,
    surface VARCHAR(50),
    image VARCHAR(500),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Índices para mejorar el rendimiento de búsquedas
CREATE INDEX idx_venues_api_football_id ON venues(api_football_id);
CREATE INDEX idx_venues_name ON venues(name);
CREATE INDEX idx_venues_city ON venues(city);

-- Comentarios en las columnas
COMMENT ON TABLE venues IS 'Catálogo de estadios/recintos deportivos disponibles en API-Football';
COMMENT ON COLUMN venues.id IS 'Identificador único autogenerado';
COMMENT ON COLUMN venues.api_football_id IS 'ID del venue en API-Football (usado para consultas a la API)';
COMMENT ON COLUMN venues.name IS 'Nombre del estadio';
COMMENT ON COLUMN venues.address IS 'Dirección del estadio';
COMMENT ON COLUMN venues.city IS 'Ciudad donde se encuentra el estadio';
COMMENT ON COLUMN venues.capacity IS 'Capacidad de espectadores';
COMMENT ON COLUMN venues.surface IS 'Tipo de superficie (grass, artificial, etc.)';
COMMENT ON COLUMN venues.image IS 'URL de la imagen del estadio';
COMMENT ON COLUMN venues.created_at IS 'Fecha y hora de creación del registro';
COMMENT ON COLUMN venues.updated_at IS 'Fecha y hora de última actualización';


-- Tabla: teams
-- Descripción: Almacena información de equipos obtenidos de API-Football
-- Autor: ElProducto Team
-- Fecha: 2025-12-18

CREATE TABLE IF NOT EXISTS teams (
    id BIGSERIAL PRIMARY KEY,
    api_football_id BIGINT NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    code VARCHAR(10),
    country VARCHAR(255) NOT NULL,
    founded INTEGER,
    national BOOLEAN NOT NULL DEFAULT false,
    logo VARCHAR(500),
    venue_id BIGINT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Foreign key a la tabla venues
    CONSTRAINT fk_teams_venue FOREIGN KEY (venue_id)
        REFERENCES venues(id) ON DELETE SET NULL ON UPDATE CASCADE
);

-- Índices para mejorar el rendimiento de búsquedas
CREATE INDEX idx_teams_api_football_id ON teams(api_football_id);
CREATE INDEX idx_teams_name ON teams(name);
CREATE INDEX idx_teams_country ON teams(country);
CREATE INDEX idx_teams_code ON teams(code);
CREATE INDEX idx_teams_national ON teams(national) WHERE national = true;
CREATE INDEX idx_teams_venue_id ON teams(venue_id);

-- Comentarios en las columnas
COMMENT ON TABLE teams IS 'Catálogo de equipos de fútbol disponibles en API-Football';
COMMENT ON COLUMN teams.id IS 'Identificador único autogenerado';
COMMENT ON COLUMN teams.api_football_id IS 'ID del equipo en API-Football (usado para consultas a la API)';
COMMENT ON COLUMN teams.name IS 'Nombre del equipo';
COMMENT ON COLUMN teams.code IS 'Código del equipo (ej: ARG, BOC, RIV)';
COMMENT ON COLUMN teams.country IS 'País al que pertenece el equipo';
COMMENT ON COLUMN teams.founded IS 'Año de fundación del equipo';
COMMENT ON COLUMN teams.national IS 'Indica si es una selección nacional';
COMMENT ON COLUMN teams.logo IS 'URL del logo del equipo';
COMMENT ON COLUMN teams.venue_id IS 'ID del estadio del equipo (puede ser NULL)';
COMMENT ON COLUMN teams.created_at IS 'Fecha y hora de creación del registro';
COMMENT ON COLUMN teams.updated_at IS 'Fecha y hora de última actualización';