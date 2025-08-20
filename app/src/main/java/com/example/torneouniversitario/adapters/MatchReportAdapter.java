package com.example.torneouniversitario.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.torneouniversitario.R;
import com.example.torneouniversitario.models.MatchModel;

import java.util.List;

public class MatchReportAdapter extends RecyclerView.Adapter<MatchReportAdapter.ViewHolder> {

    private List<MatchModel> matches;

    public MatchReportAdapter(List<MatchModel> matches) {
        this.matches = matches;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_report_match, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MatchModel match = matches.get(position);
        holder.tvTeams.setText("Equipo A: " + match.getTeamAId() + " vs Equipo B: " + match.getTeamBId());
        holder.tvDate.setText("Fecha: " + match.getDate() + " " + match.getTime());
        holder.tvPlace.setText("Lugar: " + match.getPlace());
    }

    @Override
    public int getItemCount() {
        return matches.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTeams, tvDate, tvPlace;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTeams = itemView.findViewById(R.id.tvTeams);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvPlace = itemView.findViewById(R.id.tvScore); // ⚠️ revisa tu XML: tvScore está siendo usado como lugar
        }
    }
}
