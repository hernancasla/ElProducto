# ElProducto - PWA de Resultados Deportivos

Aplicación web progresiva (PWA) para seguir resultados deportivos en tiempo real, construida con Next.js 14, React 18, y TypeScript.

## 🚀 Stack Tecnológico

### Core
- **Next.js 14** (App Router) - Framework React con SSR y optimizaciones
- **React 18** - Biblioteca UI con Server Components
- **TypeScript** - Tipado estático
- **Tailwind CSS v3** - Framework CSS utility-first

### Estado y Data Fetching
- **TanStack Query v5** (React Query) - Data fetching, caching y sincronización
- **Zustand** - Estado global simple y performante

### UI Components
- **shadcn/ui** - Componentes accesibles y customizables
- **Lucide React** - Iconos
- **Framer Motion** - Animaciones fluidas
- **class-variance-authority** - Manejo de variantes CSS

### Tiempo Real
- **Server-Sent Events (SSE)** - Para streaming de actualizaciones
- **Socket.io Client** - Para WebSockets bidireccionales

### PWA
- **next-pwa** - Service Workers y cache strategies
- **Manifest.json** - Configuración de la PWA

### Utilidades
- **Zod** - Validación de esquemas
- **date-fns** - Manejo de fechas
- **clsx + tailwind-merge** - Utilidades para clases CSS

## 📁 Estructura del Proyecto

```
web-app/
├── app/                          # Next.js App Router
│   ├── partidos/                # Rutas de partidos
│   ├── ligas/                   # Rutas de ligas
│   ├── equipos/                 # Rutas de equipos
│   ├── api/                     # API Routes (SSE endpoints)
│   ├── layout.tsx               # Layout raíz
│   ├── page.tsx                 # Página principal
│   └── globals.css              # Estilos globales
│
├── components/                  # Componentes React
│   ├── ui/                      # Componentes base (shadcn)
│   │   ├── button.tsx
│   │   ├── card.tsx
│   │   └── badge.tsx
│   ├── matches/                 # Componentes de partidos
│   ├── teams/                   # Componentes de equipos
│   ├── leagues/                 # Componentes de ligas
│   ├── layout/                  # Componentes de layout
│   └── shared/                  # Componentes compartidos
│
├── lib/                         # Lógica de negocio
│   ├── api/                     # Clientes de API
│   │   ├── client.ts            # Cliente HTTP base
│   │   ├── matches.ts           # API de partidos
│   │   ├── teams.ts             # API de equipos
│   │   └── leagues.ts           # API de ligas
│   ├── hooks/                   # Custom hooks
│   │   ├── useMatches.ts        # Hooks de partidos
│   │   ├── useTeams.ts          # Hooks de equipos
│   │   └── useLeagues.ts        # Hooks de ligas
│   ├── store/                   # Zustand stores
│   │   └── index.ts             # Store de favoritos y config
│   ├── realtime/                # Clientes de tiempo real
│   │   ├── sse.ts               # Cliente SSE
│   │   └── websocket.ts         # Cliente WebSocket
│   ├── providers.tsx            # React Query provider
│   └── utils.ts                 # Utilidades generales
│
├── types/                       # Definiciones TypeScript
│   └── sports.ts                # Tipos de datos deportivos
│
├── public/                      # Archivos estáticos
│   ├── manifest.json            # PWA manifest
│   ├── icon-192x192.png         # Icono PWA (crear)
│   └── icon-512x512.png         # Icono PWA (crear)
│
├── .env.example                 # Variables de entorno ejemplo
├── .env.local                   # Variables de entorno locales
├── next.config.ts               # Configuración Next.js + PWA
├── tailwind.config.js           # Configuración Tailwind
├── tsconfig.json                # Configuración TypeScript
└── package.json                 # Dependencias
```

## 🛠️ Instalación y Desarrollo

### Prerequisitos
- Node.js 18+ 
- npm o yarn
- Backend API corriendo en `http://localhost:8080`

### Instalación

```bash
# Instalar dependencias
npm install

# Copiar variables de entorno
cp .env.example .env.local

# Editar .env.local con tus configuraciones
```

### Scripts de Desarrollo

```bash
# Iniciar servidor de desarrollo
npm run dev
# Abrir http://localhost:3000

# Build para producción
npm run build

# Iniciar en producción
npm run start

# Linting
npm run lint

# Formatear código
npx prettier --write .
```

## 🌐 Configuración de Variables de Entorno

Edita `.env.local`:

```bash
# URL del backend API
NEXT_PUBLIC_API_URL=http://localhost:8080/api/v1

# URL de WebSocket
NEXT_PUBLIC_WS_URL=ws://localhost:8080
```

## 🔌 Integración con Backend

La aplicación consume el backend Java/Spring Boot a través de:

### REST API
- Base URL: `http://localhost:8080/api/v1`
- Endpoints principales:
  - `GET /matches` - Lista de partidos
  - `GET /matches/live` - Partidos en vivo
  - `GET /matches/{id}` - Detalle de partido
  - `GET /teams` - Lista de equipos
  - `GET /leagues` - Lista de ligas
  - `GET /leagues/{id}/standings` - Tabla de posiciones

### Tiempo Real

#### Server-Sent Events (SSE)
```typescript
import { createSSEClient } from "@/lib/realtime/sse";

const sseClient = createSSEClient("/matches/live/stream");
sseClient.on("match-update", (data) => {
  console.log("Match updated:", data);
});
sseClient.connect();
```

#### WebSocket (Socket.io)
```typescript
import { createWebSocketClient } from "@/lib/realtime/websocket";

const wsClient = createWebSocketClient();
wsClient.connect();
wsClient.subscribeToLiveMatches();
wsClient.on("live-update", (data) => {
  console.log("Live update:", data);
});
```

## 🎨 Uso de Componentes

### Ejemplo: Usar React Query Hooks

```typescript
import { useLiveMatches } from "@/lib/hooks/useMatches";

function LiveMatchesPage() {
  const { data, isLoading, error } = useLiveMatches();
  
  if (isLoading) return <div>Cargando...</div>;
  if (error) return <div>Error: {error.message}</div>;
  
  return (
    <div>
      {data?.data.map((match) => (
        <MatchCard key={match.id} match={match} />
      ))}
    </div>
  );
}
```

### Ejemplo: Usar Zustand Store

```typescript
import { useFavoritesStore } from "@/lib/store";

function TeamCard({ team }) {
  const { isFavoriteTeam, addFavoriteTeam, removeFavoriteTeam } = useFavoritesStore();
  const isFavorite = isFavoriteTeam(team.id);
  
  return (
    <div>
      <h3>{team.name}</h3>
      <button onClick={() => 
        isFavorite ? removeFavoriteTeam(team.id) : addFavoriteTeam(team.id)
      }>
        {isFavorite ? "Quitar de favoritos" : "Agregar a favoritos"}
      </button>
    </div>
  );
}
```

## 📱 PWA Features

La aplicación está configurada como PWA con:

✅ **Instalable** - Puede instalarse en dispositivos móviles y desktop  
✅ **Offline Ready** - Service Worker para cache y funcionalidad offline  
✅ **App-like** - Experiencia de aplicación nativa  
✅ **Responsive** - Diseño adaptativo mobile-first  
✅ **Fast** - Optimizado para performance  

### Service Worker
- Generado automáticamente por `next-pwa`
- Cache de assets estáticos
- Estrategia de cache configurables
- Deshabilitado en desarrollo

## 🎯 Próximos Pasos

### Componentes a Crear
- [ ] `MatchCard` - Tarjeta de partido
- [ ] `LiveScore` - Marcador en vivo
- [ ] `StandingsTable` - Tabla de posiciones
- [ ] `TeamBadge` - Badge de equipo
- [ ] `MatchEvents` - Eventos del partido
- [ ] `Navigation` - Navegación principal
- [ ] `SearchBar` - Barra de búsqueda

### Páginas a Desarrollar
- [ ] Home/Dashboard - `/`
- [ ] Partidos en vivo - `/partidos/en-vivo`
- [ ] Detalle de partido - `/partidos/[id]`
- [ ] Lista de ligas - `/ligas`
- [ ] Tabla de posiciones - `/ligas/[id]/tabla`
- [ ] Detalle de equipo - `/equipos/[id]`
- [ ] Búsqueda - `/buscar`

### Features Adicionales
- [ ] Notificaciones push
- [ ] Dark mode
- [ ] Filtros avanzados
- [ ] Búsqueda global
- [ ] Favoritos persistentes
- [ ] Compartir resultados

## 🧪 Testing (Futuro)

```bash
# Unit tests con Jest
npm run test

# E2E tests con Playwright
npm run test:e2e

# Coverage
npm run test:coverage
```

## 📦 Build y Deploy

### Build de Producción

```bash
npm run build
```

Esto genera:
- Archivos estáticos optimizados en `.next/`
- Service Worker en `public/`
- Assets optimizados

### Deploy

La aplicación puede desplegarse en:

- **Vercel** (recomendado para Next.js)
  ```bash
  npm i -g vercel
  vercel
  ```

- **Netlify**
  ```bash
  npm run build
  # Deploy carpeta .next/
  ```

- **Docker**
  ```dockerfile
  FROM node:18-alpine
  WORKDIR /app
  COPY package*.json ./
  RUN npm ci
  COPY . .
  RUN npm run build
  CMD ["npm", "start"]
  ```

## 🐛 Troubleshooting

### El backend no responde
- Verifica que el backend esté corriendo en `http://localhost:8080`
- Revisa la configuración en `.env.local`
- Verifica CORS en el backend

### Service Worker no funciona
- Service Workers solo funcionan en HTTPS o localhost
- En desarrollo está deshabilitado por defecto
- Revisar en DevTools > Application > Service Workers

### Estilos no se aplican
- Ejecuta `npm run build` para regenerar CSS
- Limpia cache: `rm -rf .next`
- Verifica que globals.css esté importado en layout.tsx

## 📚 Recursos

- [Next.js Docs](https://nextjs.org/docs)
- [TanStack Query Docs](https://tanstack.com/query/latest)
- [Tailwind CSS Docs](https://tailwindcss.com/docs)
- [shadcn/ui Docs](https://ui.shadcn.com)
- [Zustand Docs](https://zustand-demo.pmnd.rs/)

## 📄 Licencia

Ver archivo LICENSE en el repositorio principal.

---

**Desarrollado para ElProducto** 🎯⚽
