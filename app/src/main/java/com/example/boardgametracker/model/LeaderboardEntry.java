package com.example.boardgametracker.model;

public class LeaderboardEntry {
    private final String name;
    private final int wins;

    public LeaderboardEntry(String name, int wins) {
        this.name = name;
        this.wins = wins;
    }

    public String getName() {
        return name;
    }

    public int getWins() {
        return wins;
    }
}