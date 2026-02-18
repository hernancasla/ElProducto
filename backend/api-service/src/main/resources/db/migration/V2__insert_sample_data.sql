-- Insert sample leagues
INSERT INTO leagues (name, type, logo, country, season, api_id) VALUES
('Premier League', 'League', 'https://media.api-sports.io/football/leagues/39.png', 'England', 2023, 39),
('La Liga', 'League', 'https://media.api-sports.io/football/leagues/140.png', 'Spain', 2023, 140),
('Bundesliga', 'League', 'https://media.api-sports.io/football/leagues/78.png', 'Germany', 2023, 78),
('Serie A', 'League', 'https://media.api-sports.io/football/leagues/135.png', 'Italy', 2023, 135),
('Ligue 1', 'League', 'https://media.api-sports.io/football/leagues/61.png', 'France', 2023, 61);

-- Insert sample teams (Premier League)
INSERT INTO teams (name, code, logo, country, founded, venue_name, venue_city, venue_capacity, api_id) VALUES
('Manchester City', 'MCI', 'https://media.api-sports.io/football/teams/50.png', 'England', 1880, 'Etihad Stadium', 'Manchester', 55097, 50),
('Arsenal', 'ARS', 'https://media.api-sports.io/football/teams/42.png', 'England', 1886, 'Emirates Stadium', 'London', 60704, 42),
('Liverpool', 'LIV', 'https://media.api-sports.io/football/teams/40.png', 'England', 1892, 'Anfield', 'Liverpool', 53394, 40),
('Manchester United', 'MUN', 'https://media.api-sports.io/football/teams/33.png', 'England', 1878, 'Old Trafford', 'Manchester', 76212, 33),
('Chelsea', 'CHE', 'https://media.api-sports.io/football/teams/49.png', 'England', 1905, 'Stamford Bridge', 'London', 40834, 49);

-- Insert sample teams (La Liga)
INSERT INTO teams (name, code, logo, country, founded, venue_name, venue_city, venue_capacity, api_id) VALUES
('Real Madrid', 'RMA', 'https://media.api-sports.io/football/teams/541.png', 'Spain', 1902, 'Santiago Bernabéu', 'Madrid', 81044, 541),
('Barcelona', 'BAR', 'https://media.api-sports.io/football/teams/529.png', 'Spain', 1899, 'Camp Nou', 'Barcelona', 99354, 529),
('Atletico Madrid', 'ATM', 'https://media.api-sports.io/football/teams/530.png', 'Spain', 1903, 'Wanda Metropolitano', 'Madrid', 68456, 530);

-- Insert sample teams (Bundesliga)
INSERT INTO teams (name, code, logo, country, founded, venue_name, venue_city, venue_capacity, api_id) VALUES
('Bayern Munich', 'BAY', 'https://media.api-sports.io/football/teams/157.png', 'Germany', 1900, 'Allianz Arena', 'München', 75024, 157),
('Borussia Dortmund', 'BVB', 'https://media.api-sports.io/football/teams/165.png', 'Germany', 1909, 'Signal-Iduna-Park', 'Dortmund', 81365, 165);

-- Insert sample matches (upcoming and recent)
INSERT INTO matches (date, timestamp, timezone, league_id, home_team_id, away_team_id, home_goals, away_goals,
                     halftime_home, halftime_away, fulltime_home, fulltime_away,
                     status_long, status_short, elapsed, venue_name, venue_city, referee_name, api_id) VALUES
-- Recent completed matches
('2024-02-15 20:00:00', 1708041600, 'UTC', 1, 1, 2, 2, 1, 1, 0, 2, 1, 'Match Finished', 'FT', 90, 'Etihad Stadium', 'Manchester', 'Michael Oliver', 1001),
('2024-02-15 17:30:00', 1708032600, 'UTC', 1, 3, 5, 3, 1, 2, 0, 3, 1, 'Match Finished', 'FT', 90, 'Anfield', 'Liverpool', 'Anthony Taylor', 1002),
('2024-02-16 21:00:00', 1708131600, 'UTC', 2, 6, 7, 2, 2, 1, 1, 2, 2, 'Match Finished', 'FT', 90, 'Santiago Bernabéu', 'Madrid', 'José María Sánchez', 1003),
('2024-02-17 15:30:00', 1708185000, 'UTC', 3, 9, 10, 1, 3, 0, 2, 1, 3, 'Match Finished', 'FT', 90, 'Allianz Arena', 'München', 'Felix Brych', 1004),

-- Live matches (in progress)
('2024-02-18 15:00:00', 1708268400, 'UTC', 1, 4, 1, 0, 1, 0, 1, NULL, NULL, 'Second Half', '2H', 67, 'Old Trafford', 'Manchester', 'Simon Hooper', 1005),
('2024-02-18 17:30:00', 1708277400, 'UTC', 2, 7, 8, 1, 0, 1, 0, NULL, NULL, 'First Half', '1H', 38, 'Camp Nou', 'Barcelona', 'Alejandro Hernández', 1006),

-- Upcoming matches
('2024-02-19 20:00:00', 1708372800, 'UTC', 1, 2, 3, NULL, NULL, NULL, NULL, NULL, NULL, 'Not Started', 'NS', NULL, 'Emirates Stadium', 'London', 'Paul Tierney', 1007),
('2024-02-19 21:00:00', 1708376400, 'UTC', 2, 8, 6, NULL, NULL, NULL, NULL, NULL, NULL, 'Not Started', 'NS', NULL, 'Wanda Metropolitano', 'Madrid', 'Carlos del Cerro', 1008),
('2024-02-20 19:30:00', 1708456200, 'UTC', 3, 10, 9, NULL, NULL, NULL, NULL, NULL, NULL, 'Not Started', 'NS', NULL, 'Signal-Iduna-Park', 'Dortmund', 'Tobias Stieler', 1009),
('2024-02-20 20:00:00', 1708458000, 'UTC', 1, 5, 4, NULL, NULL, NULL, NULL, NULL, NULL, 'Not Started', 'NS', NULL, 'Stamford Bridge', 'London', 'Craig Pawson', 1010);
