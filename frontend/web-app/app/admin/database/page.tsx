"use client";

import { useState } from "react";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Table, Database as DatabaseIcon, Eye } from "lucide-react";

interface TableInfo {
  name: string;
  rowCount: number;
  size: string;
}

export default function DatabasePage() {
  const [tables, setTables] = useState<TableInfo[]>([
    { name: "leagues", rowCount: 150, size: "2.5 MB" },
    { name: "teams", rowCount: 450, size: "5.2 MB" },
    { name: "matches", rowCount: 12500, size: "45.8 MB" },
    { name: "match_events", rowCount: 35000, size: "28.3 MB" },
    { name: "match_statistics", rowCount: 25000, size: "18.5 MB" },
    { name: "match_lineups", rowCount: 25000, size: "15.2 MB" },
    { name: "standings", rowCount: 3000, size: "1.8 MB" },
  ]);
  const [selectedTable, setSelectedTable] = useState<string | null>(null);

  const handleViewTable = (tableName: string) => {
    setSelectedTable(tableName);
    // TODO: Cargar datos de la tabla desde API
  };

  const totalRows = tables.reduce((sum, t) => sum + t.rowCount, 0);

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold tracking-tight">Base de Datos</h1>
          <p className="text-gray-500">Exploración de tablas y datos</p>
        </div>
        <Button>
          <DatabaseIcon className="mr-2 h-4 w-4" />
          Conectar a PostgreSQL
        </Button>
      </div>

      <div className="grid gap-4 md:grid-cols-3">
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Tablas</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{tables.length}</div>
            <p className="text-xs text-muted-foreground">Tablas totales</p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Registros</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{totalRows.toLocaleString()}</div>
            <p className="text-xs text-muted-foreground">Filas totales</p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Tamaño</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">117 MB</div>
            <p className="text-xs text-muted-foreground">Espacio usado</p>
          </CardContent>
        </Card>
      </div>

      <Card>
        <CardHeader>
          <CardTitle>Tablas Disponibles</CardTitle>
          <CardDescription>
            Listado de todas las tablas en la base de datos
          </CardDescription>
        </CardHeader>
        <CardContent>
          <div className="space-y-2">
            {tables.map((table) => (
              <div
                key={table.name}
                className="flex items-center justify-between rounded-lg border p-4 hover:bg-gray-50"
              >
                <div className="flex items-center gap-3">
                  <Table className="h-5 w-5 text-gray-500" />
                  <div>
                    <div className="font-medium">{table.name}</div>
                    <div className="text-sm text-gray-500">
                      {table.rowCount.toLocaleString()} filas • {table.size}
                    </div>
                  </div>
                </div>
                <Button
                  size="sm"
                  variant="outline"
                  onClick={() => handleViewTable(table.name)}
                >
                  <Eye className="mr-2 h-4 w-4" />
                  Ver Datos
                </Button>
              </div>
            ))}
          </div>
        </CardContent>
      </Card>

      {selectedTable && (
        <Card>
          <CardHeader>
            <CardTitle>Vista de Tabla: {selectedTable}</CardTitle>
            <CardDescription>Primeras 50 filas de la tabla</CardDescription>
          </CardHeader>
          <CardContent>
            <div className="rounded-lg border p-4 text-center text-sm text-gray-500">
              Funcionalidad en desarrollo. Conectar con API del backend para obtener datos.
              <br />
              <code className="mt-2 inline-block rounded bg-gray-100 px-2 py-1">
                GET /api/admin/database/tables/{selectedTable}
              </code>
            </div>
          </CardContent>
        </Card>
      )}

      <Card className="border-blue-200 bg-blue-50">
        <CardContent className="pt-6">
          <p className="text-sm text-blue-800">
            <strong>💡 Tip:</strong> Para acceso completo a PostgreSQL, puedes usar herramientas
            como pgAdmin, DBeaver o Adminer. El backoffice solo muestra vistas básicas por
            seguridad.
          </p>
        </CardContent>
      </Card>
    </div>
  );
}
