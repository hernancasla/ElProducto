import { apiClient } from "./client";
import type { Team, Match } from "@/types/sports";

export const teamsApi = {
  // Obtener lista de equipos
  getTeams: async (params?: { league?: number; season?: number; country?: string }) => {
    return apiClient.get<Team[]>("/teams", params as Record<string, string | number>);
  },

  // Obtener detalle de un equipo
  getTeamById: async (id: number) => {
    return apiClient.get<Team>(`/teams/${id}`);
  },

  // Obtener próximos partidos de un equipo
  getTeamUpcomingMatches: async (id: number, limit: number = 5) => {
    return apiClient.get<Match[]>(`/teams/${id}/matches/upcoming`, { limit });
  },

  // Obtener últimos resultados de un equipo
  getTeamRecentMatches: async (id: number, limit: number = 5) => {
    return apiClient.get<Match[]>(`/teams/${id}/matches/recent`, { limit });
  },
};
