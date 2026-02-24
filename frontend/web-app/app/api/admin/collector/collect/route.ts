import { NextRequest, NextResponse } from "next/server";
import { getIronSession } from "iron-session";
import { sessionOptions } from "@/lib/auth/session";
import { SessionData } from "@/types/auth";

const API_BASE_URL = process.env.API_BASE_URL || "http://localhost:8080";
const ADMIN_API_KEY = process.env.ADMIN_API_KEY || "";

/**
 * POST /api/admin/collector/collect?type=<type>[&params...]
 *
 * Proxy → Spring Boot POST /api/v1/admin/collector/collect/<type>[?params]
 *
 * Supported types and their required query params:
 *   countries                  (no extra params)
 *   leagues    countryCode=AR
 *   teams      country=Argentina
 *   players    teamId=26&season=2024
 *   fixtures   leagueId=128&season=2024
 *   standings  leagueId=128&season=2024
 *   statistics fixtureId=1234567
 *   events     fixtureId=1234567
 *   lineups    fixtureId=1234567
 */
export async function POST(request: NextRequest) {
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

  const searchParams = request.nextUrl.searchParams;
  const type = searchParams.get("type");

  if (!type) {
    return NextResponse.json({ error: "Missing required query param: type" }, { status: 400 });
  }

  // Build the downstream URL with the same query params (minus "type")
  const forwardParams = new URLSearchParams();
  searchParams.forEach((value, key) => {
    if (key !== "type") forwardParams.set(key, value);
  });

  const paramsStr = forwardParams.toString();
  const upstreamPath = `/api/v1/admin/collector/collect/${type}${paramsStr ? `?${paramsStr}` : ""}`;

  try {
    const apiResponse = await fetch(`${API_BASE_URL}${upstreamPath}`, {
      method: "POST",
      headers: {
        "X-Admin-Api-Key": ADMIN_API_KEY,
        "Content-Type": "application/json",
      },
    });

    const data = await apiResponse.json();
    return NextResponse.json(data, { status: apiResponse.status });
  } catch (error) {
    console.error(`Error triggering collection [${type}]:`, error);
    return NextResponse.json(
      { error: "Failed to connect to API service" },
      { status: 502 }
    );
  }
}
