package com.example.torneouniversitario.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.fragment.app.Fragment;

import com.example.torneouniversitario.R;
import com.example.torneouniversitario.db.DBHelper;
import com.example.torneouniversitario.models.EventModel;
import com.example.torneouniversitario.models.Player;

import java.util.List;

public class RefereeEventFragment extends Fragment {

    private static final String ARG_MATCH_ID = "match_id";
    int matchId;
    DBHelper db;

    public static RefereeEventFragment newInstance(int matchId) {
        RefereeEventFragment f = new RefereeEventFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_MATCH_ID, matchId);
        f.setArguments(b);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle s) {
        View v = inflater.inflate(R.layout.fragment_referee_event, container, false);
        db = new DBHelper(getContext());
        if (getArguments() != null) matchId = getArguments().getInt(ARG_MATCH_ID);

        Spinner spType = v.findViewById(R.id.spEventType);
        Spinner spPlayer = v.findViewById(R.id.spPlayer); // nuevo spinner para jugadores
        EditText etMinute = v.findViewById(R.id.etMinute);
        Button btnRegister = v.findViewById(R.id.btnRegisterEvent);

        // --- Configurar spinner de tipos de evento ---
        ArrayAdapter<String> adapterType = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_item,
                new String[]{"GOL", "FALTA", "AMARILLA", "ROJA"}
        );
        adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spType.setAdapter(adapterType);

        // --- Cargar jugadores de los equipos del partido ---
        List<Player> players = db.getPlayersByMatch(matchId); // Necesitas este método en DBHelper
        ArrayAdapter<Player> adapterPlayers = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_item,
                players
        );
        adapterPlayers.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPlayer.setAdapter(adapterPlayers);

        // --- Botón registrar ---
        btnRegister.setOnClickListener(view -> {
            String type = spType.getSelectedItem().toString();

            // Validar minuto
            String minuteStr = etMinute.getText().toString().trim();
            if (minuteStr.isEmpty()) {
                Toast.makeText(getContext(), "Ingresa el minuto del evento", Toast.LENGTH_SHORT).show();
                return;
            }
            int minute = Integer.parseInt(minuteStr);
            if (minute > 70) {
                Toast.makeText(getContext(), "El tiempo no puede superar los 70 minutos", Toast.LENGTH_SHORT).show();
                return;
            }

            // Obtener jugador seleccionado
            Player selectedPlayer = (Player) spPlayer.getSelectedItem();
            if (selectedPlayer == null) {
                Toast.makeText(getContext(), "Selecciona un jugador", Toast.LENGTH_SHORT).show();
                return;
            }

            int playerId = selectedPlayer.getId();

            EventModel e = new EventModel(matchId, playerId, type, minute, 0);
            db.insertEvent(e);
            Toast.makeText(getContext(), "Evento registrado", Toast.LENGTH_SHORT).show();
        });

        return v;
    }
}
