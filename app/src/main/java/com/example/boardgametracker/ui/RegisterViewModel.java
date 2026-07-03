package com.example.boardgametracker.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.boardgametracker.model.UserProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterViewModel extends ViewModel {
    private final FirebaseAuth mAuth;
    private final FirebaseFirestore db;

    private final MutableLiveData<String> registrationStatus = new MutableLiveData<>();

    public RegisterViewModel() {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    public LiveData<String> getRegistrationStatus() {
        return registrationStatus;
    }

    public void registerUser(String fullName, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    String uid = authResult.getUser().getUid();

                    UserProfile newUser = new UserProfile(uid, fullName, email, 0);

                    db.collection("users").document(uid).set(newUser)
                            .addOnSuccessListener(aVoid -> registrationStatus.setValue("SUCCESS"))
                            .addOnFailureListener(e -> registrationStatus.setValue("Error saving to database: " + e.getMessage()));
                })
                .addOnFailureListener(e -> registrationStatus.setValue("Registration error: " + e.getMessage()));
    }
}