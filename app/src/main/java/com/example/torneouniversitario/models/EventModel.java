package com.example.torneouniversitario.models;

public class EventModel {
    private int id, matchId, playerId, minute, teamId;
    private String type;

    public EventModel(int id, int matchId, int playerId, String type, int minute, int teamId) {
        this.id = id; this.matchId = matchId; this.playerId = playerId; this.type = type; this.minute = minute; this.teamId = teamId;
    }
    public EventModel(int matchId, int playerId, String type, int minute, int teamId) {
        this.matchId = matchId; this.playerId = playerId; this.type = type; this.minute = minute; this.teamId = teamId;
    }

    public int getId() { return id; }
    public int getMatchId() { return matchId; }
    public int getPlayerId() { return playerId; }
    public String getType() { return type; }
    public int getMinute() { return minute; }
    public int getTeamId() { return teamId; }
}
