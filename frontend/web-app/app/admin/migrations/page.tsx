"use client";

import { useEffect, useState, useCallback } from "react";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { RefreshCw, CheckCircle, XCircle, Clock, Play, AlertTriangle } from "lucide-react";

interface Migration {
  installedRank: number | null;
  version: string | null;
  description: string;
  type: string;
  script: string;
  state: string; // SUCCESS | PENDING | FAILED | MISSING_SUCCESS | BASELINE
  installedOn: string | null;
  executionTime: number | null;
}

interface RunResult {
  migrationsExecuted: number;
  initialSchemaVersion: string;
  targetSchemaVersion: string;
  success: boolean;
}

function stateVariant(state: string): "default" | "secondary" | "destructive" | "outline" {
  switch (state) {
    case "SUCCESS":
    case "MISSING_SUCCESS":
    case "BASELINE":
      return "default";
    case "PENDING":
    case "ABOVE_TARGET":
    case "IGNORED":
      return "secondary";
    case "FAILED":
    case "FUTURE_FAILED":
    case "MISSING_FAILED":
      return "destructive";
    default:
      return "outline";
  }
}

function stateLabel(state: string): string {
  switch (state) {
    case "SUCCESS": return "Aplicada";
    case "PENDING": return "Pendiente";
    case "FAILED": return "Fallida";
    case "BASELINE": return "Baseline";
    case "MISSING_SUCCESS": return "Aplicada (sin registro)";
    default: return state;
  }
}

function StateIcon({ state }: { state: string }) {
  if (state === "SUCCESS" || state === "MISSING_SUCCESS" || state === "BASELINE")
    return <CheckCircle className="h-5 w-5 text-green-600" />;
  if (state === "PENDING")
    return <Clock className="h-5 w-5 text-yellow-500" />;
  if (state === "FAILED" || state === "FUTURE_FAILED" || state === "MISSING_FAILED")
    return <XCircle className="h-5 w-5 text-red-600" />;
  return <AlertTriangle className="h-5 w-5 text-gray-400" />;
}

export default function MigrationsPage() {
  const [migrations, setMigrations] = useState<Migration[]>([]);
  const [loading, setLoading] = useState(true);
  const [running, setRunning] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [runResult, setRunResult] = useState<RunResult | null>(null);

  const fetchMigrations = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const res = await fetch("/api/admin/migrations");
      if (!res.ok) {
        const body = await res.json().catch(() => ({}));
        throw new Error(body.error || `Error ${res.status}`);
      }
      const body = await res.json();
      setMigrations(body.data ?? []);
    } catch (e: unknown) {
      setError(e instanceof Error ? e.message : "Error al cargar migraciones");
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    fetchMigrations();
  }, [fetchMigrations]);

  const handleRunMigrations = async () => {
    setRunning(true);
    setRunResult(null);
    setError(null);
    try {
      const res = await fetch("/api/admin/migrations/run", { method: "POST" });
      const body = await res.json();
      if (!res.ok) throw new Error(body.error || `Error ${res.status}`);
      setRunResult(body.data);
      await fetchMigrations(); // refresh list after running
    } catch (e: unknown) {
      setError(e instanceof Error ? e.message : "Error al ejecutar migraciones");
    } finally {
      setRunning(false);
    }
  };

  const pendingCount = migrations.filter((m) => m.state === "PENDING").length;
  const appliedCount = migrations.filter(
    (m) => m.state === "SUCCESS" || m.state === "BASELINE" || m.state === "MISSING_SUCCESS"
  ).length;
  const failedCount = migrations.filter(
    (m) => m.state === "FAILED" || m.state === "FUTURE_FAILED"
  ).length;

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold tracking-tight">Migraciones</h1>
          <p className="text-gray-500">Estado de migraciones Flyway en la base de datos</p>
        </div>
        <div className="flex gap-2">
          <Button variant="outline" onClick={fetchMigrations} disabled={loading || running}>
            <RefreshCw className={`mr-2 h-4 w-4 ${loading ? "animate-spin" : ""}`} />
            Actualizar
          </Button>
          {pendingCount > 0 && (
            <Button onClick={handleRunMigrations} disabled={running || loading}>
              <Play className={`mr-2 h-4 w-4 ${running ? "animate-pulse" : ""}`} />
              {running ? "Ejecutando..." : `Ejecutar ${pendingCount} pendiente${pendingCount > 1 ? "s" : ""}`}
            </Button>
          )}
        </div>
      </div>

      {/* Result banner */}
      {runResult && (
        <Card className={runResult.success ? "border-green-200 bg-green-50" : "border-red-200 bg-red-50"}>
          <CardContent className="pt-4">
            {runResult.migrationsExecuted === 0 ? (
              <p className="text-sm text-gray-600">No había migraciones pendientes.</p>
            ) : (
              <p className="text-sm text-green-800">
                <strong>{runResult.migrationsExecuted}</strong> migración
                {runResult.migrationsExecuted > 1 ? "es ejecutadas" : " ejecutada"} correctamente.
                Schema: <code className="text-xs">{runResult.initialSchemaVersion}</code>
                {" → "}
                <code className="text-xs">{runResult.targetSchemaVersion}</code>
              </p>
            )}
          </CardContent>
        </Card>
      )}

      {/* Error banner */}
      {error && (
        <Card className="border-red-200 bg-red-50">
          <CardContent className="pt-4">
            <p className="text-sm text-red-800">
              <strong>Error:</strong> {error}
            </p>
          </CardContent>
        </Card>
      )}

      {/* Stats */}
      <div className="grid gap-4 md:grid-cols-3">
        <Card>
          <CardHeader className="pb-2">
            <CardTitle className="text-sm font-medium text-gray-500">Aplicadas</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold text-green-600">{appliedCount}</div>
          </CardContent>
        </Card>
        <Card>
          <CardHeader className="pb-2">
            <CardTitle className="text-sm font-medium text-gray-500">Pendientes</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold text-yellow-600">{pendingCount}</div>
          </CardContent>
        </Card>
        <Card>
          <CardHeader className="pb-2">
            <CardTitle className="text-sm font-medium text-gray-500">Fallidas</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold text-red-600">{failedCount}</div>
          </CardContent>
        </Card>
      </div>

      {/* Migration list */}
      <Card>
        <CardHeader>
          <CardTitle>Historial de Migraciones</CardTitle>
          <CardDescription>
            Estado actual según la tabla <code>flyway_schema_history</code>
          </CardDescription>
        </CardHeader>
        <CardContent>
          {loading ? (
            <div className="space-y-3">
              {[1, 2, 3].map((i) => (
                <div key={i} className="h-16 rounded-lg border bg-gray-50 animate-pulse" />
              ))}
            </div>
          ) : migrations.length === 0 ? (
            <p className="text-sm text-gray-500 py-4 text-center">
              No se encontraron migraciones. Verificá que la API esté activa.
            </p>
          ) : (
            <div className="space-y-3">
              {migrations.map((m, index) => (
                <div
                  key={m.script ?? index}
                  className="flex items-center justify-between rounded-lg border p-4"
                >
                  <div className="flex items-center gap-4">
                    <StateIcon state={m.state} />
                    <div>
                      <div className="font-medium text-sm">
                        {m.version ? `V${m.version}` : m.type} — {m.description}
                      </div>
                      <div className="text-xs text-gray-400 mt-0.5">{m.script}</div>
                      {m.installedOn && (
                        <div className="text-xs text-gray-400">
                          {new Date(m.installedOn).toLocaleString("es-AR")}
                          {m.executionTime != null && ` · ${m.executionTime}ms`}
                        </div>
                      )}
                    </div>
                  </div>
                  <Badge variant={stateVariant(m.state)}>{stateLabel(m.state)}</Badge>
                </div>
              ))}
            </div>
          )}
        </CardContent>
      </Card>

      {/* Warning */}
      <Card className="border-yellow-200 bg-yellow-50">
        <CardContent className="pt-4">
          <p className="text-sm text-yellow-800">
            <strong>Precaución:</strong> Las migraciones modifican la estructura de la base de datos.
            Asegurate de tener un backup antes de ejecutar en producción.
          </p>
        </CardContent>
      </Card>
    </div>
  );
}
