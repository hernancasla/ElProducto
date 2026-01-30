-- Tabla: countries
-- Descripción: Almacena información de países obtenidos de API-Football
-- Autor: ElProducto Team
-- Fecha: 2025-12-14

CREATE TABLE IF NOT EXISTS countries (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    code VARCHAR(10) NOT NULL UNIQUE,
    flag VARCHAR(500),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Índices para mejorar el rendimiento de búsquedas
CREATE INDEX idx_countries_code ON countries(code);
CREATE INDEX idx_countries_name ON countries(name);

-- Comentarios en las columnas
COMMENT ON TABLE countries IS 'Catálogo de países disponibles en API-Football';
COMMENT ON COLUMN countries.id IS 'Identificador único autogenerado';
COMMENT ON COLUMN countries.name IS 'Nombre del país';
COMMENT ON COLUMN countries.code IS 'Código ISO del país (ej: AR, BR, ES)';
COMMENT ON COLUMN countries.flag IS 'URL de la imagen de la bandera del país';
COMMENT ON COLUMN countries.created_at IS 'Fecha y hora de creación del registro';
COMMENT ON COLUMN countries.updated_at IS 'Fecha y hora de última actualización';