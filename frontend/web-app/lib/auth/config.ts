/**
 * Configuración de administradores
 * En producción, estas credenciales deberían venir de variables de entorno
 */

export interface AdminCredentials {
  username: string;
  passwordHash: string;
}

// IMPORTANTE: En producción, usar variables de entorno
// Generar hash: bcrypt.hash("tu_password", 10)
export const ADMIN_USERS: AdminCredentials[] = [
  {
    username: process.env.ADMIN_USERNAME || "admin",
    // Default password: "admin123" (CAMBIAR EN PRODUCCIÓN!)
    passwordHash: process.env.ADMIN_PASSWORD_HASH || 
      "$2a$10$YourHashHere", // Placeholder, se debe generar
  },
];

export function findAdminByUsername(username: string): AdminCredentials | undefined {
  return ADMIN_USERS.find((admin) => admin.username === username);
}
