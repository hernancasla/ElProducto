import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";

export default function SettingsPage() {
  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-3xl font-bold tracking-tight">Configuración</h1>
        <p className="text-gray-500">Ajustes del backoffice</p>
      </div>

      <Card>
        <CardHeader>
          <CardTitle>Seguridad</CardTitle>
          <CardDescription>Configuración de acceso y autenticación</CardDescription>
        </CardHeader>
        <CardContent className="space-y-4">
          <div>
            <h3 className="font-medium">IP Whitelist</h3>
            <p className="text-sm text-gray-500 mb-2">
              IPs permitidas para acceder al backoffice
            </p>
            <code className="block rounded-lg bg-gray-100 p-3 text-sm">
              ADMIN_IP_WHITELIST=127.0.0.1,192.168.1.100
            </code>
          </div>

          <div>
            <h3 className="font-medium">Credenciales de Admin</h3>
            <p className="text-sm text-gray-500 mb-2">Variables de entorno requeridas</p>
            <code className="block rounded-lg bg-gray-100 p-3 text-sm font-mono">
              ADMIN_USERNAME=admin<br />
              ADMIN_PASSWORD_HASH=generado_con_bcrypt<br />
              SESSION_SECRET=tu_secret_key_32_chars
            </code>
          </div>
        </CardContent>
      </Card>

      <Card>
        <CardHeader>
          <CardTitle>Backend API</CardTitle>
          <CardDescription>Configuración de conexión al backend</CardDescription>
        </CardHeader>
        <CardContent>
          <div>
            <h3 className="font-medium">Endpoint</h3>
            <p className="text-sm text-gray-500 mb-2">URL del backend Spring Boot</p>
            <code className="block rounded-lg bg-gray-100 p-3 text-sm">
              NEXT_PUBLIC_API_URL=http://localhost:8080/api/v1
            </code>
          </div>
        </CardContent>
      </Card>
    </div>
  );
}
