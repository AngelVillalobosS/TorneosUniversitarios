package com.example.torneouniversitario.models;

public class TeamStats {
    private String teamName;
    private int matchesPlayed;
    private int goalsFor;
    private int goalsAgainst;
    private int points;

    public TeamStats(String teamName, int matchesPlayed, int goalsFor, int goalsAgainst, int points) {
        this.teamName = teamName;
        this.matchesPlayed = matchesPlayed;
        this.goalsFor = goalsFor;
        this.goalsAgainst = goalsAgainst;
        this.points = points;
    }

    public String getTeamName() {
        return teamName;
    }

    public int getMatchesPlayed() {
        return matchesPlayed;
    }

    public int getGoalsFor() {
        return goalsFor;
    }

    public int getGoalsAgainst() {
        return goalsAgainst;
    }

    public int getPoints() {
        return points;
    }
}
