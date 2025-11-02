package com.parking.qrmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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

public class GenerateQRActivity extends AppCompatActivity {
    
    private static final int STORAGE_PERMISSION_CODE = 102;
    
    private Spinner spinnerCompany;
    private EditText editCustomCompany;
    private Button btnGenerate;
    private Button btnSaveQR;
    private Button btnShareQR;
    private Button btnBatchGenerate;
    private ImageView imageQRCode;
    private DatabaseHelper dbHelper;
    
    private List<String> companies;
    private Bitmap currentQRBitmap;
    private String currentQRCode;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_qr);
        
        dbHelper = new DatabaseHelper(this);
        
        spinnerCompany = findViewById(R.id.spinnerCompany);
        editCustomCompany = findViewById(R.id.editCustomCompany);
        btnGenerate = findViewById(R.id.btnGenerate);
        btnSaveQR = findViewById(R.id.btnSaveQR);
        btnShareQR = findViewById(R.id.btnShareQR);
        btnBatchGenerate = findViewById(R.id.btnBatchGenerate);
        imageQRCode = findViewById(R.id.imageQRCode);
        
        // Setup spinner
        setupCompanySpinner();
        
        btnGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateQRCode();
            }
        });
        
        btnSaveQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveQRCodeToGallery();
            }
        });
        
        btnShareQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareQRCode();
            }
        });
        
        btnBatchGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBatchGeneration();
            }
        });
        
        // Inizialmente disabilita i pulsanti save/share
        btnSaveQR.setEnabled(false);
        btnShareQR.setEnabled(false);
    }
    
    private void setupCompanySpinner() {
        companies = new ArrayList<>();
        companies.add("Seleziona Azienda");
        companies.add("AZIENDA_A");
        companies.add("AZIENDA_B");
        companies.add("AZIENDA_C");
        companies.add("AZIENDA_D");
        companies.add("Personalizzato");
        
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, 
            android.R.layout.simple_spinner_item, companies);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCompany.setAdapter(adapter);
    }
    
    private void generateQRCode() {
        String selectedCompany = spinnerCompany.getSelectedItem().toString();
        
        if (selectedCompany.equals("Seleziona Azienda")) {
            Toast.makeText(this, "Seleziona un'azienda", Toast.LENGTH_SHORT).show();
            return;
        }
        
        String companyName;
        if (selectedCompany.equals("Personalizzato")) {
            companyName = editCustomCompany.getText().toString().trim();
            if (companyName.isEmpty()) {
                Toast.makeText(this, "Inserisci nome azienda personalizzato", Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            companyName = selectedCompany;
        }
        
        // Genera codice univoco sicuro
        String uniqueCode = generateSecureCode();
        currentQRCode = companyName + "_" + uniqueCode;
        
        try {
            // Genera QR Code con testo sotto
            currentQRBitmap = generateQRCodeWithText(currentQRCode, 600, 700);
            imageQRCode.setImageBitmap(currentQRBitmap);
            
            // Salva nel database
            long result = dbHelper.insertQRCode(currentQRCode, companyName);
            
            if (result != -1) {
                Toast.makeText(this, "QR Code generato con successo!", Toast.LENGTH_SHORT).show();
                // Abilita pulsanti save/share
                btnSaveQR.setEnabled(true);
                btnShareQR.setEnabled(true);
            } else {
                Toast.makeText(this, "Errore nel salvare il QR Code", Toast.LENGTH_SHORT).show();
            }
            
        } catch (WriterException e) {
            Toast.makeText(this, "Errore nella generazione del QR Code", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
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
        
        // Crea bitmap finale con QR + testo sotto
        Bitmap finalBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(finalBitmap);
        canvas.drawColor(Color.WHITE);
        
        // Disegna QR Code
        canvas.drawBitmap(qrBitmap, 0, 0, null);
        
        // Disegna testo sotto
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(20);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        paint.setTextAlign(Paint.Align.CENTER);
        
        // Dividi il testo se troppo lungo
        String[] parts = content.split("_");
        int textY = qrSize + 35;
        for (int i = 0; i < Math.min(parts.length, 3); i++) {
            canvas.drawText(parts[i], width / 2f, textY, paint);
            textY += 25;
        }
        
        return finalBitmap;
    }
    
    private void saveQRCodeToGallery() {
        if (currentQRBitmap == null) {
            Toast.makeText(this, "Genera prima un QR Code", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) 
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, 
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
            return;
        }
        
        try {
            // Crea directory per QR Codes
            File qrDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "QRCodes");
            if (!qrDir.exists()) {
                qrDir.mkdirs();
            }
            
            // Nome file
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
            String sanitized = currentQRCode.replaceAll("[^a-zA-Z0-9_]", "_");
            if (sanitized.length() > 50) {
                sanitized = sanitized.substring(0, 50);
            }
            String fileName = "QR_" + sanitized + "_" + sdf.format(new Date()) + ".png";
            File imageFile = new File(qrDir, fileName);
            
            // Salva immagine
            FileOutputStream fos = new FileOutputStream(imageFile);
            currentQRBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            
            Toast.makeText(this, "QR Code salvato in Galleria!", Toast.LENGTH_LONG).show();
            
            // Notifica la galleria
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(imageFile);
            mediaScanIntent.setData(contentUri);
            sendBroadcast(mediaScanIntent);
            
        } catch (IOException e) {
            Toast.makeText(this, "Errore nel salvare: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
    
    private void shareQRCode() {
        if (currentQRBitmap == null) {
            Toast.makeText(this, "Genera prima un QR Code", Toast.LENGTH_SHORT).show();
            return;
        }
        
        try {
            File cachePath = new File(getCacheDir(), "images");
            cachePath.mkdirs();
            File imageFile = new File(cachePath, "qr_code_temp.png");
            
            FileOutputStream fos = new FileOutputStream(imageFile);
            currentQRBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            
            Uri contentUri = FileProvider.getUriForFile(this, "com.parking.qrmanager.fileprovider", imageFile);
            
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/png");
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            shareIntent.putExtra(Intent.EXTRA_TEXT, "QR Code: " + currentQRCode);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            
            startActivity(Intent.createChooser(shareIntent, "Condividi QR Code"));
            
        } catch (IOException e) {
            Toast.makeText(this, "Errore condivisione: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    
    private void startBatchGeneration() {
        String selectedCompany = spinnerCompany.getSelectedItem().toString();
        
        if (selectedCompany.equals("Seleziona Azienda")) {
            Toast.makeText(this, "Seleziona un'azienda", Toast.LENGTH_SHORT).show();
            return;
        }
        
        String companyName;
        if (selectedCompany.equals("Personalizzato")) {
            companyName = editCustomCompany.getText().toString().trim();
            if (companyName.isEmpty()) {
                Toast.makeText(this, "Inserisci nome azienda personalizzato", Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            companyName = selectedCompany;
        }
        
        Intent intent = new Intent(this, BatchQRGeneratorActivity.class);
        intent.putExtra("COMPANY_NAME", companyName);
        startActivity(intent);
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                saveQRCodeToGallery();
            } else {
                Toast.makeText(this, "Permesso di scrittura negato", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
