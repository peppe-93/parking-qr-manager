package com.parking.qrmanager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ExportActivity extends AppCompatActivity {

    private TextView textExportInfo;
    private Button btnExportCSV;
    private Button btnDeleteOldData;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);

        dbHelper = new DatabaseHelper(this);

        textExportInfo = findViewById(R.id.textExportInfo);
        btnExportCSV = findViewById(R.id.btnExportCSV);
        btnDeleteOldData = findViewById(R.id.btnDeleteOldData);

        updateInfo();

        btnExportCSV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exportToCSV(); // scrive direttamente in getExternalFilesDir/exports senza permessi
            }
        });

        btnDeleteOldData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteDialog();
            }
        });
    }

    private void updateInfo() {
        List<DatabaseHelper.Scan> scans = dbHelper.getAllScans();
        textExportInfo.setText("Scansioni totali nel database: " + scans.size() + "\n\n" +
                "L'esportazione creerà un file CSV con tutte le scansioni.");
    }

    private void exportToCSV() {
        List<DatabaseHelper.Scan> scans = dbHelper.getAllScans();
        if (scans.isEmpty()) {
            Toast.makeText(this, "Nessun dato da esportare", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
            String fileName = "scansioni_" + sdf.format(new Date()) + ".csv";

            File exportDir = new File(getExternalFilesDir(null), "exports");
            if (!exportDir.exists()) exportDir.mkdirs();
            File csvFile = new File(exportDir, fileName);
            FileWriter writer = new FileWriter(csvFile);

            writer.append("ID,QR_Code,Area,Postazione,Sessione,Data,Ora,Vuota\n");
            for (DatabaseHelper.Scan scan : scans) {
                writer.append(String.valueOf(scan.id)).append(",")
                        .append(sanitize(scan.qrCode)).append(",")
                        .append(sanitize(scan.area)).append(",")
                        .append(String.valueOf(scan.spotNumber)).append(",")
                        .append(sanitize(scan.sessionId)).append(",")
                        .append(sanitize(scan.date)).append(",")
                        .append(sanitize(scan.time)).append(",")
                        .append(scan.isEmpty ? "SI" : "NO").append("\n");
            }
            writer.flush();
            writer.close();

            showExportSuccessDialog(csvFile);
        } catch (IOException e) {
            Toast.makeText(this, "Errore nell'esportazione: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private String sanitize(String s) {
        if (s == null) return "";
        String v = s.replace("\n", " ").replace("\r", " ");
        if (v.contains(",")) v = '"' + v.replace("\"", "\"\"") + '"';
        return v;
    }

    private void showExportSuccessDialog(File csvFile) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Esportazione Completata");
        builder.setMessage("File esportato con successo:\n" + csvFile.getName() + "\n\nVuoi condividere il file?");
        builder.setPositiveButton("Condividi", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                shareCSVFile(csvFile);
            }
        });
        builder.setNegativeButton("Chiudi", null);
        builder.show();
    }

    private void shareCSVFile(File csvFile) {
        Uri fileUri = FileProvider.getUriForFile(this, "com.parking.qrmanager.fileprovider", csvFile);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/csv");
        shareIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        if (shareIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(Intent.createChooser(shareIntent, "Condividi CSV"));
        } else {
            Toast.makeText(this, "Nessuna app disponibile per condividere", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Elimina Dati Vecchi");
        builder.setMessage("Vuoi eliminare le scansioni più vecchie di 30 giorni?\n\nQuesta operazione è irreversibile!");
        builder.setPositiveButton("Elimina", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteOldData();
            }
        });
        builder.setNegativeButton("Annulla", null);
        builder.show();
    }

    private void deleteOldData() {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());
        long thirtyDaysAgo = System.currentTimeMillis() - (30L * 24 * 60 * 60 * 1000);
        String cutoffDate = sdf.format(new Date(thirtyDaysAgo));
        int deletedRows = dbHelper.deleteOldScans(cutoffDate);
        Toast.makeText(this, "Eliminate " + deletedRows + " scansioni", Toast.LENGTH_SHORT).show();
        updateInfo();
    }
}
