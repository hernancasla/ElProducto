import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { matchesApi } from "@/lib/api/matches";
import type { MatchFilters } from "@/types/sports";

// Hook para obtener lista de partidos
export function useMatches(filters?: MatchFilters) {
  return useQuery({
    queryKey: ["matches", filters],
    queryFn: () => matchesApi.getMatches(filters),
  });
}

// Hook para obtener partidos en vivo
export function useLiveMatches() {
  return useQuery({
    queryKey: ["matches", "live"],
    queryFn: () => matchesApi.getLiveMatches(),
    refetchInterval: 30000, // Refetch cada 30 segundos
  });
}

// Hook para obtener detalle de un partido
export function useMatch(id: number) {
  return useQuery({
    queryKey: ["matches", id],
    queryFn: () => matchesApi.getMatchById(id),
    enabled: !!id,
  });
}

// Hook para obtener eventos de un partido
export function useMatchEvents(id: number) {
  return useQuery({
    queryKey: ["matches", id, "events"],
    queryFn: () => matchesApi.getMatchEvents(id),
    enabled: !!id,
  });
}

// Hook para obtener estadísticas de un partido
export function useMatchStatistics(id: number) {
  return useQuery({
    queryKey: ["matches", id, "statistics"],
    queryFn: () => matchesApi.getMatchStatistics(id),
    enabled: !!id,
  });
}

// Hook para obtener alineaciones de un partido
export function useMatchLineups(id: number) {
  return useQuery({
    queryKey: ["matches", id, "lineups"],
    queryFn: () => matchesApi.getMatchLineups(id),
    enabled: !!id,
  });
}
