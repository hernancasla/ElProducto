# 🚀 Guía Rápida - ElProducto Frontend

## Inicio Rápido (5 minutos)

### 1. Instalación
```bash
cd frontend/web-app
npm install
```

### 2. Configuración
```bash
# Copiar variables de entorno
cp .env.example .env.local

# El .env.local ya está configurado para desarrollo local
# Backend debe estar en http://localhost:8080
```

### 3. Iniciar Desarrollo
```bash
npm run dev
```

Abre [http://localhost:3000](http://localhost:3000)

## 📋 Comandos Principales

```bash
npm run dev      # Desarrollo
npm run build    # Build producción
npm run start    # Iniciar producción
npm run lint     # Linting
```

## 🎯 Estructura Básica

```
app/           → Páginas (App Router)
components/    → Componentes React
lib/           → Lógica y APIs
  ├── api/     → Clientes HTTP
  ├── hooks/   → Custom hooks
  ├── store/   → Zustand stores
  └── realtime/→ SSE/WebSocket
types/         → Tipos TypeScript
```

## 💡 Ejemplos de Código

### Obtener Partidos en Vivo
```typescript
import { useLiveMatches } from "@/lib/hooks/useMatches";

function LiveMatches() {
  const { data, isLoading } = useLiveMatches();
  return <div>{/* Renderizar partidos */}</div>;
}
```

### Usar Favoritos
```typescript
import { useFavoritesStore } from "@/lib/store";

const { addFavoriteTeam, isFavoriteTeam } = useFavoritesStore();
```

### SSE en Tiempo Real
```typescript
import { createSSEClient } from "@/lib/realtime/sse";

const client = createSSEClient("/matches/live/stream");
client.on("update", (data) => console.log(data));
client.connect();
```

## 🔧 Configuración Backend

El frontend espera estos endpoints:

- `GET /api/v1/matches` - Lista de partidos
- `GET /api/v1/matches/live` - Partidos en vivo  
- `GET /api/v1/matches/{id}` - Detalle de partido
- `GET /api/v1/teams` - Lista de equipos
- `GET /api/v1/leagues` - Lista de ligas

## 📱 PWA

La app funciona como PWA:
- Instalable en móviles
- Funciona offline (cache)
- Service Worker automático

## 🆘 Problemas Comunes

**Puerto 3000 ocupado:**
```bash
PORT=3001 npm run dev
```

**Backend no responde:**
- Verifica que esté en http://localhost:8080
- Revisa CORS en el backend

**Errores de TypeScript:**
```bash
rm -rf .next
npm run dev
```

## 📚 Más Información

Ver [README.md](./README.md) para documentación completa.

---

¡Listo para desarrollar! 🎯
