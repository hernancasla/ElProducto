-- Create leagues table
CREATE TABLE leagues (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(100),
    logo VARCHAR(500),
    country VARCHAR(100),
    season INTEGER,
    api_id BIGINT UNIQUE
);

-- Create teams table
CREATE TABLE teams (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    code VARCHAR(10),
    logo VARCHAR(500),
    country VARCHAR(100),
    founded INTEGER,
    venue_name VARCHAR(255),
    venue_city VARCHAR(100),
    venue_capacity INTEGER,
    api_id BIGINT UNIQUE
);

-- Create matches table
CREATE TABLE matches (
    id BIGSERIAL PRIMARY KEY,
    date TIMESTAMP NOT NULL,
    timestamp BIGINT,
    timezone VARCHAR(50),
    league_id BIGINT,
    home_team_id BIGINT,
    away_team_id BIGINT,
    home_goals INTEGER,
    away_goals INTEGER,
    halftime_home INTEGER,
    halftime_away INTEGER,
    fulltime_home INTEGER,
    fulltime_away INTEGER,
    status_long VARCHAR(50),
    status_short VARCHAR(10),
    elapsed INTEGER,
    venue_name VARCHAR(255),
    venue_city VARCHAR(100),
    referee_name VARCHAR(255),
    api_id BIGINT UNIQUE,
    CONSTRAINT fk_league FOREIGN KEY (league_id) REFERENCES leagues(id) ON DELETE SET NULL,
    CONSTRAINT fk_home_team FOREIGN KEY (home_team_id) REFERENCES teams(id) ON DELETE SET NULL,
    CONSTRAINT fk_away_team FOREIGN KEY (away_team_id) REFERENCES teams(id) ON DELETE SET NULL
);

-- Create indexes for better query performance
CREATE INDEX idx_matches_date ON matches(date);
CREATE INDEX idx_matches_league ON matches(league_id);
CREATE INDEX idx_matches_home_team ON matches(home_team_id);
CREATE INDEX idx_matches_away_team ON matches(away_team_id);
CREATE INDEX idx_matches_status ON matches(status_short);
CREATE INDEX idx_leagues_country ON leagues(country);
CREATE INDEX idx_leagues_season ON leagues(season);
CREATE INDEX idx_teams_country ON teams(country);
