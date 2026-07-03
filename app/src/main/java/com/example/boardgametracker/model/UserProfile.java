package com.example.boardgametracker.model;

public class UserProfile {
    private String uid;
    private String email;
    private String fullName;
    private int totalWins;

    public UserProfile() {
    }

    public UserProfile(String uid, String email, String fullName) {
        this.uid = uid;
        this.email = email;
        this.fullName = fullName;
        this.totalWins = 0;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getTotalWins() {
        return totalWins;
    }

    public void setTotalWins(int totalWins) {
        this.totalWins = totalWins;
    }
}