package com.example.boardgametracker.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.navigation.Navigation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.boardgametracker.R;
import com.example.boardgametracker.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private LoginViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);

        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel.getLoginStatus().observe(getViewLifecycleOwner(), status -> {
            if (status.equals("SUCCESS")) {
                Toast.makeText(requireContext(), "Login Successful!", Toast.LENGTH_SHORT).show();
                // TODO: Add navigation to main app screen here
            } else {
                Toast.makeText(requireContext(), status, Toast.LENGTH_LONG).show();
            }
        });

        binding.btnLogin.setOnClickListener(v -> {
            String email = binding.etLoginEmail.getText().toString().trim();
            String password = binding.etLoginPassword.getText().toString().trim();

            // Basic Input Validation
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            viewModel.loginUser(email, password);
        });

        binding.btnGoToRegister.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_registerFragment)
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Prevent memory leaks
        binding = null;
    }
}