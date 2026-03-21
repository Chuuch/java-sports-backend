CREATE TABLE matches (
    id UUID PRIMARY KEY,
    home_team_id UUID NOT NULL,
    away_team_id UUID NOT NULL,
    start_time TIMESTAMP WITH TIME ZONE NOT NULL,
    status VARCHAR(20) NOT NULL,
    home_score INT DEFAULT 0,
    away_score INT DEFAULT 0
);

CREATE INDEX idx_matches_status ON matches(status);
CREATE INDEX idx_matches_teams ON matches(home_team_id, away_team_id);