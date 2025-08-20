package com.example.torneouniversitario.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.torneouniversitario.R;
import com.example.torneouniversitario.models.TeamStats;
import java.util.List;

public class TeamStatsAdapter extends RecyclerView.Adapter<TeamStatsAdapter.ViewHolder> {
    private Context context;
    private List<TeamStats> teamStatsList;

    public TeamStatsAdapter(Context context, List<TeamStats> teamStatsList) {
        this.context = context;
        this.teamStatsList = teamStatsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_team_stats, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TeamStats stats = teamStatsList.get(position);
        holder.tvTeamName.setText(stats.getTeamName());
        holder.tvMatches.setText(String.valueOf(stats.getMatchesPlayed()));
        holder.tvGoalsFor.setText(String.valueOf(stats.getGoalsFor()));
        holder.tvGoalsAgainst.setText(String.valueOf(stats.getGoalsAgainst()));
        holder.tvPoints.setText(String.valueOf(stats.getPoints()));
    }

    @Override
    public int getItemCount() {
        return teamStatsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTeamName, tvMatches, tvGoalsFor, tvGoalsAgainst, tvPoints;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTeamName = itemView.findViewById(R.id.tvTeamName);
            tvMatches = itemView.findViewById(R.id.tvMatches);
            tvGoalsFor = itemView.findViewById(R.id.tvGoalsFor);
            tvGoalsAgainst = itemView.findViewById(R.id.tvGoalsAgainst);
            tvPoints = itemView.findViewById(R.id.tvPoints);
        }
    }
}
