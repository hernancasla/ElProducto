"use client";

import { useEffect, useState, useCallback } from "react";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import {
  CheckCircle,
  XCircle,
  Loader2,
  RefreshCw,
  Play,
  Wifi,
  WifiOff,
} from "lucide-react";

// ─────────────────────────────────────────────
// Types
// ─────────────────────────────────────────────

interface CollectionResult {
  type: string;
  status: string;
  message: string;
  count?: number;
}

interface RunningSync {
  id: number;
  entityType: string;
  syncStatus: string;
  startedAt: string;
  recordsProcessed: number;
  triggeredBy: string;
}

interface CollectionConfig {
  label: string;
  type: string;
  description: string;
  params: ParamConfig[];
}

interface ParamConfig {
  key: string;
  label: string;
  placeholder: string;
  defaultValue: string;
}

// ─────────────────────────────────────────────
// Collection definitions
// ─────────────────────────────────────────────

const COLLECTIONS: CollectionConfig[] = [
  {
    label: "Países",
    type: "countries",
    description: "Recolecta todos los países disponibles en API-Football",
    params: [],
  },
  {
    label: "Ligas",
    type: "leagues",
    description: "Recolecta ligas para un país específico",
    params: [
      { key: "countryCode", label: "Código de país", placeholder: "AR", defaultValue: "AR" },
    ],
  },
  {
    label: "Equipos",
    type: "teams",
    description: "Recolecta equipos para un país específico",
    params: [
      { key: "country", label: "País", placeholder: "Argentina", defaultValue: "Argentina" },
    ],
  },
  {
    label: "Partidos (Fixtures)",
    type: "fixtures",
    description: "Recolecta partidos de una liga y temporada",
    params: [
      { key: "leagueId", label: "ID de Liga", placeholder: "128", defaultValue: "128" },
      { key: "season", label: "Temporada", placeholder: "2024", defaultValue: "2024" },
    ],
  },
  {
    label: "Posiciones (Standings)",
    type: "standings",
    description: "Recolecta tabla de posiciones de una liga y temporada",
    params: [
      { key: "leagueId", label: "ID de Liga", placeholder: "128", defaultValue: "128" },
      { key: "season", label: "Temporada", placeholder: "2024", defaultValue: "2024" },
    ],
  },
  {
    label: "Jugadores",
    type: "players",
    description: "Recolecta jugadores de un equipo y temporada",
    params: [
      { key: "teamId", label: "ID de Equipo", placeholder: "26", defaultValue: "26" },
      { key: "season", label: "Temporada", placeholder: "2024", defaultValue: "2024" },
    ],
  },
];

// ─────────────────────────────────────────────
// Component
// ─────────────────────────────────────────────

export default function CollectorPage() {
  const [collectorOnline, setCollectorOnline] = useState<boolean | null>(null);
  const [statusLoading, setStatusLoading] = useState(true);

  // Running syncs
  const [runningSyncs, setRunningSyncs] = useState<RunningSync[]>([]);
  const [syncsLoading, setSyncsLoading] = useState(false);

  // Per-collection state: paramValues and running flag
  const [paramValues, setParamValues] = useState<Record<string, Record<string, string>>>(() => {
    const defaults: Record<string, Record<string, string>> = {};
    COLLECTIONS.forEach((c) => {
      defaults[c.type] = {};
      c.params.forEach((p) => {
        defaults[c.type][p.key] = p.defaultValue;
      });
    });
    return defaults;
  });
  const [running, setRunning] = useState<Record<string, boolean>>({});
  const [results, setResults] = useState<Record<string, CollectionResult | null>>({});
  const [errors, setErrors] = useState<Record<string, string | null>>({});

  // ─────────────────────────────────────────────
  // Fetch collector status
  // ─────────────────────────────────────────────

  const fetchStatus = useCallback(async () => {
    setStatusLoading(true);
    try {
      const res = await fetch("/api/admin/collector/status");
      if (!res.ok) throw new Error(`HTTP ${res.status}`);
      const body = await res.json();
      setCollectorOnline(body.data?.online ?? false);
    } catch {
      setCollectorOnline(false);
    } finally {
      setStatusLoading(false);
    }
  }, []);

  // ─────────────────────────────────────────────
  // Fetch running syncs
  // ─────────────────────────────────────────────

  const fetchRunningSyncs = useCallback(async () => {
    setSyncsLoading(true);
    try {
      const res = await fetch("/api/admin/collector/sync/running");
      if (!res.ok) return;
      const body = await res.json();
      // Collector returns { status, message, syncs: [...] } inside data
      const syncs = body.data?.syncs ?? [];
      setRunningSyncs(syncs);
    } catch {
      // silently ignore
    } finally {
      setSyncsLoading(false);
    }
  }, []);

  useEffect(() => {
    fetchStatus();
    fetchRunningSyncs();
  }, [fetchStatus, fetchRunningSyncs]);

  // ─────────────────────────────────────────────
  // Run a collection
  // ─────────────────────────────────────────────

  const handleCollect = async (config: CollectionConfig) => {
    const type = config.type;
    setRunning((prev) => ({ ...prev, [type]: true }));
    setResults((prev) => ({ ...prev, [type]: null }));
    setErrors((prev) => ({ ...prev, [type]: null }));

    try {
      const params = new URLSearchParams({ type });
      Object.entries(paramValues[type] || {}).forEach(([k, v]) => {
        if (v) params.set(k, v);
      });

      const res = await fetch(`/api/admin/collector/collect?${params.toString()}`, {
        method: "POST",
      });
      const body = await res.json();

      if (!res.ok) {
        throw new Error(body.error || body.errors?.[0] || `Error ${res.status}`);
      }

      // The collector response is nested inside ApiResponse.data
      const inner = body.data ?? body;
      setResults((prev) => ({
        ...prev,
        [type]: {
          type,
          status: inner.status ?? (res.ok ? "SUCCESS" : "ERROR"),
          message: inner.message ?? "Colección completada",
          count: inner.count,
        },
      }));

      // Refresh running syncs after triggering
      setTimeout(fetchRunningSyncs, 1000);
    } catch (e: unknown) {
      setErrors((prev) => ({
        ...prev,
        [type]: e instanceof Error ? e.message : "Error desconocido",
      }));
    } finally {
      setRunning((prev) => ({ ...prev, [type]: false }));
    }
  };

  const setParam = (type: string, key: string, value: string) => {
    setParamValues((prev) => ({
      ...prev,
      [type]: { ...prev[type], [key]: value },
    }));
  };

  // ─────────────────────────────────────────────
  // Render
  // ─────────────────────────────────────────────

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold tracking-tight">Recolección de Datos</h1>
          <p className="text-gray-500">
            Ejecutá recolecciones desde API-Football a demanda
          </p>
        </div>
        <Button
          variant="outline"
          onClick={() => {
            fetchStatus();
            fetchRunningSyncs();
          }}
          disabled={statusLoading}
        >
          <RefreshCw className={`mr-2 h-4 w-4 ${statusLoading ? "animate-spin" : ""}`} />
          Actualizar
        </Button>
      </div>

      {/* Collector Status */}
      <Card className={collectorOnline ? "border-green-200" : "border-red-200"}>
        <CardContent className="flex items-center gap-3 pt-4">
          {statusLoading ? (
            <Loader2 className="h-5 w-5 animate-spin text-gray-400" />
          ) : collectorOnline ? (
            <Wifi className="h-5 w-5 text-green-600" />
          ) : (
            <WifiOff className="h-5 w-5 text-red-600" />
          )}
          <span className={`text-sm font-medium ${collectorOnline ? "text-green-800" : "text-red-800"}`}>
            {statusLoading
              ? "Verificando..."
              : collectorOnline
              ? "Data Collector disponible"
              : "Data Collector no disponible — verificá que el servicio esté corriendo en :8081"}
          </span>
        </CardContent>
      </Card>

      {/* Running Syncs */}
      {runningSyncs.length > 0 && (
        <Card className="border-blue-200 bg-blue-50">
          <CardHeader className="pb-2">
            <CardTitle className="text-sm font-medium text-blue-800 flex items-center gap-2">
              <Loader2 className="h-4 w-4 animate-spin" />
              Sincronizaciones en ejecución ({runningSyncs.length})
            </CardTitle>
          </CardHeader>
          <CardContent className="space-y-2">
            {runningSyncs.map((s) => (
              <div key={s.id} className="flex items-center justify-between text-sm">
                <span className="font-medium">{s.entityType}</span>
                <span className="text-blue-600">{s.recordsProcessed} registros procesados</span>
                <Badge variant="secondary">{s.syncStatus}</Badge>
              </div>
            ))}
          </CardContent>
        </Card>
      )}

      {/* Collection Cards */}
      <div className="grid gap-4 md:grid-cols-2">
        {COLLECTIONS.map((config) => {
          const isRunning = running[config.type] ?? false;
          const result = results[config.type];
          const error = errors[config.type];

          return (
            <Card key={config.type}>
              <CardHeader className="pb-3">
                <CardTitle className="text-base">{config.label}</CardTitle>
                <CardDescription className="text-xs">{config.description}</CardDescription>
              </CardHeader>
              <CardContent className="space-y-3">
                {/* Param inputs */}
                {config.params.map((param) => (
                  <div key={param.key} className="space-y-1">
                    <Label htmlFor={`${config.type}-${param.key}`} className="text-xs">
                      {param.label}
                    </Label>
                    <Input
                      id={`${config.type}-${param.key}`}
                      placeholder={param.placeholder}
                      value={paramValues[config.type]?.[param.key] ?? param.defaultValue}
                      onChange={(e) => setParam(config.type, param.key, e.target.value)}
                      disabled={isRunning}
                      className="h-8 text-sm"
                    />
                  </div>
                ))}

                {/* Result */}
                {result && (
                  <div
                    className={`flex items-start gap-2 rounded-md p-2 text-xs ${
                      result.status === "SUCCESS"
                        ? "bg-green-50 text-green-800"
                        : "bg-red-50 text-red-800"
                    }`}
                  >
                    {result.status === "SUCCESS" ? (
                      <CheckCircle className="h-4 w-4 mt-0.5 shrink-0" />
                    ) : (
                      <XCircle className="h-4 w-4 mt-0.5 shrink-0" />
                    )}
                    <div>
                      <p>{result.message}</p>
                      {result.count != null && (
                        <p className="font-semibold">{result.count} registros</p>
                      )}
                    </div>
                  </div>
                )}

                {/* Error */}
                {error && (
                  <div className="flex items-start gap-2 rounded-md bg-red-50 p-2 text-xs text-red-800">
                    <XCircle className="h-4 w-4 mt-0.5 shrink-0" />
                    <p>{error}</p>
                  </div>
                )}

                {/* Action button */}
                <Button
                  size="sm"
                  onClick={() => handleCollect(config)}
                  disabled={isRunning || !collectorOnline}
                  className="w-full"
                >
                  {isRunning ? (
                    <>
                      <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                      Recolectando...
                    </>
                  ) : (
                    <>
                      <Play className="mr-2 h-4 w-4" />
                      Recolectar {config.label}
                    </>
                  )}
                </Button>
              </CardContent>
            </Card>
          );
        })}
      </div>

      {/* Warning */}
      <Card className="border-yellow-200 bg-yellow-50">
        <CardContent className="pt-4">
          <p className="text-sm text-yellow-800">
            <strong>Precaución:</strong> Cada recolección consume cuota de la API-Football.
            El plan gratuito tiene un límite de 100 llamadas diarias. Verificá el saldo antes
            de ejecutar múltiples recolecciones en producción.
          </p>
        </CardContent>
      </Card>
    </div>
  );
}
