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
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BatchQRGeneratorActivity extends AppCompatActivity {
    
    private static final int STORAGE_PERMISSION_CODE = 103;
    
    private TextView textCompanyName;
    private EditText editQuantity;
    private Button btnGenerateBatch;
    private Button btnExportPDF;
    private ProgressBar progressBar;
    private TextView textStatus;
    
    private String companyName;
    private DatabaseHelper dbHelper;
    private List<QRCodeData> generatedQRCodes;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        
        // Ricevi nome azienda
        companyName = getIntent().getStringExtra("COMPANY_NAME");
        if (companyName == null) {
            companyName = "AZIENDA";
        }
        textCompanyName.setText("Azienda: " + companyName);
        
        btnGenerateBatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateBatchQRCodes();
            }
        });
        
        btnExportPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exportToPDF();
            }
        });
        
        btnExportPDF.setEnabled(false);
    }
    
    private void generateBatchQRCodes() {
        String quantityStr = editQuantity.getText().toString().trim();
        if (quantityStr.isEmpty()) {
            Toast.makeText(this, "Inserisci la quantità", Toast.LENGTH_SHORT).show();
            return;
        }
        
        int quantity = Integer.parseInt(quantityStr);
        if (quantity < 1 || quantity > 100) {
            Toast.makeText(this, "Quantità deve essere tra 1 e 100", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Mostra dialogo conferma
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Genera " + quantity + " QR Code");
        builder.setMessage("Confermi la generazione di " + quantity + " QR Code per " + companyName + "?");
        builder.setPositiveButton("Genera", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                performBatchGeneration(quantity);
            }
        });
        builder.setNegativeButton("Annulla", null);
        builder.show();
    }
    
    private void performBatchGeneration(final int quantity) {
        progressBar.setVisibility(View.VISIBLE);
        textStatus.setVisibility(View.VISIBLE);
        btnGenerateBatch.setEnabled(false);
        generatedQRCodes.clear();
        
        // Usa thread per non bloccare UI
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < quantity; i++) {
                    final int current = i + 1;
                    
                    // Genera QR code
                    String uniqueCode = generateSecureCode();
                    String qrCode = companyName + "_" + uniqueCode;
                    
                    try {
                        Bitmap qrBitmap = generateQRCodeWithText(qrCode, 400, 480);
                        generatedQRCodes.add(new QRCodeData(qrCode, qrBitmap));
                        
                        // Salva nel database
                        dbHelper.insertQRCode(qrCode, companyName);
                        
                        // Aggiorna UI
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textStatus.setText("Generati: " + current + " / " + quantity);
                            }
                        });
                        
                        // Piccola pausa per non sovraccaricare
                        Thread.sleep(100);
                        
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                
                // Completato
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        textStatus.setText("✓ Generati " + quantity + " QR Code!");
                        btnGenerateBatch.setEnabled(true);
                        btnExportPDF.setEnabled(true);
                        Toast.makeText(BatchQRGeneratorActivity.this, 
                            "Generazione completata!", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }).start();
    }
    
    private String generateSecureCode() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder code = new StringBuilder();
        
        for (int i = 0; i < 12; i++) {
            code.append(chars.charAt(random.nextInt(chars.length())));
        }
        
        long timestamp = System.currentTimeMillis();
        code.append(Long.toHexString(timestamp).toUpperCase());
        
        return code.toString();
    }
    
    private Bitmap generateQRCodeWithText(String content, int width, int height) throws WriterException {
        // Genera QR Code
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        int qrSize = width;
        BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, qrSize, qrSize);
        
        Bitmap qrBitmap = Bitmap.createBitmap(qrSize, qrSize, Bitmap.Config.RGB_565);
        for (int x = 0; x < qrSize; x++) {
            for (int y = 0; y < qrSize; y++) {
                qrBitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
            }
        }
        
        // Crea bitmap finale con QR + testo
        Bitmap finalBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(finalBitmap);
        canvas.drawColor(Color.WHITE);
        
        // Disegna QR Code
        canvas.drawBitmap(qrBitmap, 0, 0, null);
        
        // Disegna testo sotto
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(14);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        paint.setTextAlign(Paint.Align.CENTER);
        
        String[] parts = content.split("_");
        int textY = qrSize + 25;
        for (int i = 0; i < Math.min(parts.length, 3); i++) {
            canvas.drawText(parts[i], width / 2f, textY, paint);
            textY += 20;
        }
        
        return finalBitmap;
    }
    
    private void exportToPDF() {
        if (generatedQRCodes.isEmpty()) {
            Toast.makeText(this, "Genera prima i QR Code", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) 
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, 
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
            return;
        }
        
        try {
            // Crea PDF
            PdfDocument document = new PdfDocument();
            
            // Dimensioni A4 in punti (595 x 842)
            int pageWidth = 595;
            int pageHeight = 842;
            
            // QR Code per pagina: 2 colonne x 3 righe = 6 QR per pagina
            int qrWidth = 240;
            int qrHeight = 290;
            int cols = 2;
            int rows = 3;
            int marginX = 40;
            int marginY = 40;
            int spacingX = 15;
            int spacingY = 15;
            
            int qrPerPage = cols * rows;
            int totalPages = (int) Math.ceil(generatedQRCodes.size() / (double) qrPerPage);
            
            int qrIndex = 0;
            
            for (int pageNum = 0; pageNum < totalPages; pageNum++) {
                PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNum + 1).create();
                PdfDocument.Page page = document.startPage(pageInfo);
                Canvas canvas = page.getCanvas();
                canvas.drawColor(Color.WHITE);
                
                // Disegna QR codes sulla pagina
                for (int row = 0; row < rows && qrIndex < generatedQRCodes.size(); row++) {
                    for (int col = 0; col < cols && qrIndex < generatedQRCodes.size(); col++) {
                        int x = marginX + col * (qrWidth + spacingX);
                        int y = marginY + row * (qrHeight + spacingY);
                        
                        Bitmap qrBitmap = generatedQRCodes.get(qrIndex).bitmap;
                        Bitmap scaledBitmap = Bitmap.createScaledBitmap(qrBitmap, qrWidth, qrHeight, true);
                        canvas.drawBitmap(scaledBitmap, x, y, null);
                        
                        qrIndex++;
                    }
                }
                
                // Aggiungi footer con pagina e data
                Paint textPaint = new Paint();
                textPaint.setColor(Color.GRAY);
                textPaint.setTextSize(10);
                textPaint.setTextAlign(Paint.Align.CENTER);
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                canvas.drawText("Pagina " + (pageNum + 1) + " di " + totalPages + " - " + 
                              companyName + " - " + sdf.format(new Date()), 
                              pageWidth / 2f, pageHeight - 20, textPaint);
                
                document.finishPage(page);
            }
            
            // Salva PDF
            File pdfDir = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "QRCodes");
            if (!pdfDir.exists()) {
                pdfDir.mkdirs();
            }
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
            String fileName = "QRCodes_" + companyName + "_" + sdf.format(new Date()) + ".pdf";
            File pdfFile = new File(pdfDir, fileName);
            
            document.writeTo(new FileOutputStream(pdfFile));
            document.close();
            
            // Mostra dialogo successo
            showExportSuccessDialog(pdfFile);
            
        } catch (IOException e) {
            Toast.makeText(this, "Errore creazione PDF: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
    
    private void showExportSuccessDialog(final File pdfFile) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("✓ PDF Creato!");
        builder.setMessage("PDF con " + generatedQRCodes.size() + " QR Code creato:\n\n" + 
                          pdfFile.getName() + "\n\nVuoi condividerlo?");
        builder.setPositiveButton("Condividi", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sharePDF(pdfFile);
            }
        });
        builder.setNegativeButton("Chiudi", null);
        builder.show();
    }
    
    private void sharePDF(File pdfFile) {
        Uri fileUri = FileProvider.getUriForFile(this, "com.parking.qrmanager.fileprovider", pdfFile);
        
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("application/pdf");
        shareIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "QR Codes per " + companyName);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        
        startActivity(Intent.createChooser(shareIntent, "Condividi PDF"));
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                exportToPDF();
            } else {
                Toast.makeText(this, "Permesso di scrittura negato", Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    // Classe per memorizzare dati QR Code
    private static class QRCodeData {
        String code;
        Bitmap bitmap;
        
        QRCodeData(String code, Bitmap bitmap) {
            this.code = code;
            this.bitmap = bitmap;
        }
    }
}
