package com.example.boardgametracker.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.boardgametracker.databinding.ItemUserBinding;
import com.example.boardgametracker.model.UserProfile;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<UserProfile> userList = new ArrayList<>();

    public void setUsers(List<UserProfile> users) {
        this.userList = users;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemUserBinding binding = ItemUserBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new UserViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        UserProfile user = userList.get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        private final ItemUserBinding binding;

        public UserViewHolder(ItemUserBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(UserProfile user) {
            binding.tvUserName.setText(user.getFullName());
            binding.tvTotalWins.setText("Wins: " + user.getTotalWins());
        }
    }
}