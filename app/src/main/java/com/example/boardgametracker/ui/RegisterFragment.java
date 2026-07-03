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
import com.example.boardgametracker.databinding.FragmentRegisterBinding;

public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;
    private RegisterViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel.getRegistrationStatus().observe(getViewLifecycleOwner(), status -> {
            if (status.equals("SUCCESS")) {
                Toast.makeText(requireContext(), "Registered Successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), status, Toast.LENGTH_LONG).show();
            }
        });

        binding.btnRegister.setOnClickListener(v -> {
            String fullName = binding.etFullName.getText().toString().trim();
            String email = binding.etEmail.getText().toString().trim();
            String password = binding.etPassword.getText().toString().trim();

            if (fullName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill all the fields!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (password.length() < 6) {
                Toast.makeText(requireContext(), "Password must be at least 6 chars.", Toast.LENGTH_SHORT).show();
                return;
            }

            viewModel.registerUser(fullName, email, password);
        });

        binding.btnGoToLogin.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_registerFragment_to_loginFragment)
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // מניעת זליגת זיכרון (Memory Leak)
    }
}