-- Tabla: players
-- Descripción: Almacena información de jugadores obtenidos de API-Football
-- Autor: ElProducto Team
-- Fecha: 2025-12-19

CREATE TABLE IF NOT EXISTS players (
    id BIGSERIAL PRIMARY KEY,
    api_football_id BIGINT NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    age INTEGER,
    birth_date DATE,
    birth_place VARCHAR(255),
    birth_country VARCHAR(255),
    nationality VARCHAR(255),
    height VARCHAR(20),
    weight VARCHAR(20),
    injured BOOLEAN NOT NULL DEFAULT false,
    photo VARCHAR(500),
    team_id BIGINT,
    team_name VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Índices para mejorar el rendimiento de búsquedas
CREATE INDEX idx_players_api_football_id ON players(api_football_id);
CREATE INDEX idx_players_name ON players(name);
CREATE INDEX idx_players_nationality ON players(nationality);
CREATE INDEX idx_players_team_id ON players(team_id);
CREATE INDEX idx_players_team_name ON players(team_name);
CREATE INDEX idx_players_injured ON players(injured) WHERE injured = true;

-- Comentarios en las columnas
COMMENT ON TABLE players IS 'Catálogo de jugadores de fútbol disponibles en API-Football';
COMMENT ON COLUMN players.id IS 'Identificador único autogenerado';
COMMENT ON COLUMN players.api_football_id IS 'ID del jugador en API-Football (usado para consultas a la API)';
COMMENT ON COLUMN players.name IS 'Nombre completo del jugador (formato corto, ej: L. Messi)';
COMMENT ON COLUMN players.first_name IS 'Primer nombre del jugador';
COMMENT ON COLUMN players.last_name IS 'Apellido del jugador';
COMMENT ON COLUMN players.age IS 'Edad actual del jugador';
COMMENT ON COLUMN players.birth_date IS 'Fecha de nacimiento del jugador';
COMMENT ON COLUMN players.birth_place IS 'Lugar de nacimiento del jugador';
COMMENT ON COLUMN players.birth_country IS 'País de nacimiento del jugador';
COMMENT ON COLUMN players.nationality IS 'Nacionalidad del jugador';
COMMENT ON COLUMN players.height IS 'Altura del jugador (ej: 170 cm)';
COMMENT ON COLUMN players.weight IS 'Peso del jugador (ej: 72 kg)';
COMMENT ON COLUMN players.injured IS 'Indica si el jugador está actualmente lesionado';
COMMENT ON COLUMN players.photo IS 'URL de la foto del jugador';
COMMENT ON COLUMN players.team_id IS 'ID del equipo actual en API-Football';
COMMENT ON COLUMN players.team_name IS 'Nombre del equipo actual';
COMMENT ON COLUMN players.created_at IS 'Fecha y hora de creación del registro';
COMMENT ON COLUMN players.updated_at IS 'Fecha y hora de última actualización';