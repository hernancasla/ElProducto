import { apiClient } from "./client";
import type { Match, MatchEvent, MatchStatistics, Lineup, MatchFilters } from "@/types/sports";

export const matchesApi = {
  // Obtener lista de partidos con filtros
  getMatches: async (filters?: MatchFilters) => {
    return apiClient.get<Match[]>("/matches", filters as Record<string, string | number>);
  },

  // Obtener partidos en vivo
  getLiveMatches: async () => {
    return apiClient.get<Match[]>("/matches/live");
  },

  // Obtener detalle de un partido
  getMatchById: async (id: number) => {
    return apiClient.get<Match>(`/matches/${id}`);
  },

  // Obtener eventos de un partido
  getMatchEvents: async (id: number) => {
    return apiClient.get<MatchEvent[]>(`/matches/${id}/events`);
  },

  // Obtener estadísticas de un partido
  getMatchStatistics: async (id: number) => {
    return apiClient.get<MatchStatistics[]>(`/matches/${id}/statistics`);
  },

  // Obtener alineaciones de un partido
  getMatchLineups: async (id: number) => {
    return apiClient.get<Lineup[]>(`/matches/${id}/lineups`);
  },
};
