import { apiClient } from "./client";
import type { League, Standing } from "@/types/sports";

export const leaguesApi = {
  // Obtener lista de ligas
  getLeagues: async (params?: { country?: string; season?: number }) => {
    return apiClient.get<League[]>("/leagues", params as Record<string, string | number>);
  },

  // Obtener detalle de una liga
  getLeagueById: async (id: number) => {
    return apiClient.get<League>(`/leagues/${id}`);
  },

  // Obtener tabla de posiciones
  getLeagueStandings: async (id: number, season?: number) => {
    return apiClient.get<Standing[]>(`/leagues/${id}/standings`, season ? { season } : undefined);
  },
};
