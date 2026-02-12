-- Script de inicialización de base de datos
-- Este script se ejecuta automáticamente cuando se crea el contenedor de PostgreSQL
-- Fecha: 2025-12-14

-- Verificar que la base de datos existe
SELECT 'Database elproducto_db created successfully!' as status;

-- Configuraciones adicionales para PostgreSQL
ALTER DATABASE elproducto_db SET timezone TO 'UTC';

-- Log de inicialización
DO $$
BEGIN
    RAISE NOTICE 'Database initialization completed at %', NOW();
END $$;