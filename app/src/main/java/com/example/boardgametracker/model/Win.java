package com.example.boardgametracker.model;

import com.google.firebase.firestore.ServerTimestamp;
import java.util.Date;

public class Win {
    private String userId;
    private String gameId;
    private String gameName;
    @ServerTimestamp
    private Date timestamp;

    public Win() {
    }

    public Win(String userId, String gameId, String gameName) {
        this.userId = userId;
        this.gameId = gameId;
        this.gameName = gameName;
        // timestamp is left null — @ServerTimestamp tells Firestore to fill it in
        // automatically with the server's clock when the document is written
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}