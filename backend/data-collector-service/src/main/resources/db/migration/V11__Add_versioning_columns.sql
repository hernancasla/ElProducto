-- Migracion: Agregar columnas de versionado a todas las tablas
-- Descripcion: Agrega updated_at y sync_version para rastrear actualizaciones
-- Autor: ElProducto Team

-- =====================================================
-- COUNTRIES
-- =====================================================
ALTER TABLE countries ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP;
ALTER TABLE countries ADD COLUMN IF NOT EXISTS sync_version BIGINT;

UPDATE countries SET updated_at = created_at WHERE updated_at IS NULL;

CREATE INDEX IF NOT EXISTS idx_countries_updated_at ON countries(updated_at DESC);
CREATE INDEX IF NOT EXISTS idx_countries_sync_version ON countries(sync_version);

COMMENT ON COLUMN countries.updated_at IS 'Fecha y hora de ultima actualizacion del registro';
COMMENT ON COLUMN countries.sync_version IS 'ID del sync_metadata que actualizo este registro';

-- =====================================================
-- LEAGUES
-- =====================================================
ALTER TABLE leagues ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP;
ALTER TABLE leagues ADD COLUMN IF NOT EXISTS sync_version BIGINT;

UPDATE leagues SET updated_at = created_at WHERE updated_at IS NULL;

CREATE INDEX IF NOT EXISTS idx_leagues_updated_at ON leagues(updated_at DESC);
CREATE INDEX IF NOT EXISTS idx_leagues_sync_version ON leagues(sync_version);

COMMENT ON COLUMN leagues.updated_at IS 'Fecha y hora de ultima actualizacion del registro';
COMMENT ON COLUMN leagues.sync_version IS 'ID del sync_metadata que actualizo este registro';

-- =====================================================
-- SEASONS
-- =====================================================
ALTER TABLE seasons ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP;
ALTER TABLE seasons ADD COLUMN IF NOT EXISTS sync_version BIGINT;

UPDATE seasons SET updated_at = created_at WHERE updated_at IS NULL;

CREATE INDEX IF NOT EXISTS idx_seasons_updated_at ON seasons(updated_at DESC);
CREATE INDEX IF NOT EXISTS idx_seasons_sync_version ON seasons(sync_version);

-- =====================================================
-- SEASON_COVERAGE
-- =====================================================
ALTER TABLE season_coverage ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP;
ALTER TABLE season_coverage ADD COLUMN IF NOT EXISTS sync_version BIGINT;

UPDATE season_coverage SET updated_at = created_at WHERE updated_at IS NULL;

-- =====================================================
-- TEAMS
-- =====================================================
ALTER TABLE teams ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP;
ALTER TABLE teams ADD COLUMN IF NOT EXISTS sync_version BIGINT;

UPDATE teams SET updated_at = created_at WHERE updated_at IS NULL;

CREATE INDEX IF NOT EXISTS idx_teams_updated_at ON teams(updated_at DESC);
CREATE INDEX IF NOT EXISTS idx_teams_sync_version ON teams(sync_version);

COMMENT ON COLUMN teams.updated_at IS 'Fecha y hora de ultima actualizacion del registro';
COMMENT ON COLUMN teams.sync_version IS 'ID del sync_metadata que actualizo este registro';

-- =====================================================
-- VENUES
-- =====================================================
ALTER TABLE venues ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP;
ALTER TABLE venues ADD COLUMN IF NOT EXISTS sync_version BIGINT;

UPDATE venues SET updated_at = created_at WHERE updated_at IS NULL;

CREATE INDEX IF NOT EXISTS idx_venues_updated_at ON venues(updated_at DESC);

-- =====================================================
-- PLAYERS
-- =====================================================
ALTER TABLE players ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP;
ALTER TABLE players ADD COLUMN IF NOT EXISTS sync_version BIGINT;

UPDATE players SET updated_at = created_at WHERE updated_at IS NULL;

CREATE INDEX IF NOT EXISTS idx_players_updated_at ON players(updated_at DESC);
CREATE INDEX IF NOT EXISTS idx_players_sync_version ON players(sync_version);

COMMENT ON COLUMN players.updated_at IS 'Fecha y hora de ultima actualizacion del registro';
COMMENT ON COLUMN players.sync_version IS 'ID del sync_metadata que actualizo este registro';

-- =====================================================
-- FIXTURES
-- =====================================================
ALTER TABLE fixtures ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP;
ALTER TABLE fixtures ADD COLUMN IF NOT EXISTS sync_version BIGINT;

UPDATE fixtures SET updated_at = created_at WHERE updated_at IS NULL;

CREATE INDEX IF NOT EXISTS idx_fixtures_updated_at ON fixtures(updated_at DESC);
CREATE INDEX IF NOT EXISTS idx_fixtures_sync_version ON fixtures(sync_version);

COMMENT ON COLUMN fixtures.updated_at IS 'Fecha y hora de ultima actualizacion del registro';
COMMENT ON COLUMN fixtures.sync_version IS 'ID del sync_metadata que actualizo este registro';

-- =====================================================
-- STANDINGS
-- =====================================================
ALTER TABLE standings ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP;
ALTER TABLE standings ADD COLUMN IF NOT EXISTS sync_version BIGINT;

UPDATE standings SET updated_at = created_at WHERE updated_at IS NULL;

CREATE INDEX IF NOT EXISTS idx_standings_updated_at ON standings(updated_at DESC);
CREATE INDEX IF NOT EXISTS idx_standings_sync_version ON standings(sync_version);

COMMENT ON COLUMN standings.updated_at IS 'Fecha y hora de ultima actualizacion del registro';
COMMENT ON COLUMN standings.sync_version IS 'ID del sync_metadata que actualizo este registro';

-- =====================================================
-- FIXTURE_STATISTICS
-- =====================================================
ALTER TABLE fixture_statistics ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP;
ALTER TABLE fixture_statistics ADD COLUMN IF NOT EXISTS sync_version BIGINT;

UPDATE fixture_statistics SET updated_at = created_at WHERE updated_at IS NULL;

CREATE INDEX IF NOT EXISTS idx_fixture_statistics_updated_at ON fixture_statistics(updated_at DESC);
CREATE INDEX IF NOT EXISTS idx_fixture_statistics_sync_version ON fixture_statistics(sync_version);

-- =====================================================
-- FIXTURE_EVENTS
-- =====================================================
ALTER TABLE fixture_events ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP;
ALTER TABLE fixture_events ADD COLUMN IF NOT EXISTS sync_version BIGINT;

UPDATE fixture_events SET updated_at = created_at WHERE updated_at IS NULL;

CREATE INDEX IF NOT EXISTS idx_fixture_events_updated_at ON fixture_events(updated_at DESC);
CREATE INDEX IF NOT EXISTS idx_fixture_events_sync_version ON fixture_events(sync_version);

-- =====================================================
-- MATCH_LINEUPS
-- =====================================================
ALTER TABLE match_lineups ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP;
ALTER TABLE match_lineups ADD COLUMN IF NOT EXISTS sync_version BIGINT;

UPDATE match_lineups SET updated_at = created_at WHERE updated_at IS NULL;

CREATE INDEX IF NOT EXISTS idx_match_lineups_updated_at ON match_lineups(updated_at DESC);
CREATE INDEX IF NOT EXISTS idx_match_lineups_sync_version ON match_lineups(sync_version);

-- =====================================================
-- VISTA: Resumen de ultima sincronizacion por entidad
-- =====================================================
CREATE OR REPLACE VIEW v_last_sync_by_entity AS
SELECT DISTINCT ON (entity_type)
    entity_type,
    sync_scope,
    sync_status,
    started_at,
    completed_at,
    records_processed,
    records_created,
    records_updated,
    triggered_by,
    EXTRACT(EPOCH FROM (completed_at - started_at)) as duration_seconds
FROM sync_metadata
WHERE sync_status = 'SUCCESS'
ORDER BY entity_type, completed_at DESC;

COMMENT ON VIEW v_last_sync_by_entity IS 'Ultima sincronizacion exitosa por tipo de entidad';