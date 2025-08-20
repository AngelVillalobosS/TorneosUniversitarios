package com.example.torneouniversitario.models;

public class PlayerWithTeam {
    private String teamName;
    private String playerName;
    private String playerNumber;
    private String position;

    public PlayerWithTeam(String teamName, String playerName, String playerNumber, String position) {
        this.teamName = teamName;
        this.playerName = playerName;
        this.playerNumber = playerNumber;
        this.position = position;
    }

    public String getTeamName() {
        return teamName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getPlayerNumber() {
        return playerNumber;
    }

    public String getPosition() {
        return position;
    }
}
