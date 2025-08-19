package com.example.torneouniversitario.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.torneouniversitario.R;
import com.example.torneouniversitario.models.Player;

import java.util.List;

public class PlayerTeamAdapter extends RecyclerView.Adapter<PlayerTeamAdapter.ViewHolder> {

    private List<Player> players;

    public PlayerTeamAdapter(List<Player> players) {
        this.players = players;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflamos el layout simple
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_myteam, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Player player = players.get(position);
        holder.tvPlayerName.setText(player.getName());
        holder.tvPlayerPosition.setText(player.getPosition());
        holder.tvPlayerNumber.setText(String.valueOf(player.getNumber()));
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvPlayerName, tvPlayerPosition, tvPlayerNumber;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPlayerName = itemView.findViewById(R.id.tvPlayerName);
            tvPlayerPosition = itemView.findViewById(R.id.tvPlayerPosition);
            tvPlayerNumber = itemView.findViewById(R.id.tvPlayerNumber);
        }
    }
}
