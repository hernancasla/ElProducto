import { NextRequest, NextResponse } from "next/server";
import { getIronSession } from "iron-session";
import { sessionOptions } from "@/lib/auth/session";
import { SessionData } from "@/types/auth";

const API_BASE_URL = process.env.API_BASE_URL || "http://localhost:8080";
const ADMIN_API_KEY = process.env.ADMIN_API_KEY || "";

/**
 * GET /api/admin/migrations
 *
 * Server-side proxy to Spring Boot GET /api/v1/admin/migrations.
 * - Validates the admin iron-session (user must be logged in)
 * - Adds X-Admin-Api-Key header (never exposed to the browser)
 * - Returns the migration list from Flyway
 */
export async function GET(request: NextRequest) {
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

  // 2. Forward request to Spring Boot with the API key
  try {
    const apiResponse = await fetch(`${API_BASE_URL}/api/v1/admin/migrations`, {
      headers: {
        "X-Admin-Api-Key": ADMIN_API_KEY,
        "Content-Type": "application/json",
      },
      cache: "no-store",
    });

    const data = await apiResponse.json();
    return NextResponse.json(data, { status: apiResponse.status });
  } catch (error) {
    console.error("Error fetching migrations from API:", error);
    return NextResponse.json(
      { error: "Failed to connect to API service" },
      { status: 502 }
    );
  }
}
