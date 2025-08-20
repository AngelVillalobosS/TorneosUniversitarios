package com.example.torneouniversitario.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.torneouniversitario.R;
import com.example.torneouniversitario.adapters.TeamStatsAdapter;
import com.example.torneouniversitario.db.DBHelper;
import com.example.torneouniversitario.models.TeamStats;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class ReportTeamStatsFragment extends Fragment {

    private RecyclerView recyclerView;
    private TeamStatsAdapter adapter;
    private DBHelper dbHelper;
    private Button btnExportPDF;
    private List<TeamStats> teamStatsList;
    private static final int STORAGE_PERMISSION_CODE = 101;

    public ReportTeamStatsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report_team_stats, container, false);

        recyclerView = view.findViewById(R.id.recyclerTeamStats);
        btnExportPDF = view.findViewById(R.id.btnExportTeamStatsPDF);
        dbHelper = new DBHelper(getContext());

        teamStatsList = dbHelper.getTeamsWithStats();
        adapter = new TeamStatsAdapter(getContext(), teamStatsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        btnExportPDF.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(getContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
            } else {
                exportToPDF();
            }
        });

        return view;
    }

    private void exportToPDF() {
        try {
            Document document = new Document();
            String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    + "/Reporte_Equipos_Stats.pdf";
            PdfWriter.getInstance(document, new FileOutputStream(new File(filePath)));
            document.open();

            document.add(new Paragraph("Reporte de Datos de Equipos con Estadísticas\n\n"));

            PdfPTable table = new PdfPTable(5);
            table.addCell("Equipo");
            table.addCell("Partidos");
            table.addCell("Goles Favor");
            table.addCell("Goles Contra");
            table.addCell("Puntaje");

            for (TeamStats t : teamStatsList) {
                table.addCell(t.getTeamName());
                table.addCell(String.valueOf(t.getMatchesPlayed()));
                table.addCell(String.valueOf(t.getGoalsFor()));
                table.addCell(String.valueOf(t.getGoalsAgainst()));
                table.addCell(String.valueOf(t.getPoints()));
            }

            document.add(table);
            document.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                exportToPDF();
            }
        }
    }
}
