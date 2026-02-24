import { NextRequest, NextResponse } from "next/server";
import { getIronSession } from "iron-session";
import { sessionOptions } from "@/lib/auth/session";
import { SessionData } from "@/types/auth";

const API_BASE_URL = process.env.API_BASE_URL || "http://localhost:8080";
const ADMIN_API_KEY = process.env.ADMIN_API_KEY || "";

/**
 * POST /api/admin/migrations/run
 *
 * Server-side proxy to Spring Boot POST /api/v1/admin/migrations/run.
 * Triggers Flyway to apply all pending migrations.
 * Idempotent — safe to call even if no migrations are pending.
 */
export async function POST(request: NextRequest) {
  // 1. Verify admin session
  const response = new NextResponse();
  const session = await getIronSession<SessionData>(request, response, sessionOptions);

  if (!session.isLoggedIn) {
    return NextResponse.json({ error: "Unauthorized" }, { status: 401 });
  }

  if (!ADMIN_API_KEY) {
    return NextResponse.json(
      { error: "ADMIN_API_KEY not configured on server" },
      { status: 500 }
    );
  }

  // 2. Forward to Spring Boot
  try {
    const apiResponse = await fetch(`${API_BASE_URL}/api/v1/admin/migrations/run`, {
      method: "POST",
      headers: {
        "X-Admin-Api-Key": ADMIN_API_KEY,
        "Content-Type": "application/json",
      },
    });

    const data = await apiResponse.json();
    return NextResponse.json(data, { status: apiResponse.status });
  } catch (error) {
    console.error("Error running migrations via API:", error);
    return NextResponse.json(
      { error: "Failed to connect to API service" },
      { status: 502 }
    );
  }
}
