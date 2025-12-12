# Frontend

Este directorio contiene las aplicaciones frontend de ElProducto.

## Aplicaciones

### web-app
**URL Desarrollo:** http://localhost:3000
**Descripción:** Aplicación web responsive que puede ser exportada a plataformas móviles nativas.

**Stack Tecnológico (Por Definir):**

**Opción A: React Native Web**
- React Native
- React Native Web
- Expo (opcional)
- Redux/Zustand

**Opción B: Flutter Web**
- Flutter 3.x
- Dart
- Flutter Web

**Opción C: PWA**
- React/Next.js
- TypeScript
- Tailwind CSS
- PWA

---

## Estructura (Tentativa - React)

```
web-app/
├── public/
├── src/
│   ├── components/
│   ├── pages/
│   ├── services/
│   │   └── api/
│   ├── store/
│   ├── hooks/
│   ├── utils/
│   ├── styles/
│   ├── App.tsx
│   └── index.tsx
├── package.json
├── tsconfig.json
└── README.md
```

## Desarrollo Local

```bash
cd web-app

# Instalar dependencias
npm install

# Ejecutar en desarrollo
npm run dev

# Build
npm run build
```

## Integración con Backend

La aplicación consumirá la API REST del backend:
- **Base URL Desarrollo:** http://localhost:8080/api/v1
- **Base URL Producción:** https://api.elproducto.com/api/v1

## Features Principales (MVP)

1. **Home/Dashboard**
   - Partidos en vivo
   - Próximos partidos
   - Resultados recientes

2. **Detalle de Partido**
   - Información completa
   - Estadísticas
   - Eventos (goles, tarjetas)
   - Alineaciones

3. **Competiciones**
   - Lista de ligas
   - Tabla de posiciones

4. **Equipos**
   - Información de equipos
   - Próximos partidos
   - Últimos resultados

5. **Búsqueda**
   - Buscar equipos y partidos

## Próximos Pasos

1. Decidir stack tecnológico (React Native / Flutter / PWA)
2. Crear proyecto inicial
3. Configurar routing
4. Implementar integración con API
5. Desarrollar componentes principales

Ver documentación completa en `/docs/epics/epic-03-frontend.md`