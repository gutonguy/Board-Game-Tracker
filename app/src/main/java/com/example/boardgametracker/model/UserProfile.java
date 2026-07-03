package com.example.boardgametracker.model;

import java.util.Map;

public class UserProfile {
    private String uid;
    private String fullName;
    private String email;

    // Game name - String, Amount of Wins - Integer
    private Map<String, Integer> gameWins;

    // Required empty constructor for Firebase Serialization
    public UserProfile() {
    }

    // Updated constructor
    public UserProfile(String uid, String fullName, String email, Map<String, Integer> gameWins) {
        this.uid = uid;
        this.fullName = fullName;
        this.email = email;
        this.gameWins = gameWins;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Map<String, Integer> getGameWins() {
        return gameWins;
    }

    public void setGameWins(Map<String, Integer> gameWins) {
        this.gameWins = gameWins;
    }
}