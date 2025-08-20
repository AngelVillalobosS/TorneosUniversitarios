package com.example.torneouniversitario.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.torneouniversitario.R;
import com.example.torneouniversitario.models.PlayerWithTeam;

import java.util.List;

public class PlayersByTeamAdapter extends RecyclerView.Adapter<PlayersByTeamAdapter.ViewHolder> {

    private Context context;
    private List<PlayerWithTeam> playerList;

    public PlayersByTeamAdapter(Context context, List<PlayerWithTeam> playerList) {
        this.context = context;
        this.playerList = playerList;
    }

    @Override
    public PlayersByTeamAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_player_by_team, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PlayersByTeamAdapter.ViewHolder holder, int position) {
        PlayerWithTeam player = playerList.get(position);
        holder.tvTeamName.setText(player.getTeamName());
        holder.tvPlayerName.setText(player.getPlayerName());
        holder.tvPlayerNumber.setText(player.getPlayerNumber());
        holder.tvPosition.setText(player.getPosition());
    }

    @Override
    public int getItemCount() {
        return playerList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTeamName, tvPlayerName, tvPlayerNumber, tvPosition;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTeamName = itemView.findViewById(R.id.tvTeamName);
            tvPlayerName = itemView.findViewById(R.id.tvPlayerName);
            tvPlayerNumber = itemView.findViewById(R.id.tvPlayerNumber);
            tvPosition = itemView.findViewById(R.id.tvPosition);
        }
    }
}
