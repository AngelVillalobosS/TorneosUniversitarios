package com.example.torneouniversitario.models;

public class MatchModel {
    private int id, teamAId, teamBId;
    private String date, time, place;

    public MatchModel(int id, int teamAId, int teamBId, String date, String time, String place) {
        this.id = id; this.teamAId = teamAId; this.teamBId = teamBId; this.date = date; this.time = time; this.place = place;
    }
    public MatchModel(int teamAId, int teamBId, String date, String time, String place) {
        this.teamAId = teamAId; this.teamBId = teamBId; this.date = date; this.time = time; this.place = place;
    }

    public int getId() { return id; }
    public int getTeamAId() { return teamAId; }
    public int getTeamBId() { return teamBId; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public String getPlace() { return place; }
}
