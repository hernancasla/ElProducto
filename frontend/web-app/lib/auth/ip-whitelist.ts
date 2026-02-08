/**
 * IP Whitelist para acceso al backoffice
 * Configurable vía variable de entorno ADMIN_IP_WHITELIST
 */

export function isIpAllowed(ip: string): boolean {
  const whitelist = process.env.ADMIN_IP_WHITELIST;
  
  // Si no hay whitelist configurada, permitir todos (solo en desarrollo)
  if (!whitelist) {
    if (process.env.NODE_ENV === "development") {
      return true;
    }
    // En producción, sin whitelist = denegar todos por seguridad
    console.warn("ADMIN_IP_WHITELIST not configured in production!");
    return false;
  }

  // Soporta múltiples IPs separadas por coma
  const allowedIps = whitelist.split(",").map((ip) => ip.trim());
  
  // Permitir localhost en desarrollo
  if (process.env.NODE_ENV === "development") {
    allowedIps.push("127.0.0.1", "::1", "localhost");
  }

  return allowedIps.includes(ip);
}

export function getClientIp(request: Request): string {
  // Intentar obtener IP real detrás de proxy
  const forwarded = request.headers.get("x-forwarded-for");
  const realIp = request.headers.get("x-real-ip");
  
  if (forwarded) {
    return forwarded.split(",")[0].trim();
  }
  
  if (realIp) {
    return realIp;
  }

  // Fallback (no debería llegar aquí en producción)
  return "unknown";
}
