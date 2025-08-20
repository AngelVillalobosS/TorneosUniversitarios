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
import com.example.torneouniversitario.adapters.PlayersByTeamAdapter;
import com.example.torneouniversitario.db.DBHelper;
import com.example.torneouniversitario.models.PlayerWithTeam;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class ReportPlayersByTeamFragment extends Fragment {

    private RecyclerView recyclerView;
    private PlayersByTeamAdapter adapter;
    private DBHelper dbHelper;
    private Button btnExportPDF;
    private List<PlayerWithTeam> playersList;

    private static final int STORAGE_PERMISSION_CODE = 100;

    public ReportPlayersByTeamFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report_players_by_team, container, false);

        recyclerView = view.findViewById(R.id.recyclerPlayersByTeam);
        btnExportPDF = view.findViewById(R.id.btnExportPlayersByTeamPDF);
        dbHelper = new DBHelper(getContext());

        playersList = dbHelper.getPlayersByTeamReport();
        adapter = new PlayersByTeamAdapter(getContext(), playersList);
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
                    + "/Reporte_Jugadores_Por_Equipo.pdf";
            PdfWriter.getInstance(document, new FileOutputStream(new File(filePath)));
            document.open();

            document.add(new Paragraph("Reporte de Jugadores por Equipo\n\n"));

            PdfPTable table = new PdfPTable(4);
            table.addCell("Equipo");
            table.addCell("Jugador");
            table.addCell("Número");
            table.addCell("Posición");

            for (PlayerWithTeam p : playersList) {
                table.addCell(p.getTeamName());
                table.addCell(p.getPlayerName());
                table.addCell(p.getPlayerNumber());
                table.addCell(p.getPosition());
            }

            document.add(table);
            document.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Manejo del permiso de almacenamiento
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
