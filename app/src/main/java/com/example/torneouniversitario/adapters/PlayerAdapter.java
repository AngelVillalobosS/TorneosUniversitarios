package com.example.torneouniversitario.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.torneouniversitario.R;
import com.example.torneouniversitario.models.Player;

import java.util.List;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.ViewHolder> {

    public interface OnPlayerActionListener {
        void onEdit(Player p);
        void onDelete(Player p);
    }

    private List<Player> players;
    private OnPlayerActionListener listener;

    public PlayerAdapter(List<Player> players, OnPlayerActionListener listener) {
        this.players = players;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_player, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Player p = players.get(position);
        holder.tvName.setText(p.getName());
        holder.tvPos.setText(p.getPosition());
        holder.tvNum.setText(String.valueOf(p.getNumber()));

        holder.btnEdit.setOnClickListener(v -> listener.onEdit(p));
        holder.btnDelete.setOnClickListener(v -> listener.onDelete(p));
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPos, tvNum;
        ImageButton btnEdit, btnDelete;

        ViewHolder(View v) {
            super(v);
            tvName = v.findViewById(R.id.tvPlayerName);
            tvPos = v.findViewById(R.id.tvPlayerPosition);
            tvNum = v.findViewById(R.id.tvPlayerNumber);
            btnEdit = v.findViewById(R.id.btnEditPlayer);
            btnDelete = v.findViewById(R.id.btnDeletePlayer);
        }
    }
}
