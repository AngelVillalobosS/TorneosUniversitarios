package com.example.torneouniversitario.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.torneouniversitario.R;
import com.example.torneouniversitario.adapters.MatchAdapter;
import com.example.torneouniversitario.db.DBHelper;
import com.example.torneouniversitario.models.MatchModel;
import com.example.torneouniversitario.models.Team;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class AdminMatchesFragment extends Fragment implements MatchAdapter.OnMatchActionListener {

    DBHelper db;
    RecyclerView rv;
    MatchAdapter adapter;
    FloatingActionButton fabAdd;

    public AdminMatchesFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_admin_matches, container, false);
        db = new DBHelper(getContext());
        rv = v.findViewById(R.id.rvMatches);
        fabAdd = v.findViewById(R.id.fabAddMatch);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        loadMatches();

        fabAdd.setOnClickListener(view -> showAddDialog());

        return v;
    }

    private void loadMatches() {
        List<MatchModel> matches = db.getAllMatches();
        adapter = new MatchAdapter(matches, this);
        rv.setAdapter(adapter);
    }

    private void showAddDialog() {
        View dialogV = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_match, null);
        Spinner spTeamA = dialogV.findViewById(R.id.spTeamA);
        Spinner spTeamB = dialogV.findViewById(R.id.spTeamB);
        EditText etDate = dialogV.findViewById(R.id.etMatchDate);
        EditText etTime = dialogV.findViewById(R.id.etMatchTime);
        EditText etPlace = dialogV.findViewById(R.id.etMatchPlace);

        List<Team> teamList = db.getAllTeams();
        ArrayAdapter<String> adapterTeams = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item);
        for (Team t : teamList) adapterTeams.add(t.getName());
        spTeamA.setAdapter(adapterTeams);
        spTeamB.setAdapter(adapterTeams);

        new AlertDialog.Builder(getContext())
                .setTitle("Agregar partido")
                .setView(dialogV)
                .setPositiveButton("Guardar", (d, w) -> {
                    int teamAId = teamList.get(spTeamA.getSelectedItemPosition()).getId();
                    int teamBId = teamList.get(spTeamB.getSelectedItemPosition()).getId();
                    String date = etDate.getText().toString().trim();
                    String time = etTime.getText().toString().trim();
                    String place = etPlace.getText().toString().trim();
                    if (date.isEmpty() || time.isEmpty() || place.isEmpty()) {
                        Toast.makeText(getContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    db.insertMatch(new MatchModel(teamAId, teamBId, date, time, place));
                    loadMatches();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    @Override
    public void onEdit(MatchModel m) {
        Toast.makeText(getContext(), "Editar partido (implementa en DBHelper.updateMatch)", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDelete(MatchModel m) {
        Toast.makeText(getContext(), "Eliminar partido (implementa en DBHelper.deleteMatch)", Toast.LENGTH_SHORT).show();
    }
}
