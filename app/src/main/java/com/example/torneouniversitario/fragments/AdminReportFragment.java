package com.example.torneouniversitario.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.torneouniversitario.R;

public class AdminReportFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_admin_report, container, false);

        CardView cardConteo = root.findViewById(R.id.cardReporteConteo);
        CardView cardPartidos = root.findViewById(R.id.cardReportePartidos);
        CardView cardJugadores = root.findViewById(R.id.cardReporteJugadores);
        CardView cardEquipos = root.findViewById(R.id.cardReporteEquipos);

        cardConteo.setOnClickListener(v -> openFragment(new ReportCountFragment()));
        cardPartidos.setOnClickListener(v -> openFragment(new ReportePartidosFragment()));
        cardJugadores.setOnClickListener(v -> openFragment(new ReportPlayersByTeamFragment()));
        cardEquipos.setOnClickListener(v -> openFragment(new ReportTeamStatsFragment()));

        return root;
    }

    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
