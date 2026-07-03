package com.example.boardgametracker.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.boardgametracker.R;
import com.example.boardgametracker.adapter.UserAdapter;
import com.example.boardgametracker.databinding.FragmentDashboardBinding;
import com.example.boardgametracker.model.Game;
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
            adapter.setUsers(users);
        });

        viewModel.getErrorLiveData().observe(getViewLifecycleOwner(), error -> {
            Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show();
        });

        viewModel.fetchUsersRealTime();

        binding.fabAddWin.setOnClickListener(v -> showAddWinDialog());
        viewModel.fetchGames();

        // Logout logic
        binding.btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Navigation.findNavController(v).navigate(R.id.action_dashboardFragment_to_loginFragment);
        });
    }

    private void showAddWinDialog() {
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_win, null);
        AutoCompleteTextView actv = dialogView.findViewById(R.id.actvGameName);

        // Populate autocomplete with games from ViewModel
        viewModel.getGamesLiveData().observe(getViewLifecycleOwner(), games -> {
            ArrayAdapter<Game> gamesAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, games);
            actv.setAdapter(gamesAdapter);
        });

        new AlertDialog.Builder(requireContext())
                .setTitle("Record Win")
                .setView(dialogView)
                .setPositiveButton("Save", (dialog, which) -> {
                    String gameName = actv.getText().toString().trim();
                    if (!gameName.isEmpty()) {
                        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        viewModel.recordWin(userId, gameName);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}