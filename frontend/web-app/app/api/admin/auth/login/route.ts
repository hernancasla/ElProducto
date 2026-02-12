import { NextRequest, NextResponse } from "next/server";
import { getIronSession } from "iron-session";
import { sessionOptions } from "@/lib/auth/session";
import { SessionData } from "@/types/auth";
import { findAdminByUsername } from "@/lib/auth/config";
import { verifyPassword } from "@/lib/auth/password";
import { isIpAllowed, getClientIp } from "@/lib/auth/ip-whitelist";

export async function POST(request: NextRequest) {
  try {
    // Verificar IP whitelist
    const clientIp = getClientIp(request);
    
    if (!isIpAllowed(clientIp)) {
      console.warn(`Login attempt from unauthorized IP: ${clientIp}`);
      return NextResponse.json(
        { error: "Access denied from your IP address" },
        { status: 403 }
      );
    }

    const body = await request.json();
    const { username, password } = body;

    if (!username || !password) {
      return NextResponse.json(
        { error: "Username and password are required" },
        { status: 400 }
      );
    }

    // Buscar administrador
    const admin = findAdminByUsername(username);

    if (!admin) {
      // No revelar si el usuario existe o no (seguridad)
      return NextResponse.json(
        { error: "Invalid credentials" },
        { status: 401 }
      );
    }

    // Verificar password
    const isValid = await verifyPassword(password, admin.passwordHash);

    if (!isValid) {
      return NextResponse.json(
        { error: "Invalid credentials" },
        { status: 401 }
      );
    }

    // Crear sesión
    const response = NextResponse.json({ success: true });
    const session = await getIronSession<SessionData>(request, response, sessionOptions);

    session.user = {
      id: username,
      username,
      role: "admin",
    };
    session.isLoggedIn = true;
    session.loginTime = Date.now();
    session.ipAddress = clientIp;

    await session.save();

    return response;
  } catch (error) {
    console.error("Login error:", error);
    return NextResponse.json(
      { error: "Internal server error" },
      { status: 500 }
    );
  }
}
