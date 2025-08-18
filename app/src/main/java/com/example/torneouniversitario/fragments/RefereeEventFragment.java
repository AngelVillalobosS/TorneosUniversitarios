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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle s){
        View v = inflater.inflate(R.layout.fragment_referee_event, container, false);
        db = new DBHelper(getContext());
        if (getArguments() != null) matchId = getArguments().getInt(ARG_MATCH_ID);

        Spinner spType = v.findViewById(R.id.spEventType);
        EditText etPlayerId = v.findViewById(R.id.etPlayerId);
        EditText etMinute = v.findViewById(R.id.etMinute);
        Button btnRegister = v.findViewById(R.id.btnRegisterEvent);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, new String[]{"GOAL","FOUL","YELLOW","RED"});
        spType.setAdapter(adapter);

        btnRegister.setOnClickListener(view -> {
            String type = spType.getSelectedItem().toString();
            int playerId = Integer.parseInt(etPlayerId.getText().toString().trim());
            int minute = Integer.parseInt(etMinute.getText().toString().trim());
            EventModel e = new EventModel(matchId, playerId, type, minute, 0);
            db.insertEvent(e);
            Toast.makeText(getContext(), "Evento registrado", Toast.LENGTH_SHORT).show();
        });

        return v;
    }
}
