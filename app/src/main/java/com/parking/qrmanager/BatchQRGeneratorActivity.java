package com.parking.qrmanager;

import android.content.Context;
import android.graphics.Bitmap;
import android.print.PrintAttributes;
import android.print.PrintManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BatchQRGeneratorActivity extends AppCompatActivity {
    private TextView textCompanyName; private EditText editQuantity; private Button btnGenerateBatch; private Button btnExportPDF; private ProgressBar progressBar; private TextView textStatus;
    private String companyName; private DatabaseHelper dbHelper; private List<QRCodeData> generatedQRCodes;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch_qr);
        dbHelper = new DatabaseHelper(this);
        generatedQRCodes = new ArrayList<>();
        textCompanyName = findViewById(R.id.textCompanyName);
        editQuantity = findViewById(R.id.editQuantity);
        btnGenerateBatch = findViewById(R.id.btnGenerateBatch);
        btnExportPDF = findViewById(R.id.btnExportPDF);
        progressBar = findViewById(R.id.progressBar);
        textStatus = findViewById(R.id.textStatus);
        companyName = getIntent().getStringExtra("COMPANY_NAME"); if (companyName == null) companyName = "AZIENDA"; textCompanyName.setText("Azienda: " + companyName);
        btnGenerateBatch.setOnClickListener(v -> generateBatchQRCodes());
        btnExportPDF.setOnClickListener(v -> exportViaPrintManager());
        btnExportPDF.setEnabled(false);
    }

    private void generateBatchQRCodes() {
        String quantityStr = editQuantity.getText().toString().trim(); if (quantityStr.isEmpty()) { Toast.makeText(this, "Inserisci la quantità", Toast.LENGTH_SHORT).show(); return; }
        int quantity = Integer.parseInt(quantityStr); if (quantity < 1 || quantity > 100) { Toast.makeText(this, "Quantità deve essere tra 1 e 100", Toast.LENGTH_SHORT).show(); return; }
        new AlertDialog.Builder(this).setTitle("Genera " + quantity + " QR Code").setMessage("Confermi la generazione di " + quantity + " QR Code per " + companyName + "?")
                .setPositiveButton("Genera", (d, w) -> performBatchGeneration(quantity)).setNegativeButton("Annulla", null).show();
    }

    private void performBatchGeneration(final int quantity) {
        progressBar.setVisibility(View.VISIBLE); textStatus.setVisibility(View.VISIBLE); btnGenerateBatch.setEnabled(false); generatedQRCodes.clear();
        new Thread(() -> {
            for (int i = 0; i < quantity; i++) {
                final int current = i + 1;
                String uniqueCode = generateSecureCode(); String qrCode = companyName + "_" + uniqueCode;
                try {
                    Bitmap qrBitmap = QRBitmapUtil.generateQRCodeWithText(qrCode, 400, 480);
                    generatedQRCodes.add(new QRCodeData(qrCode, qrBitmap));
                    dbHelper.insertQRCode(qrCode, companyName);
                    runOnUiThread(() -> textStatus.setText("Generati: " + current + " / " + quantity));
                    Thread.sleep(60);
                } catch (Exception ignored) {}
            }
            runOnUiThread(() -> { progressBar.setVisibility(View.GONE); textStatus.setText("✓ Generati " + quantity + " QR Code!"); btnGenerateBatch.setEnabled(true); btnExportPDF.setEnabled(true); Toast.makeText(BatchQRGeneratorActivity.this, "Generazione completata!", Toast.LENGTH_LONG).show(); });
        }).start();
    }

    private String generateSecureCode() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"; java.security.SecureRandom random = new java.security.SecureRandom(); StringBuilder code = new StringBuilder();
        for (int i = 0; i < 12; i++) code.append(chars.charAt(random.nextInt(chars.length())));
        code.append(Long.toHexString(System.currentTimeMillis()).toUpperCase()); return code.toString();
    }

    private void exportViaPrintManager() {
        if (generatedQRCodes == null || generatedQRCodes.isEmpty()) { Toast.makeText(this, "Genera prima i QR Code", Toast.LENGTH_SHORT).show(); return; }
        List<BatchQRPrintDocumentAdapter.BitmapItem> items = new ArrayList<>();
        for (QRCodeData d : generatedQRCodes) items.add(new BatchQRPrintDocumentAdapter.BitmapItem(d.bitmap));
        PrintManager pm = (PrintManager) getSystemService(Context.PRINT_SERVICE);
        pm.print("QR Batch " + companyName + " " + new SimpleDateFormat("yyyyMMdd_HHmm", Locale.getDefault()).format(new Date()),
                new BatchQRPrintDocumentAdapter(items, companyName), new PrintAttributes.Builder().build());
    }

    private static class QRCodeData { String code; Bitmap bitmap; QRCodeData(String code, Bitmap bitmap) { this.code = code; this.bitmap = bitmap; } }
}
