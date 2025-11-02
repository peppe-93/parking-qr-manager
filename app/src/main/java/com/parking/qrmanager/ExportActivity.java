package com.parking.qrmanager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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
    
    private static final int STORAGE_PERMISSION_CODE = 101;
    
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
                checkStoragePermission();
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
    
    private void checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) 
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, 
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        } else {
            exportToCSV();
        }
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                exportToCSV();
            } else {
                Toast.makeText(this, "Permesso di scrittura negato", Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    private void exportToCSV() {
        List<DatabaseHelper.Scan> scans = dbHelper.getAllScans();
        
        if (scans.isEmpty()) {
            Toast.makeText(this, "Nessun dato da esportare", Toast.LENGTH_SHORT).show();
            return;
        }
        
        try {
            // Crea file CSV
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
            String fileName = "scansioni_" + sdf.format(new Date()) + ".csv";
            
            File exportDir = new File(getExternalFilesDir(null), "exports");
            if (!exportDir.exists()) {
                exportDir.mkdirs();
            }
            
            File csvFile = new File(exportDir, fileName);
            FileWriter writer = new FileWriter(csvFile);
            
            // Header CSV
            writer.append("ID,QR_Code,Area,Postazione,Sessione,Data,Ora,Vuota\n");
            
            // Dati
            for (DatabaseHelper.Scan scan : scans) {
                writer.append(String.valueOf(scan.id)).append(",");
                writer.append(scan.qrCode).append(",");
                writer.append(scan.area).append(",");
                writer.append(String.valueOf(scan.spotNumber)).append(",");
                writer.append(scan.sessionId).append(",");
                writer.append(scan.date).append(",");
                writer.append(scan.time).append(",");
                writer.append(scan.isEmpty ? "SI" : "NO").append("\n");
            }
            
            writer.flush();
            writer.close();
            
            // Mostra dialog con opzioni
            showExportSuccessDialog(csvFile);
            
        } catch (IOException e) {
            Toast.makeText(this, "Errore nell'esportazione: " + e.getMessage(), 
                         Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
    
    private void showExportSuccessDialog(File csvFile) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Esportazione Completata");
        builder.setMessage("File esportato con successo:\n" + csvFile.getName() + 
                          "\n\nVuoi condividere il file?");
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
        Uri fileUri = FileProvider.getUriForFile(this, 
            "com.parking.qrmanager.fileprovider", csvFile);
        
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/csv");
        shareIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        
        startActivity(Intent.createChooser(shareIntent, "Condividi CSV"));
    }
    
    private void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Elimina Dati Vecchi");
        builder.setMessage("Vuoi eliminare le scansioni più vecchie di 30 giorni?\n\n" +
                          "Questa operazione è irreversibile!");
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
        // Calcola data 30 giorni fa
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        long thirtyDaysAgo = System.currentTimeMillis() - (30L * 24 * 60 * 60 * 1000);
        String cutoffDate = sdf.format(new Date(thirtyDaysAgo));
        
        int deletedRows = dbHelper.deleteOldScans(cutoffDate);
        
        Toast.makeText(this, "Eliminate " + deletedRows + " scansioni", Toast.LENGTH_SHORT).show();
        updateInfo();
    }
}
