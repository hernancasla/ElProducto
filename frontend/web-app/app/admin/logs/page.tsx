"use client";

import { useState } from "react";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { RefreshCw, AlertCircle, Info, AlertTriangle } from "lucide-react";

interface LogEntry {
  id: string;
  timestamp: string;
  level: "INFO" | "WARN" | "ERROR";
  message: string;
  service: string;
}

export default function LogsPage() {
  const [loading, setLoading] = useState(false);
  const [logs, setLogs] = useState<LogEntry[]>([
    {
      id: "1",
      timestamp: "2024-12-16T10:30:00Z",
      level: "INFO",
      message: "Data collector service started successfully",
      service: "data-collector",
    },
    {
      id: "2",
      timestamp: "2024-12-16T10:31:00Z",
      level: "INFO",
      message: "Fetched 150 matches from API-Football",
      service: "data-collector",
    },
    {
      id: "3",
      timestamp: "2024-12-16T10:32:00Z",
      level: "WARN",
      message: "Rate limit approaching for API-Football (90/100 requests)",
      service: "data-collector",
    },
    {
      id: "4",
      timestamp: "2024-12-16T10:33:00Z",
      level: "ERROR",
      message: "Failed to connect to Redis cache",
      service: "api-service",
    },
    {
      id: "5",
      timestamp: "2024-12-16T10:34:00Z",
      level: "INFO",
      message: "Database migration V3 completed successfully",
      service: "api-service",
    },
  ]);
  const [filter, setFilter] = useState<"ALL" | "INFO" | "WARN" | "ERROR">("ALL");

  const handleRefresh = async () => {
    setLoading(true);
    try {
      const response = await fetch("/api/admin/logs");
      if (response.ok) {
        const data = await response.json();
        setLogs(data.logs || []);
      }
    } catch (error) {
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  const filteredLogs = filter === "ALL" ? logs : logs.filter((log) => log.level === filter);

  const getLevelIcon = (level: string) => {
    switch (level) {
      case "INFO":
        return <Info className="h-4 w-4 text-blue-600" />;
      case "WARN":
        return <AlertTriangle className="h-4 w-4 text-yellow-600" />;
      case "ERROR":
        return <AlertCircle className="h-4 w-4 text-red-600" />;
      default:
        return null;
    }
  };

  const getLevelVariant = (level: string) => {
    switch (level) {
      case "INFO":
        return "default";
      case "WARN":
        return "secondary";
      case "ERROR":
        return "destructive";
      default:
        return "default";
    }
  };

  const errorCount = logs.filter((l) => l.level === "ERROR").length;
  const warnCount = logs.filter((l) => l.level === "WARN").length;

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold tracking-tight">Logs del Sistema</h1>
          <p className="text-gray-500">Eventos y errores del backend</p>
        </div>
        <Button onClick={handleRefresh} disabled={loading}>
          <RefreshCw className={`mr-2 h-4 w-4 ${loading ? "animate-spin" : ""}`} />
          Actualizar
        </Button>
      </div>

      <div className="grid gap-4 md:grid-cols-4">
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Total</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{logs.length}</div>
            <p className="text-xs text-muted-foreground">Últimas 24 horas</p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Info</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold text-blue-600">
              {logs.filter((l) => l.level === "INFO").length}
            </div>
            <p className="text-xs text-muted-foreground">Informativos</p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Advertencias</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold text-yellow-600">{warnCount}</div>
            <p className="text-xs text-muted-foreground">Warnings</p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Errores</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold text-red-600">{errorCount}</div>
            <p className="text-xs text-muted-foreground">Errors</p>
          </CardContent>
        </Card>
      </div>

      <Card>
        <CardHeader>
          <div className="flex items-center justify-between">
            <div>
              <CardTitle>Registro de Eventos</CardTitle>
              <CardDescription>Logs en tiempo real del sistema</CardDescription>
            </div>
            <div className="flex gap-2">
              <Button
                size="sm"
                variant={filter === "ALL" ? "default" : "outline"}
                onClick={() => setFilter("ALL")}
              >
                Todos
              </Button>
              <Button
                size="sm"
                variant={filter === "INFO" ? "default" : "outline"}
                onClick={() => setFilter("INFO")}
              >
                Info
              </Button>
              <Button
                size="sm"
                variant={filter === "WARN" ? "default" : "outline"}
                onClick={() => setFilter("WARN")}
              >
                Warn
              </Button>
              <Button
                size="sm"
                variant={filter === "ERROR" ? "default" : "outline"}
                onClick={() => setFilter("ERROR")}
              >
                Error
              </Button>
            </div>
          </div>
        </CardHeader>
        <CardContent>
          <div className="space-y-2">
            {filteredLogs.map((log) => (
              <div key={log.id} className="flex items-start gap-3 rounded-lg border p-3">
                <div className="mt-0.5">{getLevelIcon(log.level)}</div>
                <div className="flex-1">
                  <div className="flex items-center gap-2">
                    <Badge variant={getLevelVariant(log.level) as any}>{log.level}</Badge>
                    <span className="text-xs text-gray-500">{log.service}</span>
                    <span className="text-xs text-gray-400">
                      {new Date(log.timestamp).toLocaleString()}
                    </span>
                  </div>
                  <p className="mt-1 text-sm">{log.message}</p>
                </div>
              </div>
            ))}
          </div>
        </CardContent>
      </Card>
    </div>
  );
}
