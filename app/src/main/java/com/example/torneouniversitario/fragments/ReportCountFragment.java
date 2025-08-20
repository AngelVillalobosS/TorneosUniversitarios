package com.example.torneouniversitario.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.torneouniversitario.R;
import com.example.torneouniversitario.db.DBHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ReportCountFragment extends Fragment {

    private TextView tvResumen;
    private DBHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_report_count, container, false);

        tvResumen = root.findViewById(R.id.tvResumen);
        Button btnExport = root.findViewById(R.id.btnExportPdf);

        dbHelper = new DBHelper(getContext());

        int totalTeams = dbHelper.getTeamsCount();
        int totalPlayers = dbHelper.getPlayersCount();
        int totalMatches = dbHelper.getMatchesCount();
        int totalSports = dbHelper.getSportsCount();

        String report = "📊 Reporte de Conteo\n\n" +
                "Equipos: " + totalTeams + "\n" +
                "Jugadores: " + totalPlayers + "\n" +
                "Partidos: " + totalMatches + "\n" +
                "Deportes: " + totalSports;

        tvResumen.setText(report);

        btnExport.setOnClickListener(v -> exportToPdf(root, requireContext()));

        return root;
    }

    private void exportToPdf(View view, Context context) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(), 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);
        page.getCanvas().drawBitmap(bitmap, 0, 0, null);
        document.finishPage(page);

        File pdfDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File pdfFile = new File(pdfDir, "Reporte_Conteo.pdf");

        try {
            FileOutputStream fos = new FileOutputStream(pdfFile);
            document.writeTo(fos);
            document.close();
            fos.close();
            Toast.makeText(context, "PDF guardado en Descargas", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
