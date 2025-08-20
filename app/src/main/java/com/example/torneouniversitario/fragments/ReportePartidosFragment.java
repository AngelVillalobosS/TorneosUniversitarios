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
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.torneouniversitario.R;
import com.example.torneouniversitario.adapters.MatchReportAdapter;
import com.example.torneouniversitario.db.DBHelper;
import com.example.torneouniversitario.models.MatchModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ReportePartidosFragment extends Fragment {

    private LinearLayout reportContainer;
    private DBHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_reporte_partidos, container, false);

        reportContainer = root.findViewById(R.id.reportContainer);
        RecyclerView recyclerView = root.findViewById(R.id.recyclerView);
        Button btnExport = root.findViewById(R.id.btnExportPdf);

        dbHelper = new DBHelper(getContext());

        // 🔹 Aquí sacas los partidos de la BD
        // Antes: List<String>
        List<MatchModel> listaPartidos = dbHelper.getMatchesFinished();
        MatchReportAdapter adapter = new MatchReportAdapter(listaPartidos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        btnExport.setOnClickListener(v -> exportToPdf(reportContainer, requireContext()));

        return root;
    }

    private void exportToPdf(View view, Context context) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(), 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas pageCanvas = page.getCanvas();
        pageCanvas.drawBitmap(bitmap, 0, 0, null);
        document.finishPage(page);

        File pdfDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File pdfFile = new File(pdfDir, "Reporte_Partidos.pdf");

        try {
            FileOutputStream fos = new FileOutputStream(pdfFile);
            document.writeTo(fos);
            document.close();
            fos.close();
            Toast.makeText(context, "PDF guardado en Descargas", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error al guardar PDF: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
