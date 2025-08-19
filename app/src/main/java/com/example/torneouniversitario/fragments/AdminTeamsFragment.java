package com.example.torneouniversitario.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.torneouniversitario.R;
import com.example.torneouniversitario.adapters.TeamAdapter;
import com.example.torneouniversitario.db.DBHelper;
import com.example.torneouniversitario.models.Team;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class AdminTeamsFragment extends Fragment implements TeamAdapter.OnTeamActionListener {

    DBHelper db;
    RecyclerView rv;
    TeamAdapter adapter;
    FloatingActionButton fabAdd;

    public AdminTeamsFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_admin_teams, container, false);
        db = new DBHelper(getContext());
        rv = v.findViewById(R.id.rvTeams);
        fabAdd = v.findViewById(R.id.fabAddTeam);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        loadTeams();

        fabAdd.setOnClickListener(view -> showAddDialog());

        return v;
    }

    private void loadTeams() {
        List<Team> teams = db.getAllTeams();
        adapter = new TeamAdapter(teams, this);
        rv.setAdapter(adapter);
    }

    private void showAddDialog() {
        View dialogV = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_team, null);
        EditText etName = dialogV.findViewById(R.id.etTeamName);
        EditText etSport = dialogV.findViewById(R.id.etTeamSport);

        new AlertDialog.Builder(getContext())
                .setTitle("Agregar equipo")
                .setView(dialogV)
                .setPositiveButton("Guardar", (d, w) -> {
                    String name = etName.getText().toString().trim();
                    String sport = etSport.getText().toString().trim();
                    if (name.isEmpty() || sport.isEmpty()) {
                        Toast.makeText(getContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    long id = db.insertTeam(new Team(name, sport));
                    if (id > 0) {
                        Toast.makeText(getContext(), "Equipo agregado", Toast.LENGTH_SHORT).show();
                        loadTeams();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    @Override
    public void onEdit(Team t) {
        View dialogV = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_team, null);
        EditText etName = dialogV.findViewById(R.id.etTeamName);
        EditText etSport = dialogV.findViewById(R.id.etTeamSport);
        etName.setText(t.getName());
        etSport.setText(t.getSport());

        new AlertDialog.Builder(getContext())
                .setTitle("Editar equipo")
                .setView(dialogV)
                .setPositiveButton("Guardar", (d, w) -> {
                    t.setName(etName.getText().toString().trim());
                    t.setSport(etSport.getText().toString().trim());
                    db.updateTeam(t);
                    loadTeams();
                    Toast.makeText(getContext(), "Equipo actualizado", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    @Override
    public void onDelete(Team t) {
        new AlertDialog.Builder(getContext())
                .setTitle("Confirmar eliminación")
                .setMessage("Eliminar equipo " + t.getName() + " ?")
                .setPositiveButton("Eliminar", (d, w) -> {
                    db.deleteTeam(t.getId());
                    loadTeams();
                    Toast.makeText(getContext(), "Equipo eliminado", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}
