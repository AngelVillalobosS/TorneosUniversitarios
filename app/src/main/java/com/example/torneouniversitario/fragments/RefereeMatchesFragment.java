package com.example.torneouniversitario.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.torneouniversitario.R;
import com.example.torneouniversitario.db.DBHelper;
import com.example.torneouniversitario.models.MatchModel;

import java.util.List;

public class RefereeMatchesFragment extends Fragment {
    DBHelper db;
    ListView lv;
    List<MatchModel> matches;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle s) {
        View v = inflater.inflate(R.layout.fragment_referee_matches, container, false);
        db = new DBHelper(getContext());
        lv = v.findViewById(R.id.lvRefMatches);
        matches = db.getAllMatches();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1);
        for (MatchModel m : matches)
            adapter.add("Partido " + m.getId() + " - " + m.getDate() + " " + m.getTime());
        lv.setAdapter(adapter);

        lv.setOnItemClickListener((parent, view, position, id) -> {
            MatchModel selected = matches.get(position);
            // abrir fragmento para registrar eventos (pasa id)
            RefereeEventFragment frag = RefereeEventFragment.newInstance(selected.getId());
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, frag)
                    .addToBackStack(null)
                    .commit();
        });

        return v;
    }
}
