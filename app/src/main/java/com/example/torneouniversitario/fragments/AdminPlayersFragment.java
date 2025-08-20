package com.example.torneouniversitario.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

    public AdminPlayersFragment() {
    }

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
        List<Player> players = db.getAllPlayers();
        adapter = new PlayerAdapter(players, this);
        rv.setAdapter(adapter);
    }

    private void showAddDialog() {
        // Inflar el layout personalizado UNA sola vez
        View dialogV = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_player, null);

        EditText etName = dialogV.findViewById(R.id.etPlayerName);
        EditText etPos = dialogV.findViewById(R.id.etPlayerPosition);
        EditText etNum = dialogV.findViewById(R.id.etPlayerNumber);
        Spinner spTeams = dialogV.findViewById(R.id.spTeams);
        Button btnSave = dialogV.findViewById(R.id.btnSavePlayer); // Debe estar en tu layout
        Button btnCancel = dialogV.findViewById(R.id.btnCancelPlayer); // Opcional

        // Cargar lista de equipos en el Spinner
        List<Team> teamList = db.getAllTeams();
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        for (Team t : teamList) spinnerAdapter.add(t.getName());
        spTeams.setAdapter(spinnerAdapter);

        // Crear el AlertDialog
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setView(dialogV)
                .setCancelable(true)
                .create();

        dialog.show();

        // Botón Guardar
        btnSave.setOnClickListener(v -> {
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
            dialog.dismiss();
        });

        // Botón Cancelar opcional
        if (btnCancel != null) {
            btnCancel.setOnClickListener(v -> dialog.dismiss());
        }
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
                    String name = etName.getText().toString().trim();
                    String pos = etPos.getText().toString().trim();
                    int num = Integer.parseInt(etNum.getText().toString().trim());
                    int teamId = teamList.get(spTeams.getSelectedItemPosition()).getId();

                    Player updated = new Player(p.getId(), name, pos, num, teamId);
                    db.updatePlayer(updated);
                    loadPlayers();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    @Override
    public void onDelete(Player p) {
        new AlertDialog.Builder(getContext())
                .setTitle("Eliminar jugador")
                .setMessage("¿Seguro que deseas eliminar a " + p.getName() + "?")
                .setPositiveButton("Sí", (d, w) -> {
                    db.deletePlayer(p.getId());
                    loadPlayers();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}
