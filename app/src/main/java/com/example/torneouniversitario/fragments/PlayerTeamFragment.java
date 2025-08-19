package com.example.torneouniversitario.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.torneouniversitario.R;
import com.example.torneouniversitario.adapters.PlayerTeamAdapter;
import com.example.torneouniversitario.db.DBHelper;
import com.example.torneouniversitario.models.Player;
import com.example.torneouniversitario.models.Team;

import java.util.List;

public class PlayerTeamFragment extends Fragment {

    DBHelper db;
    RecyclerView rv;
    TextView tvTeamName;

    public PlayerTeamFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_player_team, container, false);
        db = new DBHelper(getContext());
        rv = v.findViewById(R.id.rvPlayerTeam);
        tvTeamName = v.findViewById(R.id.tvTeamNamePlayer);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        // De momento cargamos todos los jugadores del primer equipo (demo)
        List<Team> teams = db.getAllTeams();
        if (!teams.isEmpty()) {
            Team t = teams.get(0);
            tvTeamName.setText(t.getName() + " (" + t.getSport() + ")");
            List<Player> players = db.getPlayersByTeam(t.getId());
            rv.setAdapter(new PlayerTeamAdapter(players));
        }

        return v;
    }
}
