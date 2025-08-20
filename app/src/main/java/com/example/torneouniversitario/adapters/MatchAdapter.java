package com.example.torneouniversitario.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.torneouniversitario.R;
import com.example.torneouniversitario.db.DBHelper;
import com.example.torneouniversitario.models.MatchModel;
import com.example.torneouniversitario.models.Team;

import java.util.List;

public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.VH> {

    public interface OnMatchActionListener {
        void onEdit(MatchModel m);
        void onDelete(MatchModel m);
    }

    private List<MatchModel> list;
    private OnMatchActionListener listener;
    private DBHelper dbHelper;

    public MatchAdapter(List<MatchModel> list, OnMatchActionListener listener, DBHelper dbHelper) {
        this.list = list;
        this.listener = listener;
        this.dbHelper = dbHelper;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_match, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        MatchModel m = list.get(position);

        // Obtenemos los nombres de los equipos desde la base de datos
        Team teamA = dbHelper.getTeamById(m.getTeamAId());
        Team teamB = dbHelper.getTeamById(m.getTeamBId());
        String teamAName = (teamA != null) ? teamA.getName() : "Equipo A";
        String teamBName = (teamB != null) ? teamB.getName() : "Equipo B";

        holder.tvMatchInfo.setText(teamAName + " vs " + teamBName + "\n" +
                m.getDate() + " " + m.getTime() + " en " + m.getPlace());

        if (listener != null) {
            holder.btnEdit.setOnClickListener(v -> listener.onEdit(m));
            holder.btnDelete.setOnClickListener(v -> listener.onDelete(m));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvMatchInfo;
        ImageButton btnEdit, btnDelete;

        VH(View v) {
            super(v);
            tvMatchInfo = v.findViewById(R.id.tvMatchInfo);
            btnEdit = v.findViewById(R.id.btnEditMatch);
            btnDelete = v.findViewById(R.id.btnDeleteMatch);
        }
    }
}
