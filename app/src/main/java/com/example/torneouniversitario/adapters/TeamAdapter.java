package com.example.torneouniversitario.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.torneouniversitario.R;
import com.example.torneouniversitario.models.Team;

import java.util.List;

public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.VH> {

    public interface OnTeamActionListener {
        void onEdit(Team t);

        void onDelete(Team t);
    }

    private List<Team> list;
    private OnTeamActionListener listener;

    public TeamAdapter(List<Team> list, OnTeamActionListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_team, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        Team t = list.get(position);
        holder.tvName.setText(t.getName());
        holder.tvSport.setText(t.getSport());
        holder.btnEdit.setOnClickListener(v -> {
            if (listener != null) listener.onEdit(t);
        });
        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) listener.onDelete(t);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvName, tvSport;
        ImageButton btnEdit, btnDelete;

        VH(View v) {
            super(v);
            tvName = v.findViewById(R.id.tvTeamName);
            tvSport = v.findViewById(R.id.tvTeamSport);
            btnEdit = v.findViewById(R.id.btnEditTeam);
            btnDelete = v.findViewById(R.id.btnDeleteTeam);
        }
    }
}
