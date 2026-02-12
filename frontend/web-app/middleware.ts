import { NextResponse } from "next/server";
import type { NextRequest } from "next/server";
import { getIronSession } from "iron-session";
import { sessionOptions, defaultSession } from "@/lib/auth/session";
import { SessionData } from "@/types/auth";
import { isIpAllowed, getClientIp } from "@/lib/auth/ip-whitelist";

export async function middleware(request: NextRequest) {
  const { pathname } = request.nextUrl;

  // Solo aplicar a rutas /admin (excepto login)
  if (pathname.startsWith("/admin") && !pathname.startsWith("/admin/login")) {
    // Verificar IP whitelist
    const clientIp = getClientIp(request);
    
    if (!isIpAllowed(clientIp)) {
      console.warn(`Access denied for IP: ${clientIp}`);
      return new NextResponse("Access Denied - IP not whitelisted", {
        status: 403,
      });
    }

    // Verificar sesión
    const response = NextResponse.next();
    const session = await getIronSession<SessionData>(request, response, sessionOptions);

    if (!session.isLoggedIn) {
      // Redirigir a login
      const loginUrl = new URL("/admin/login", request.url);
      loginUrl.searchParams.set("from", pathname);
      return NextResponse.redirect(loginUrl);
    }

    // Verificar que la sesión no haya expirado (8 horas)
    const now = Date.now();
    const sessionAge = now - (session.loginTime || 0);
    const maxAge = 8 * 60 * 60 * 1000; // 8 horas en milisegundos

    if (sessionAge > maxAge) {
      // Sesión expirada, limpiar y redirigir
      session.destroy();
      const loginUrl = new URL("/admin/login", request.url);
      loginUrl.searchParams.set("expired", "true");
      return NextResponse.redirect(loginUrl);
    }

    return response;
  }

  return NextResponse.next();
}

export const config = {
  matcher: ["/admin/:path*"],
};
