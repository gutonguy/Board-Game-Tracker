package com.example.boardgametracker.model;

public class UserProfile {
    private String uid;
    private String fullName;
    private String email;
    private int wins;

    public UserProfile() {
    }

    public UserProfile(String uid, String fullName, String email, int wins) {
        this.uid = uid;
        this.fullName = fullName;
        this.email = email;
        this.wins = wins;
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

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }
}