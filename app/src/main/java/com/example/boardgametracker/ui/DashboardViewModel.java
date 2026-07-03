package com.example.boardgametracker.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.boardgametracker.model.UserProfile;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class DashboardViewModel extends ViewModel {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final MutableLiveData<List<UserProfile>> usersLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    public LiveData<List<UserProfile>> getUsersLiveData() {
        return usersLiveData;
    }

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public void fetchUsersRealTime() {
        // Listening to the "users" collection in real-time
        db.collection("users").addSnapshotListener((value, error) -> {
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
}