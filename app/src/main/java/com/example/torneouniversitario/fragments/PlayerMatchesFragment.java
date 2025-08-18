package com.example.torneouniversitario.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.torneouniversitario.R;
import com.example.torneouniversitario.db.DBHelper;
import com.example.torneouniversitario.models.MatchModel;

import java.util.List;

public class PlayerMatchesFragment extends Fragment {

    DBHelper db;
    ListView lv;

    public PlayerMatchesFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_player_matches, container, false);
        db = new DBHelper(getContext());
        lv = v.findViewById(R.id.lvPlayerMatches);

        // Demo: lista todos los partidos
        List<MatchModel> matches = db.getAllMatches();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1);
        for (MatchModel m : matches) {
            adapter.add("Partido: " + m.getDate() + " " + m.getTime() + " en " + m.getPlace());
        }
        lv.setAdapter(adapter);

        return v;
    }
}
