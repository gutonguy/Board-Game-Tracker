package com.example.boardgametracker.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.boardgametracker.R;
import com.example.boardgametracker.adapter.UserAdapter;
import com.example.boardgametracker.databinding.FragmentDashboardBinding;
import com.google.firebase.auth.FirebaseAuth;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private DashboardViewModel viewModel;
    private UserAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new UserAdapter();
        binding.recyclerViewUsers.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(DashboardViewModel.class);

        viewModel.getUsersLiveData().observe(getViewLifecycleOwner(), users -> {
            // This triggers every time the data in Firestore changes!
            adapter.setUsers(users);
        });

        viewModel.getErrorLiveData().observe(getViewLifecycleOwner(), error -> {
            Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show();
        });

        viewModel.fetchUsersRealTime();

        // Logout logic
        binding.btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Navigation.findNavController(v).navigate(R.id.action_dashboardFragment_to_loginFragment);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}