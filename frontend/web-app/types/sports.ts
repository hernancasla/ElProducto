// Tipos principales para datos deportivos

export interface League {
  id: number;
  name: string;
  type: string;
  logo: string;
  country: string;
  season: number;
}

export interface Team {
  id: number;
  name: string;
  logo: string;
  code: string;
  country: string;
  founded: number;
  venue: {
    id: number;
    name: string;
    capacity: number;
    city: string;
  };
}

export interface Match {
  id: number;
  date: string;
  timestamp: number;
  timezone: string;
  league: League;
  teams: {
    home: Team;
    away: Team;
  };
  goals: {
    home: number | null;
    away: number | null;
  };
  score: {
    halftime: {
      home: number | null;
      away: number | null;
    };
    fulltime: {
      home: number | null;
      away: number | null;
    };
    extratime: {
      home: number | null;
      away: number | null;
    } | null;
    penalty: {
      home: number | null;
      away: number | null;
    } | null;
  };
  status: {
    long: string;
    short: string;
    elapsed: number | null;
  };
}

export interface MatchEvent {
  time: {
    elapsed: number;
    extra: number | null;
  };
  team: Team;
  player: {
    id: number;
    name: string;
  };
  assist: {
    id: number | null;
    name: string | null;
  };
  type: string;
  detail: string;
  comments: string | null;
}

export interface MatchStatistics {
  team: Team;
  statistics: Array<{
    type: string;
    value: number | string | null;
  }>;
}

export interface Lineup {
  team: Team;
  formation: string;
  startXI: Array<{
    player: {
      id: number;
      name: string;
      number: number;
      pos: string;
    };
  }>;
  substitutes: Array<{
    player: {
      id: number;
      name: string;
      number: number;
      pos: string;
    };
  }>;
  coach: {
    id: number;
    name: string;
    photo: string;
  };
}

export interface Standing {
  rank: number;
  team: Team;
  points: number;
  goalsDiff: number;
  group: string;
  form: string;
  status: string;
  description: string | null;
  all: {
    played: number;
    win: number;
    draw: number;
    lose: number;
    goals: {
      for: number;
      against: number;
    };
  };
  home: {
    played: number;
    win: number;
    draw: number;
    lose: number;
    goals: {
      for: number;
      against: number;
    };
  };
  away: {
    played: number;
    win: number;
    draw: number;
    lose: number;
    goals: {
      for: number;
      against: number;
    };
  };
  update: string;
}

// Tipos de respuesta de API
export interface ApiResponse<T> {
  data: T;
  errors: string[];
  metadata: {
    page: number;
    per_page: number;
    total: number;
  };
}

// Tipos para filtros
export interface MatchFilters {
  league?: number;
  season?: number;
  team?: number;
  date?: string;
  status?: "live" | "finished" | "scheduled";
  timezone?: string;
}

// Estados de partido
export type MatchStatus = 
  | "TBD"
  | "NS"
  | "1H"
  | "HT"
  | "2H"
  | "ET"
  | "P"
  | "FT"
  | "AET"
  | "PEN"
  | "PST"
  | "CANC"
  | "ABD"
  | "AWD"
  | "WO"
  | "LIVE";
