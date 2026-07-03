package com.example.boardgametracker.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;

public class LoginViewModel extends ViewModel {
    private final FirebaseAuth mAuth;
    private final MutableLiveData<String> loginStatus = new MutableLiveData<>();

    public LoginViewModel() {
        mAuth = FirebaseAuth.getInstance();
    }

    public LiveData<String> getLoginStatus() {
        return loginStatus;
    }

    public void loginUser(String email, String password) {
        // Authenticate user with Firebase
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> loginStatus.setValue("SUCCESS"))
                .addOnFailureListener(e -> loginStatus.setValue("Login Error: " + e.getMessage()));
    }
}