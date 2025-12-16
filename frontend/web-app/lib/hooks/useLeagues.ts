import { useQuery } from "@tanstack/react-query";
import { leaguesApi } from "@/lib/api/leagues";

export function useLeagues(params?: { country?: string; season?: number }) {
  return useQuery({
    queryKey: ["leagues", params],
    queryFn: () => leaguesApi.getLeagues(params),
  });
}

export function useLeague(id: number) {
  return useQuery({
    queryKey: ["leagues", id],
    queryFn: () => leaguesApi.getLeagueById(id),
    enabled: !!id,
  });
}

export function useLeagueStandings(id: number, season?: number) {
  return useQuery({
    queryKey: ["leagues", id, "standings", season],
    queryFn: () => leaguesApi.getLeagueStandings(id, season),
    enabled: !!id,
  });
}
