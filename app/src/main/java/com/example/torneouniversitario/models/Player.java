package com.example.torneouniversitario.models;

public class Player {
    private int id;
    private String name;
    private String position;
    private int number;
    private int teamId;

    public Player(int id, String name, String position, int number, int teamId) {
        this.id = id; this.name = name; this.position = position; this.number = number; this.teamId = teamId;
    }
    public Player(String name, String position, int number, int teamId) {
        this.name = name; this.position = position; this.number = number; this.teamId = teamId;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getPosition() { return position; }
    public int getNumber() { return number; }
    public int getTeamId() { return teamId; }
}
