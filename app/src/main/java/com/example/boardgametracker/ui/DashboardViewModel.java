package com.example.boardgametracker.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.boardgametracker.model.Game;
import com.example.boardgametracker.model.UserProfile;
import com.example.boardgametracker.model.Win;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class DashboardViewModel extends ViewModel {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private final MutableLiveData<List<UserProfile>> usersLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Game>> gamesLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Win>> gameWinsLiveData = new MutableLiveData<>();

    private ListenerRegistration winsListenerRegistration;

    public LiveData<List<UserProfile>> getUsersLiveData() {
        return usersLiveData;
    }

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public LiveData<List<Game>> getGamesLiveData() {
        return gamesLiveData;
    }

    public LiveData<List<Win>> getGameWinsLiveData() {
        return gameWinsLiveData;
    }

    // General leaderboard, sorted highest wins first
    public void fetchUsersRealTime() {
        db.collection("users")
                .orderBy("totalWins", Query.Direction.DESCENDING)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        errorLiveData.setValue("Error fetching data: " + error.getMessage());
                        return;
                    }
                    List<UserProfile> userList = new ArrayList<>();
                    if (value != null) {
                        for (QueryDocumentSnapshot doc : value) {
                            UserProfile user = doc.toObject(UserProfile.class);
                            userList.add(user);
                        }
                        usersLiveData.setValue(userList);
                    }
                });
    }

    public void fetchGames() {
        db.collection("games").addSnapshotListener((value, error) -> {
            List<Game> gameList = new ArrayList<>();
            if (value != null) {
                for (QueryDocumentSnapshot doc : value) {
                    Game game = doc.toObject(Game.class);
                    game.setId(doc.getId());
                    gameList.add(game);
                }
                gamesLiveData.setValue(gameList);
            }
        });
    }

    // Per-game leaderboard: every win logged for a specific game
    public void fetchWinsForGame(String gameId) {
        if (winsListenerRegistration != null) {
            winsListenerRegistration.remove();
        }
        winsListenerRegistration = db.collection("wins")
                .whereEqualTo("gameId", gameId)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener((value, error) -> {
                    List<Win> winList = new ArrayList<>();
                    if (value != null) {
                        for (QueryDocumentSnapshot doc : value) {
                            winList.add(doc.toObject(Win.class));
                        }
                    }
                    gameWinsLiveData.setValue(winList);
                });
    }

    // Records a win: resolves the game by name (creating it if new),
    // then atomically bumps the user's total and logs the event.
    public void recordWin(String userId, String gameName) {
        db.collection("games").whereEqualTo("name", gameName).get()
                .addOnSuccessListener(query -> {
                    if (!query.isEmpty()) {
                        String gameId = query.getDocuments().get(0).getId();
                        logWin(userId, gameId, gameName);
                    } else {
                        db.collection("games").add(new Game(null, gameName))
                                .addOnSuccessListener(docRef -> logWin(userId, docRef.getId(), gameName))
                                .addOnFailureListener(e ->
                                        errorLiveData.setValue("Failed to create game: " + e.getMessage()));
                    }
                })
                .addOnFailureListener(e ->
                        errorLiveData.setValue("Failed to look up game: " + e.getMessage()));
    }

    private void logWin(String userId, String gameId, String gameName) {
        db.collection("users").document(userId)
                .update("totalWins", FieldValue.increment(1))
                .addOnFailureListener(e ->
                        errorLiveData.setValue("Failed to update total: " + e.getMessage()));

        Win win = new Win(userId, gameId, gameName);
        db.collection("wins").add(win)
                .addOnFailureListener(e ->
                        errorLiveData.setValue("Failed to log win: " + e.getMessage()));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (winsListenerRegistration != null) {
            winsListenerRegistration.remove();
        }
    }
}