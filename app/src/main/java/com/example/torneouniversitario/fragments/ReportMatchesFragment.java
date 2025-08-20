package com.example.torneouniversitario.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.view.*;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.*;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.torneouniversitario.R;
import com.example.torneouniversitario.adapters.MatchAdapter;
import com.example.torneouniversitario.db.DBHelper;
import com.example.torneouniversitario.models.MatchModel;

import java.io.*;
import java.util.List;

public class ReportMatchesFragment extends Fragment {
    private RecyclerView recyclerView;
    private DBHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_report_matches, container, false);

        recyclerView = root.findViewById(R.id.recyclerMatches);
        Button btnExport = root.findViewById(R.id.btnExportPdf);

        dbHelper = new DBHelper(getContext());
        List<MatchModel> matches = dbHelper.getMatchesFinished();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new MatchAdapter(matches, null));

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

        File pdfFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Reporte_Partidos.pdf");
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
