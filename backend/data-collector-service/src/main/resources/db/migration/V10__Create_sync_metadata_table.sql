-- Tabla: sync_metadata
-- Descripcion: Almacena metadatos de cada sincronizacion ejecutada
-- Permite rastrear el estado, progreso y resultados de cada sync
-- Autor: ElProducto Team

CREATE TABLE IF NOT EXISTS sync_metadata (
    id BIGSERIAL PRIMARY KEY,

    -- Identificacion de la sincronizacion
    entity_type VARCHAR(50) NOT NULL,           -- 'countries', 'leagues', 'fixtures', etc.
    sync_scope VARCHAR(100),                     -- null (global), 'league:128', 'fixture:123456'

    -- Estado y estrategia
    sync_status VARCHAR(20) NOT NULL DEFAULT 'PENDING',  -- PENDING, RUNNING, SUCCESS, FAILED, CANCELLED
    sync_strategy VARCHAR(30) NOT NULL,          -- FULL_REFRESH, UPSERT, INCREMENTAL

    -- Timestamps
    started_at TIMESTAMP,
    completed_at TIMESTAMP,

    -- Metricas
    records_processed INTEGER DEFAULT 0,
    records_created INTEGER DEFAULT 0,
    records_updated INTEGER DEFAULT 0,
    records_deleted INTEGER DEFAULT 0,
    records_skipped INTEGER DEFAULT 0,

    -- API metrics
    api_calls_made INTEGER DEFAULT 0,
    api_errors INTEGER DEFAULT 0,

    -- Error handling
    error_message TEXT,
    error_details JSONB,
    retry_count INTEGER DEFAULT 0,

    -- Contexto
    triggered_by VARCHAR(50) NOT NULL,           -- SCHEDULER, MANUAL, EVENT, DEPENDENCY
    trigger_event VARCHAR(100),                  -- 'fixture_completed:123456', 'cron:daily'
    parameters JSONB,                            -- {"league_id": 128, "season": 2024}

    -- Auditoria
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Constraints
    CONSTRAINT chk_sync_status CHECK (sync_status IN ('PENDING', 'RUNNING', 'SUCCESS', 'FAILED', 'CANCELLED')),
    CONSTRAINT chk_sync_strategy CHECK (sync_strategy IN ('FULL_REFRESH', 'UPSERT', 'INCREMENTAL', 'DELETE_INSERT')),
    CONSTRAINT chk_triggered_by CHECK (triggered_by IN ('SCHEDULER', 'MANUAL', 'EVENT', 'DEPENDENCY', 'INITIALIZATION'))
);

-- Indices para consultas frecuentes
CREATE INDEX idx_sync_metadata_entity_type ON sync_metadata(entity_type);
CREATE INDEX idx_sync_metadata_status ON sync_metadata(sync_status);
CREATE INDEX idx_sync_metadata_started_at ON sync_metadata(started_at DESC);
CREATE INDEX idx_sync_metadata_entity_status ON sync_metadata(entity_type, sync_status);

-- Indice parcial para evitar sincronizaciones duplicadas en ejecucion
CREATE UNIQUE INDEX idx_sync_metadata_running_unique
    ON sync_metadata(entity_type, COALESCE(sync_scope, ''))
    WHERE sync_status = 'RUNNING';

-- Indice para buscar ultima sincronizacion exitosa por entidad
CREATE INDEX idx_sync_metadata_last_success
    ON sync_metadata(entity_type, sync_scope, completed_at DESC)
    WHERE sync_status = 'SUCCESS';

-- Comentarios
COMMENT ON TABLE sync_metadata IS 'Registro de todas las sincronizaciones ejecutadas en el sistema';
COMMENT ON COLUMN sync_metadata.entity_type IS 'Tipo de entidad sincronizada (countries, leagues, fixtures, etc.)';
COMMENT ON COLUMN sync_metadata.sync_scope IS 'Alcance de la sincronizacion (null=global, league:128, fixture:123456)';
COMMENT ON COLUMN sync_metadata.sync_status IS 'Estado actual: PENDING, RUNNING, SUCCESS, FAILED, CANCELLED';
COMMENT ON COLUMN sync_metadata.sync_strategy IS 'Estrategia usada: FULL_REFRESH, UPSERT, INCREMENTAL, DELETE_INSERT';
COMMENT ON COLUMN sync_metadata.triggered_by IS 'Origen del trigger: SCHEDULER, MANUAL, EVENT, DEPENDENCY, INITIALIZATION';
COMMENT ON COLUMN sync_metadata.trigger_event IS 'Evento especifico que disparo la sync';
COMMENT ON COLUMN sync_metadata.parameters IS 'Parametros JSON usados en la sincronizacion';