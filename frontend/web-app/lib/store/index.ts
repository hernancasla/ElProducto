import { create } from "zustand";
import { persist } from "zustand/middleware";
import type { Match, Team, League } from "@/types/sports";

// Store para favoritos
interface FavoritesState {
  favoriteTeams: number[];
  favoriteLeagues: number[];
  addFavoriteTeam: (teamId: number) => void;
  removeFavoriteTeam: (teamId: number) => void;
  addFavoriteLeague: (leagueId: number) => void;
  removeFavoriteLeague: (leagueId: number) => void;
  isFavoriteTeam: (teamId: number) => boolean;
  isFavoriteLeague: (leagueId: number) => boolean;
}

export const useFavoritesStore = create<FavoritesState>()(
  persist(
    (set, get) => ({
      favoriteTeams: [],
      favoriteLeagues: [],
      
      addFavoriteTeam: (teamId) =>
        set((state) => ({
          favoriteTeams: [...state.favoriteTeams, teamId],
        })),
      
      removeFavoriteTeam: (teamId) =>
        set((state) => ({
          favoriteTeams: state.favoriteTeams.filter((id) => id !== teamId),
        })),
      
      addFavoriteLeague: (leagueId) =>
        set((state) => ({
          favoriteLeagues: [...state.favoriteLeagues, leagueId],
        })),
      
      removeFavoriteLeague: (leagueId) =>
        set((state) => ({
          favoriteLeagues: state.favoriteLeagues.filter((id) => id !== leagueId),
        })),
      
      isFavoriteTeam: (teamId) => get().favoriteTeams.includes(teamId),
      
      isFavoriteLeague: (leagueId) => get().favoriteLeagues.includes(leagueId),
    }),
    {
      name: "favorites-storage",
    }
  )
);

// Store para configuración de la app
interface AppState {
  darkMode: boolean;
  toggleDarkMode: () => void;
  notifications: boolean;
  toggleNotifications: () => void;
}

export const useAppStore = create<AppState>()(
  persist(
    (set) => ({
      darkMode: false,
      toggleDarkMode: () => set((state) => ({ darkMode: !state.darkMode })),
      notifications: true,
      toggleNotifications: () => set((state) => ({ notifications: !state.notifications })),
    }),
    {
      name: "app-storage",
    }
  )
);
