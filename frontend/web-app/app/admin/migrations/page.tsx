"use client";

import { useState } from "react";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { RefreshCw, CheckCircle, XCircle, Clock } from "lucide-react";

interface Migration {
  id: string;
  version: string;
  description: string;
  executed: boolean;
  executedAt?: string;
  status: "pending" | "completed" | "failed";
}

export default function MigrationsPage() {
  const [loading, setLoading] = useState(false);
  const [migrations, setMigrations] = useState<Migration[]>([
    {
      id: "1",
      version: "V1__Initial_Schema",
      description: "Crear tablas iniciales (leagues, teams, matches)",
      executed: true,
      executedAt: "2024-01-15T10:00:00Z",
      status: "completed",
    },
    {
      id: "2",
      version: "V2__Add_Statistics",
      description: "Agregar tablas de estadísticas de partidos",
      executed: true,
      executedAt: "2024-01-16T10:00:00Z",
      status: "completed",
    },
    {
      id: "3",
      version: "V3__Add_Lineups",
      description: "Agregar tablas de alineaciones",
      executed: false,
      status: "pending",
    },
  ]);

  const handleExecuteMigration = async (id: string) => {
    setLoading(true);
    try {
      const response = await fetch(`/api/admin/migrations/${id}/execute`, {
        method: "POST",
      });

      if (response.ok) {
        setMigrations((prev) =>
          prev.map((m) =>
            m.id === id
              ? { ...m, executed: true, executedAt: new Date().toISOString(), status: "completed" as const }
              : m
          )
        );
      } else {
        alert("Error al ejecutar migración");
      }
    } catch (error) {
      console.error(error);
      alert("Error de conexión");
    } finally {
      setLoading(false);
    }
  };

  const handleRefresh = async () => {
    setLoading(true);
    try {
      const response = await fetch("/api/admin/migrations");
      if (response.ok) {
        const data = await response.json();
        setMigrations(data.migrations || []);
      }
    } catch (error) {
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  const pendingCount = migrations.filter((m) => !m.executed).length;
  const completedCount = migrations.filter((m) => m.executed).length;

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold tracking-tight">Migraciones</h1>
          <p className="text-gray-500">Gestión de migraciones de base de datos</p>
        </div>
        <Button onClick={handleRefresh} disabled={loading}>
          <RefreshCw className={`mr-2 h-4 w-4 ${loading ? "animate-spin" : ""}`} />
          Actualizar
        </Button>
      </div>

      <div className="grid gap-4 md:grid-cols-3">
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Total</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{migrations.length}</div>
            <p className="text-xs text-muted-foreground">Migraciones totales</p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Completadas</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold text-green-600">{completedCount}</div>
            <p className="text-xs text-muted-foreground">Ejecutadas exitosamente</p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Pendientes</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold text-yellow-600">{pendingCount}</div>
            <p className="text-xs text-muted-foreground">Por ejecutar</p>
          </CardContent>
        </Card>
      </div>

      <Card>
        <CardHeader>
          <CardTitle>Lista de Migraciones</CardTitle>
          <CardDescription>
            Todas las migraciones disponibles y su estado de ejecución
          </CardDescription>
        </CardHeader>
        <CardContent>
          <div className="space-y-3">
            {migrations.map((migration) => (
              <div
                key={migration.id}
                className="flex items-center justify-between rounded-lg border p-4"
              >
                <div className="flex items-center gap-4">
                  <div className="flex-shrink-0">
                    {migration.status === "completed" && (
                      <CheckCircle className="h-6 w-6 text-green-600" />
                    )}
                    {migration.status === "pending" && (
                      <Clock className="h-6 w-6 text-yellow-600" />
                    )}
                    {migration.status === "failed" && (
                      <XCircle className="h-6 w-6 text-red-600" />
                    )}
                  </div>
                  <div>
                    <div className="font-medium">{migration.version}</div>
                    <div className="text-sm text-gray-500">{migration.description}</div>
                    {migration.executedAt && (
                      <div className="text-xs text-gray-400 mt-1">
                        Ejecutada: {new Date(migration.executedAt).toLocaleString()}
                      </div>
                    )}
                  </div>
                </div>
                <div className="flex items-center gap-2">
                  <Badge
                    variant={
                      migration.status === "completed"
                        ? "default"
                        : migration.status === "failed"
                        ? "destructive"
                        : "secondary"
                    }
                  >
                    {migration.status === "completed" && "Completada"}
                    {migration.status === "pending" && "Pendiente"}
                    {migration.status === "failed" && "Fallida"}
                  </Badge>
                  {!migration.executed && (
                    <Button
                      size="sm"
                      onClick={() => handleExecuteMigration(migration.id)}
                      disabled={loading}
                    >
                      Ejecutar
                    </Button>
                  )}
                </div>
              </div>
            ))}
          </div>
        </CardContent>
      </Card>

      <Card className="border-yellow-200 bg-yellow-50">
        <CardContent className="pt-6">
          <p className="text-sm text-yellow-800">
            <strong>⚠️ Precaución:</strong> Las migraciones modifican la estructura de la base de
            datos. Asegúrate de tener un backup antes de ejecutar migraciones en producción.
          </p>
        </CardContent>
      </Card>
    </div>
  );
}
