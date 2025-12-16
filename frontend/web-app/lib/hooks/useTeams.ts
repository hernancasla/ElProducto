import { useQuery } from "@tanstack/react-query";
import { teamsApi } from "@/lib/api/teams";

export function useTeams(params?: { league?: number; season?: number; country?: string }) {
  return useQuery({
    queryKey: ["teams", params],
    queryFn: () => teamsApi.getTeams(params),
  });
}

export function useTeam(id: number) {
  return useQuery({
    queryKey: ["teams", id],
    queryFn: () => teamsApi.getTeamById(id),
    enabled: !!id,
  });
}

export function useTeamUpcomingMatches(id: number, limit: number = 5) {
  return useQuery({
    queryKey: ["teams", id, "upcoming"],
    queryFn: () => teamsApi.getTeamUpcomingMatches(id, limit),
    enabled: !!id,
  });
}

export function useTeamRecentMatches(id: number, limit: number = 5) {
  return useQuery({
    queryKey: ["teams", id, "recent"],
    queryFn: () => teamsApi.getTeamRecentMatches(id, limit),
    enabled: !!id,
  });
}
