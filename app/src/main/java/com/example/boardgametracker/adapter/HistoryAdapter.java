package com.example.boardgametracker.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.boardgametracker.databinding.ItemHistoryBinding;
import com.example.boardgametracker.model.Win;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    // Lets the Fragment supply player names without the adapter needing to know about UserProfile
    public interface NameResolver {
        String resolveName(String userId);
    }

    private List<Win> wins = new ArrayList<>();
    private final NameResolver nameResolver;
    private final SimpleDateFormat dateFormat =
            new SimpleDateFormat("MMM d, yyyy • h:mm a", Locale.getDefault());

    public HistoryAdapter(NameResolver nameResolver) {
        this.nameResolver = nameResolver;
    }

    public void setWins(List<Win> wins) {
        this.wins = wins;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemHistoryBinding binding = ItemHistoryBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new HistoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        holder.bind(wins.get(position), nameResolver, dateFormat);
    }

    @Override
    public int getItemCount() {
        return wins.size();
    }

    static class HistoryViewHolder extends RecyclerView.ViewHolder {
        private final ItemHistoryBinding binding;

        public HistoryViewHolder(ItemHistoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Win win, NameResolver nameResolver, SimpleDateFormat dateFormat) {
            binding.tvHistoryPlayer.setText(nameResolver.resolveName(win.getUserId()));
            binding.tvHistoryGame.setText("won " + win.getGameName());
            binding.tvHistoryDate.setText(
                    win.getTimestamp() != null ? dateFormat.format(win.getTimestamp()) : "Just now");
        }
    }
}