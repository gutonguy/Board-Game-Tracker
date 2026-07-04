package com.example.boardgametracker.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.example.boardgametracker.adapter.LeaderboardAdapter;
import com.example.boardgametracker.databinding.FragmentDashboardBinding;
import com.example.boardgametracker.model.Game;
import com.example.boardgametracker.model.LeaderboardEntry;
import com.example.boardgametracker.model.UserProfile;
import com.example.boardgametracker.model.Win;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private DashboardViewModel viewModel;
    private LeaderboardAdapter adapter;

    private List<UserProfile> cachedUsers = new ArrayList<>();
    private List<Game> cachedGames = new ArrayList<>();
    private String selectedGameId = null;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new LeaderboardAdapter();
        binding.recyclerViewUsers.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(DashboardViewModel.class);

        viewModel.getUsersLiveData().observe(getViewLifecycleOwner(), users -> {
            cachedUsers = users;
            if (selectedGameId == null) {
                renderGeneralLeaderboard();
            }
        });

        viewModel.getGameWinsLiveData().observe(getViewLifecycleOwner(), wins -> {
            if (selectedGameId != null) {
                renderGameLeaderboard(wins);
            }
        });

        viewModel.getErrorLiveData().observe(getViewLifecycleOwner(), error -> {
            Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show();
        });

        viewModel.getGamesLiveData().observe(getViewLifecycleOwner(), games -> {
            cachedGames = games;
            setupGameFilterSpinner();
        });

        viewModel.fetchUsersRealTime();
        viewModel.fetchGames();

        binding.fabAddWin.setOnClickListener(v -> showAddWinDialog());

        binding.btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Navigation.findNavController(v).navigate(R.id.action_dashboardFragment_to_loginFragment);
        });
    }

    private void setupGameFilterSpinner() {
        List<String> spinnerLabels = new ArrayList<>();
        spinnerLabels.add("All Games");
        for (Game game : cachedGames) {
            spinnerLabels.add(game.getName());
        }

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                requireContext(), android.R.layout.simple_spinner_item, spinnerLabels);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerGameFilter.setAdapter(spinnerAdapter);

        binding.spinnerGameFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    selectedGameId = null;
                    renderGeneralLeaderboard();
                } else {
                    Game selectedGame = cachedGames.get(position - 1);
                    selectedGameId = selectedGame.getId();
                    viewModel.fetchWinsForGame(selectedGameId);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void renderGeneralLeaderboard() {
        List<LeaderboardEntry> entries = new ArrayList<>();
        for (UserProfile user : cachedUsers) {
            entries.add(new LeaderboardEntry(user.getFullName(), user.getTotalWins()));
        }
        adapter.setEntries(entries);
    }

    private void renderGameLeaderboard(List<Win> wins) {
        Map<String, Integer> countsByUserId = new HashMap<>();
        for (Win win : wins) {
            countsByUserId.put(win.getUserId(), countsByUserId.getOrDefault(win.getUserId(), 0) + 1);
        }

        List<LeaderboardEntry> entries = new ArrayList<>();
        for (Map.Entry<String, Integer> count : countsByUserId.entrySet()) {
            String name = resolveUserName(count.getKey());
            entries.add(new LeaderboardEntry(name, count.getValue()));
        }

        entries.sort((a, b) -> b.getWins() - a.getWins());
        adapter.setEntries(entries);
    }

    private String resolveUserName(String userId) {
        for (UserProfile user : cachedUsers) {
            if (userId.equals(user.getUid())) {
                return user.getFullName();
            }
        }
        return "Unknown player";
    }

    private void showAddWinDialog() {
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_win, null);
        AutoCompleteTextView actv = dialogView.findViewById(R.id.actvGameName);

        ArrayAdapter<Game> gameNameAdapter = new ArrayAdapter<>(
                requireContext(), android.R.layout.simple_dropdown_item_1line, cachedGames);
        actv.setAdapter(gameNameAdapter);

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