import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Database, FileText, RefreshCw, Activity } from "lucide-react";

export default function AdminDashboard() {
  return (
    <div className="space-y-8">
      <div>
        <h1 className="text-3xl font-bold tracking-tight">Dashboard</h1>
        <p className="text-gray-500">Panel de control de administración</p>
      </div>

      <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-4">
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Sistema</CardTitle>
            <Activity className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">Activo</div>
            <p className="text-xs text-muted-foreground">
              Backend conectado
            </p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Migraciones</CardTitle>
            <RefreshCw className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">-</div>
            <p className="text-xs text-muted-foreground">
              Pendientes
            </p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Base de Datos</CardTitle>
            <Database className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">PostgreSQL</div>
            <p className="text-xs text-muted-foreground">
              Conectada
            </p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Logs</CardTitle>
            <FileText className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">-</div>
            <p className="text-xs text-muted-foreground">
              Últimas 24h
            </p>
          </CardContent>
        </Card>
      </div>

      <div className="grid gap-4 md:grid-cols-2">
        <Card>
          <CardHeader>
            <CardTitle>Acciones Rápidas</CardTitle>
            <CardDescription>
              Operaciones comunes de administración
            </CardDescription>
          </CardHeader>
          <CardContent className="space-y-2">
            <a
              href="/admin/migrations"
              className="block rounded-lg border p-3 hover:bg-gray-50 transition-colors"
            >
              <div className="font-medium">Gestionar Migraciones</div>
              <div className="text-sm text-gray-500">
                Ver y ejecutar migraciones de base de datos
              </div>
            </a>
            <a
              href="/admin/database"
              className="block rounded-lg border p-3 hover:bg-gray-50 transition-colors"
            >
              <div className="font-medium">Explorar Base de Datos</div>
              <div className="text-sm text-gray-500">
                Ver tablas y datos almacenados
              </div>
            </a>
            <a
              href="/admin/logs"
              className="block rounded-lg border p-3 hover:bg-gray-50 transition-colors"
            >
              <div className="font-medium">Ver Logs</div>
              <div className="text-sm text-gray-500">
                Revisar logs del sistema y errores
              </div>
            </a>
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle>Estado del Sistema</CardTitle>
            <CardDescription>
              Información sobre el estado actual
            </CardDescription>
          </CardHeader>
          <CardContent className="space-y-3">
            <div className="flex items-center justify-between">
              <span className="text-sm">Backend API</span>
              <span className="text-sm font-medium text-green-600">● Online</span>
            </div>
            <div className="flex items-center justify-between">
              <span className="text-sm">PostgreSQL</span>
              <span className="text-sm font-medium text-green-600">● Conectada</span>
            </div>
            <div className="flex items-center justify-between">
              <span className="text-sm">Redis</span>
              <span className="text-sm font-medium text-yellow-600">● Verificando...</span>
            </div>
            <div className="flex items-center justify-between">
              <span className="text-sm">Recolector de Datos</span>
              <span className="text-sm font-medium text-yellow-600">● Verificando...</span>
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  );
}
