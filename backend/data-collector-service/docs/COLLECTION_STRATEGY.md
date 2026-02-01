# Estrategia de Recoleccion de Datos

## Resumen

Este documento define la estrategia de sincronizacion para cada entidad del sistema, incluyendo frecuencias de actualizacion, dependencias y versionado.

---

## Clasificacion de Entidades

### Tipo 1: Datos Estaticos (Raramente cambian)
| Entidad | Frecuencia | Trigger | Notas |
|---------|------------|---------|-------|
| Countries | Manual / Mensual | Inicio de temporada | Solo cambian si API agrega paises |
| Leagues | Semanal | Domingo 00:00 | Cambios en temporadas activas |
| Teams | Semanal | Lunes 00:00 | Cambios en planteles, logos |

### Tipo 2: Datos Semi-Dinamicos (Cambian periodicamente)
| Entidad | Frecuencia | Trigger | Notas |
|---------|------------|---------|-------|
| Players | Semanal | Martes 00:00 | Lesiones, transferencias |
| Fixtures | Diario | 06:00 AM | Nuevos partidos, reprogramaciones |
| Standings | Post-jornada | Despues de cada fixture FT | Actualizar tras cada partido |

### Tipo 3: Datos Dinamicos (Cambian frecuentemente)
| Entidad | Frecuencia | Trigger | Notas |
|---------|------------|---------|-------|
| FixtureStatistics | Post-partido | Fixture status = FT | Solo partidos finalizados |
| FixtureEvents | Durante partido | Cada 1-2 min (live) | Polling durante partidos en vivo |
| MatchLineups | Pre-partido | 1 hora antes del partido | Cuando la API las publica |

---

## Diagrama de Dependencias

```
Countries (base)
    |
    +---> Leagues (requiere country)
              |
              +---> Teams (requiere league para filtrar)
              |         |
              |         +---> Players (requiere team)
              |
              +---> Fixtures (requiere league + season)
                        |
                        +---> Standings (requiere league + season)
                        |
                        +---> FixtureStatistics (requiere fixture)
                        |
                        +---> FixtureEvents (requiere fixture)
                        |
                        +---> MatchLineups (requiere fixture)
```

---

## Estrategia de Sincronizacion por Entidad

### 1. Countries
```yaml
entity: countries
frequency: MANUAL
cron: null  # Solo manual o al iniciar temporada
priority: 1
dependencies: []
strategy: FULL_REFRESH
retention: PERMANENT
estimated_records: ~200
api_calls_per_sync: 1
```

### 2. Leagues
```yaml
entity: leagues
frequency: WEEKLY
cron: "0 0 0 * * SUN"  # Domingos a medianoche
priority: 2
dependencies: [countries]
strategy: UPSERT
retention: PERMANENT
parameters:
  - country_code: AR  # Filtrar por Argentina para MVP
estimated_records: ~20
api_calls_per_sync: 1
```

### 3. Teams
```yaml
entity: teams
frequency: WEEKLY
cron: "0 0 0 * * MON"  # Lunes a medianoche
priority: 3
dependencies: [leagues]
strategy: UPSERT
retention: PERMANENT
parameters:
  - country: Argentina
estimated_records: ~300
api_calls_per_sync: 1
```

### 4. Players
```yaml
entity: players
frequency: WEEKLY
cron: "0 0 0 * * TUE"  # Martes a medianoche
priority: 4
dependencies: [teams]
strategy: UPSERT
retention: BY_SEASON
parameters:
  - team_id: <from teams>
  - season: 2024
estimated_records: ~25 per team
api_calls_per_sync: N (one per team)
batch_size: 10  # Equipos por batch
rate_limit_delay: 1000ms
```

### 5. Fixtures
```yaml
entity: fixtures
frequency: DAILY
cron: "0 0 6 * * *"  # 6 AM todos los dias
priority: 5
dependencies: [leagues]
strategy: UPSERT
retention: BY_SEASON
parameters:
  - league_id: 128  # Liga Profesional Argentina
  - season: 2024
estimated_records: ~400 per league/season
api_calls_per_sync: 1 per league
```

### 6. Standings
```yaml
entity: standings
frequency: EVENT_DRIVEN
cron: "0 0 * * * *"  # Cada hora (fallback)
priority: 6
dependencies: [fixtures]
strategy: FULL_REFRESH_BY_LEAGUE
trigger: FIXTURE_COMPLETED
parameters:
  - league_id: <from leagues>
  - season: 2024
estimated_records: ~28 per league
api_calls_per_sync: 1 per league
```

### 7. FixtureStatistics
```yaml
entity: fixture_statistics
frequency: EVENT_DRIVEN
cron: null
priority: 7
dependencies: [fixtures]
strategy: UPSERT_BY_FIXTURE
trigger: FIXTURE_COMPLETED  # Solo cuando status = FT
parameters:
  - fixture_id: <from fixtures where status = FT and not synced>
estimated_records: 2 per fixture
api_calls_per_sync: 1 per fixture
```

### 8. FixtureEvents
```yaml
entity: fixture_events
frequency: EVENT_DRIVEN
cron: null
priority: 8
dependencies: [fixtures]
strategy: FULL_REFRESH_BY_FIXTURE
triggers:
  - FIXTURE_LIVE: every 60 seconds
  - FIXTURE_COMPLETED: once
parameters:
  - fixture_id: <from fixtures where status IN (1H, HT, 2H, ET, P, FT)>
estimated_records: ~20 per fixture
api_calls_per_sync: 1 per fixture
```

### 9. MatchLineups
```yaml
entity: match_lineups
frequency: EVENT_DRIVEN
cron: null
priority: 9
dependencies: [fixtures]
strategy: FULL_REFRESH_BY_FIXTURE
trigger: FIXTURE_LINEUP_AVAILABLE  # ~1 hora antes del partido
parameters:
  - fixture_id: <from fixtures where date within next 2 hours>
estimated_records: ~36 per fixture (18 per team)
api_calls_per_sync: 1 per fixture
```

---

## Tabla de Metadatos de Sincronizacion

### Estructura: `sync_metadata`
```sql
CREATE TABLE sync_metadata (
    id BIGSERIAL PRIMARY KEY,
    entity_type VARCHAR(50) NOT NULL,        -- 'countries', 'leagues', etc.
    sync_scope VARCHAR(100),                  -- null, 'league:128', 'fixture:123456'
    sync_status VARCHAR(20) NOT NULL,         -- 'PENDING', 'RUNNING', 'SUCCESS', 'FAILED'
    sync_strategy VARCHAR(30) NOT NULL,       -- 'FULL_REFRESH', 'UPSERT', 'INCREMENTAL'
    started_at TIMESTAMP,
    completed_at TIMESTAMP,
    records_processed INTEGER DEFAULT 0,
    records_created INTEGER DEFAULT 0,
    records_updated INTEGER DEFAULT 0,
    records_deleted INTEGER DEFAULT 0,
    error_message TEXT,
    api_calls_made INTEGER DEFAULT 0,
    triggered_by VARCHAR(50),                 -- 'SCHEDULER', 'MANUAL', 'EVENT:fixture_completed'
    parameters JSONB,                         -- {"league_id": 128, "season": 2024}
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Indices
CREATE INDEX idx_sync_metadata_entity ON sync_metadata(entity_type);
CREATE INDEX idx_sync_metadata_status ON sync_metadata(sync_status);
CREATE INDEX idx_sync_metadata_started ON sync_metadata(started_at DESC);
CREATE UNIQUE INDEX idx_sync_metadata_running ON sync_metadata(entity_type, sync_scope)
    WHERE sync_status = 'RUNNING';  -- Evita sincronizaciones duplicadas
```

---

## Versionado por Registro

Agregar a todas las tablas existentes:
```sql
ALTER TABLE <table> ADD COLUMN updated_at TIMESTAMP;
ALTER TABLE <table> ADD COLUMN sync_version BIGINT;  -- ID del sync_metadata
```

Esto permite:
- Saber cuando fue actualizado cada registro individualmente
- Rastrear que sincronizacion actualizo cada registro
- Queries como: "mostrar registros actualizados en la ultima sincronizacion"

---

## Estados de Fixtures y Triggers

### Estados de API-Football
| Codigo | Significado | Accion |
|--------|-------------|--------|
| TBD | Por definir | Ignorar |
| NS | No iniciado | Esperar |
| 1H | Primera mitad | Sync events cada 60s |
| HT | Entretiempo | Sync events |
| 2H | Segunda mitad | Sync events cada 60s |
| ET | Tiempo extra | Sync events cada 60s |
| P | Penales | Sync events cada 30s |
| FT | Finalizado | Sync stats, events (final) |
| AET | Finalizado despues de ET | Sync stats, events (final) |
| PEN | Finalizado en penales | Sync stats, events (final) |

### Lineups
- Disponibles: ~1 hora antes del partido
- Trigger: Fixtures con `date` en proximas 2 horas y sin lineups sincronizados

---

## Orden de Inicializacion (Primera Ejecucion)

Para una primera carga completa del sistema:

```
1. Countries          (1 API call)
2. Leagues AR         (1 API call)
3. Teams AR           (1 API call)
4. Players            (N API calls, ~30 equipos = 30 calls)
5. Fixtures 2024      (1 API call per league)
6. Standings          (1 API call per league)
7. Statistics/Events  (Solo para fixtures FT existentes)
```

### Estimacion de API Calls para Inicializacion MVP
- Countries: 1
- Leagues (AR): 1
- Teams (AR): 1
- Players: ~30 (1 por equipo de primera division)
- Fixtures: ~5 (por cada liga argentina activa)
- Standings: ~5
- Stats/Events/Lineups: Variable (solo partidos finalizados recientes)

**Total estimado inicial: ~50-100 API calls**

---

## Rate Limiting

API-Football tiene limites segun el plan:
- Free: 100 calls/day
- Pro: 7,500 calls/day

### Estrategia de Rate Limiting
1. Delay entre llamadas: 200ms minimo
2. Batch processing para players: 10 equipos por batch, 1 segundo entre batches
3. Cola de prioridad: Partidos en vivo tienen maxima prioridad
4. Circuit breaker: Si hay errores 429, pausar 60 segundos

---

## Configuracion Recomendada por Entorno

### Desarrollo
```yaml
sync:
  enabled: false  # Solo manual
  rate_limit_delay: 1000ms
```

### Staging
```yaml
sync:
  enabled: true
  countries:
    cron: "0 0 0 1 * *"  # Mensual
  leagues:
    cron: "0 0 0 * * SUN"
  fixtures:
    cron: "0 0 6 * * *"
  live_polling:
    enabled: true
    interval: 120s  # Mas lento para no gastar quota
```

### Produccion
```yaml
sync:
  enabled: true
  countries:
    cron: "0 0 0 1 * *"
  leagues:
    cron: "0 0 0 * * SUN"
  teams:
    cron: "0 0 0 * * MON"
  players:
    cron: "0 0 0 * * TUE"
  fixtures:
    cron: "0 0 6 * * *"
  standings:
    cron: "0 0 * * * *"  # Cada hora
  live_polling:
    enabled: true
    interval: 60s
```
