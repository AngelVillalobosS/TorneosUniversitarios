package com.example.torneouniversitario.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.TextView;
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

    public AdminTeamsFragment() { }

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
        Spinner spinnerSports = dialogV.findViewById(R.id.spinnerSports);

        List<String> sportsList = db.getAllSports();
        ArrayAdapter<String> sportAdapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_item,
                sportsList
        );
        sportAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSports.setAdapter(sportAdapter);

        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setView(dialogV)
                .setCancelable(true)
                .create();

        dialog.show();

        dialogV.findViewById(R.id.btnSaveTeam).setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String sport = (String) spinnerSports.getSelectedItem();

            if (name.isEmpty() || sport == null) {
                Toast.makeText(getContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show();
            } else {
                long id = db.insertTeam(new Team(name, sport));
                if (id > 0) {
                    Toast.makeText(getContext(), "Equipo agregado", Toast.LENGTH_SHORT).show();
                    loadTeams();
                    dialog.dismiss();
                }
            }
        });

        dialogV.findViewById(R.id.btnCancelTeam).setOnClickListener(v -> dialog.dismiss());
    }

    @Override
    public void onEdit(Team t) {
        View dialogV = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_team, null);
        TextView title = dialogV.findViewById(R.id.tvDialogTitle);
        EditText etName = dialogV.findViewById(R.id.etTeamName);
        Spinner spinnerSports = dialogV.findViewById(R.id.spinnerSports);

        title.setText("Editar Equipo");
        etName.setText(t.getName());

        List<String> sportsList = db.getAllSports();
        ArrayAdapter<String> sportAdapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_item,
                sportsList
        );
        sportAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSports.setAdapter(sportAdapter);

        int pos = sportsList.indexOf(t.getSport());
        if (pos >= 0) spinnerSports.setSelection(pos);

        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setView(dialogV)
                .setCancelable(true)
                .create();

        dialog.show();

        dialogV.findViewById(R.id.btnSaveTeam).setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String sport = (String) spinnerSports.getSelectedItem();

            if (name.isEmpty() || sport == null) {
                Toast.makeText(getContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show();
            } else {
                t.setName(name);
                t.setSport(sport);
                db.updateTeam(t);
                loadTeams();
                Toast.makeText(getContext(), "Equipo actualizado", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        dialogV.findViewById(R.id.btnCancelTeam).setOnClickListener(v -> dialog.dismiss());
    }

    @Override
    public void onDelete(Team t) {
        View dialogV = LayoutInflater.from(getContext()).inflate(R.layout.dialog_confirm_delete, null);
        ((TextView) dialogV.findViewById(R.id.tvDeleteMessage))
                .setText("¿Seguro que deseas eliminar a " + t.getName() + "?");

        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setView(dialogV)
                .setCancelable(true)
                .create();

        dialog.show();

        dialogV.findViewById(R.id.btnDelete).setOnClickListener(v -> {
            db.deleteTeam(t.getId());
            loadTeams();
            Toast.makeText(getContext(), "Equipo eliminado", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        dialogV.findViewById(R.id.btnCancel).setOnClickListener(v -> dialog.dismiss());
    }

}
