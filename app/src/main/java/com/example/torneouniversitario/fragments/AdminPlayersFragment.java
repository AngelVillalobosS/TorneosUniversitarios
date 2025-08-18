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
import com.example.torneouniversitario.adapters.PlayerAdapter;
import com.example.torneouniversitario.db.DBHelper;
import com.example.torneouniversitario.models.Player;
import com.example.torneouniversitario.models.Team;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class AdminPlayersFragment extends Fragment implements PlayerAdapter.OnPlayerActionListener {

    DBHelper db;
    RecyclerView rv;
    PlayerAdapter adapter;
    FloatingActionButton fabAdd;
    List<Team> teams;

    public AdminPlayersFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_admin_players, container, false);
        db = new DBHelper(getContext());
        rv = v.findViewById(R.id.rvPlayers);
        fabAdd = v.findViewById(R.id.fabAddPlayer);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        loadPlayers();

        fabAdd.setOnClickListener(view -> showAddDialog());

        return v;
    }

    private void loadPlayers() {
        // Por simplicidad listamos todos los jugadores
        // (puedes modificar para filtrar por equipo si lo deseas)
        teams = db.getAllTeams();
        List<Player> players = db.getPlayersByTeam(-1); // Ajustar si filtras por equipo
        adapter = new PlayerAdapter(players, this);
        rv.setAdapter(adapter);
    }

    private void showAddDialog() {
        View dialogV = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_player, null);
        EditText etName = dialogV.findViewById(R.id.etPlayerName);
        EditText etPos = dialogV.findViewById(R.id.etPlayerPosition);
        EditText etNum = dialogV.findViewById(R.id.etPlayerNumber);
        Spinner spTeams = dialogV.findViewById(R.id.spTeams);

        List<Team> teamList = db.getAllTeams();
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item);
        for (Team t : teamList) spinnerAdapter.add(t.getName());
        spTeams.setAdapter(spinnerAdapter);

        new AlertDialog.Builder(getContext())
                .setTitle("Agregar jugador")
                .setView(dialogV)
                .setPositiveButton("Guardar", (d, w) -> {
                    String name = etName.getText().toString().trim();
                    String pos = etPos.getText().toString().trim();
                    String numStr = etNum.getText().toString().trim();
                    if (name.isEmpty() || pos.isEmpty() || numStr.isEmpty() || teamList.isEmpty()) {
                        Toast.makeText(getContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    int number = Integer.parseInt(numStr);
                    int teamId = teamList.get(spTeams.getSelectedItemPosition()).getId();
                    db.insertPlayer(new Player(name, pos, number, teamId));
                    loadPlayers();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    @Override
    public void onEdit(Player p) {
        View dialogV = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_player, null);
        EditText etName = dialogV.findViewById(R.id.etPlayerName);
        EditText etPos = dialogV.findViewById(R.id.etPlayerPosition);
        EditText etNum = dialogV.findViewById(R.id.etPlayerNumber);
        Spinner spTeams = dialogV.findViewById(R.id.spTeams);

        List<Team> teamList = db.getAllTeams();
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item);
        for (Team t : teamList) spinnerAdapter.add(t.getName());
        spTeams.setAdapter(spinnerAdapter);

        etName.setText(p.getName());
        etPos.setText(p.getPosition());
        etNum.setText(String.valueOf(p.getNumber()));

        new AlertDialog.Builder(getContext())
                .setTitle("Editar jugador")
                .setView(dialogV)
                .setPositiveButton("Guardar", (d, w) -> {
                    etName.getText().toString().trim();
                    etPos.getText().toString().trim();
                    Integer.parseInt(etNum.getText().toString().trim());
                    spTeams.getSelectedItemPosition();
                    // No hemos implementado updatePlayer en DBHelper; puedes añadirlo igual que updateTeam
                    Toast.makeText(getContext(), "Actualizar jugador en DBHelper", Toast.LENGTH_SHORT).show();
                    loadPlayers();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    @Override
    public void onDelete(Player p) {
        Toast.makeText(getContext(), "Eliminar jugador en DBHelper", Toast.LENGTH_SHORT).show();
    }
}
