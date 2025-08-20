package com.example.torneouniversitario.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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

import java.util.Calendar;
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

        fabAdd.setOnClickListener(view -> showAddMatchDialog());

        return v;
    }

    private void loadMatches() {
        List<MatchModel> matches = db.getAllMatches();
        adapter = new MatchAdapter(matches, this, db);
        rv.setAdapter(adapter);
    }

    private void showAddMatchDialog() {
        View dialogV = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_match, null);

        Spinner spTeamA = dialogV.findViewById(R.id.spTeamA);
        Spinner spTeamB = dialogV.findViewById(R.id.spTeamB);
        EditText etDate = dialogV.findViewById(R.id.etMatchDate);
        EditText etTime = dialogV.findViewById(R.id.etMatchTime);
        EditText etPlace = dialogV.findViewById(R.id.etMatchPlace);
        Button btnCancel = dialogV.findViewById(R.id.btnCancelMatch);
        Button btnSave = dialogV.findViewById(R.id.btnSaveMatch);

        List<Team> teams = db.getAllTeams();

        ArrayAdapter<Team> teamAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, teams) {
            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                TextView tv = (TextView) super.getView(position, convertView, parent);
                tv.setText(getItem(position).getName());
                return tv;
            }

            @Override
            public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
                TextView tv = (TextView) super.getDropDownView(position, convertView, parent);
                tv.setText(getItem(position).getName());
                return tv;
            }
        };

        teamAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTeamA.setAdapter(teamAdapter);
        spTeamB.setAdapter(teamAdapter);

        // ------------------- DATE PICKER -------------------
        etDate.setFocusable(false);
        etDate.setClickable(true);
        etDate.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        selectedMonth += 1;
                        String formattedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth, selectedDay);
                        etDate.setText(formattedDate);
                    }, year, month, day);
            datePickerDialog.show();
        });

        // ------------------- TIME PICKER -------------------
        etTime.setFocusable(false);
        etTime.setClickable(true);
        etTime.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(),
                    (view, selectedHour, selectedMinute) -> {
                        String formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute);
                        etTime.setText(formattedTime);
                    }, hour, minute, true);
            timePickerDialog.show();
        });

        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setView(dialogV)
                .create();

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnSave.setOnClickListener(v -> {
            Team teamA = (Team) spTeamA.getSelectedItem();
            Team teamB = (Team) spTeamB.getSelectedItem();
            String date = etDate.getText().toString().trim();
            String time = etTime.getText().toString().trim();
            String place = etPlace.getText().toString().trim();
            String status = ("Pendiente");

            if (teamA == null || teamB == null || date.isEmpty() || time.isEmpty() || place.isEmpty()) {
                Toast.makeText(getContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            if (teamA.getId() == teamB.getId()) {
                Toast.makeText(getContext(), "El Equipo A y Equipo B no pueden ser el mismo", Toast.LENGTH_SHORT).show();
                return;
            }

            MatchModel match = new MatchModel(teamA.getId(), teamB.getId(), date, time, place);
            long id = db.insertMatch(match);

            if (id > 0) {
                Toast.makeText(getContext(), "Partido agregado", Toast.LENGTH_SHORT).show();
                loadMatches();
                dialog.dismiss();
            } else {
                Toast.makeText(getContext(), "Error al agregar el partido", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    private void showEditMatchDialog(MatchModel match) {
        View dialogV = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_match, null);

        Spinner spTeamA = dialogV.findViewById(R.id.spTeamA);
        Spinner spTeamB = dialogV.findViewById(R.id.spTeamB);
        EditText etDate = dialogV.findViewById(R.id.etMatchDate);
        EditText etTime = dialogV.findViewById(R.id.etMatchTime);
        EditText etPlace = dialogV.findViewById(R.id.etMatchPlace);
        Button btnCancel = dialogV.findViewById(R.id.btnCancelMatch);
        Button btnSave = dialogV.findViewById(R.id.btnSaveMatch);

        List<Team> teams = db.getAllTeams();

        ArrayAdapter<Team> teamAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, teams) {
            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                TextView tv = (TextView) super.getView(position, convertView, parent);
                tv.setText(getItem(position).getName());
                return tv;
            }

            @Override
            public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
                TextView tv = (TextView) super.getDropDownView(position, convertView, parent);
                tv.setText(getItem(position).getName());
                return tv;
            }
        };

        teamAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTeamA.setAdapter(teamAdapter);
        spTeamB.setAdapter(teamAdapter);

        for (int i = 0; i < teams.size(); i++) {
            if (teams.get(i).getId() == match.getTeamAId()) spTeamA.setSelection(i);
            if (teams.get(i).getId() == match.getTeamBId()) spTeamB.setSelection(i);
        }

        etDate.setText(match.getDate());
        etTime.setText(match.getTime());
        etPlace.setText(match.getPlace());

        // ------------------- DATE PICKER -------------------
        etDate.setFocusable(false);
        etDate.setClickable(true);
        etDate.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        selectedMonth += 1;
                        String formattedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth, selectedDay);
                        etDate.setText(formattedDate);
                    }, year, month, day);
            datePickerDialog.show();
        });

        // ------------------- TIME PICKER -------------------
        etTime.setFocusable(false);
        etTime.setClickable(true);
        etTime.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(),
                    (view, selectedHour, selectedMinute) -> {
                        String formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute);
                        etTime.setText(formattedTime);
                    }, hour, minute, true);
            timePickerDialog.show();
        });

        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setView(dialogV)
                .create();

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnSave.setOnClickListener(v -> {
            Team teamA = (Team) spTeamA.getSelectedItem();
            Team teamB = (Team) spTeamB.getSelectedItem();
            String date = etDate.getText().toString().trim();
            String time = etTime.getText().toString().trim();
            String place = etPlace.getText().toString().trim();

            if (teamA == null || teamB == null || date.isEmpty() || time.isEmpty() || place.isEmpty()) {
                Toast.makeText(getContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            if (teamA.getId() == teamB.getId()) {
                Toast.makeText(getContext(), "El Equipo A y Equipo B no pueden ser el mismo", Toast.LENGTH_SHORT).show();
                return;
            }

            MatchModel updatedMatch = new MatchModel(
                    match.getId(),
                    teamA.getId(),
                    teamB.getId(),
                    date,
                    time,
                    place
            );

            db.updateMatch(updatedMatch);
            loadMatches();
            Toast.makeText(getContext(), "Partido actualizado", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        dialog.show();
    }

    private void deleteMatch(MatchModel match) {
        Team teamA = db.getTeamById(match.getTeamAId());
        Team teamB = db.getTeamById(match.getTeamBId());

        String teamAName = (teamA != null) ? teamA.getName() : "Equipo A";
        String teamBName = (teamB != null) ? teamB.getName() : "Equipo B";

        new AlertDialog.Builder(getContext())
                .setTitle("Eliminar Partido")
                .setMessage("¿Estás seguro de que quieres eliminar el partido " + teamAName + " vs " + teamBName + "?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    db.deleteMatch(match.getId());
                    loadMatches();
                    Toast.makeText(getContext(), "Partido eliminado", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }

    @Override
    public void onEdit(MatchModel match) {
        showEditMatchDialog(match);
    }

    @Override
    public void onDelete(MatchModel match) {
        deleteMatch(match);
    }
}

