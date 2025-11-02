package com.parking.qrmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ScanActivity extends AppCompatActivity {
    
    private static final int CAMERA_PERMISSION_CODE = 100;
    
    private DecoratedBarcodeView barcodeView;
    private RadioGroup radioGroupArea;
    private EditText editSpotNumber;
    private TextView textSessionInfo;
    private TextView textScannedCount;
    private Button btnScanQR;
    private Button btnMarkEmpty;
    private Button btnEndSession;
    
    private DatabaseHelper dbHelper;
    private String currentSessionId;
    private String selectedArea = "";
    private int scannedCount = 0;
    private boolean isScanning = false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        
        dbHelper = new DatabaseHelper(this);
        
        // Inizializza views
        barcodeView = findViewById(R.id.barcodeView);
        radioGroupArea = findViewById(R.id.radioGroupArea);
        editSpotNumber = findViewById(R.id.editSpotNumber);
        textSessionInfo = findViewById(R.id.textSessionInfo);
        textScannedCount = findViewById(R.id.textScannedCount);
        btnScanQR = findViewById(R.id.btnScanQR);
        btnMarkEmpty = findViewById(R.id.btnMarkEmpty);
        btnEndSession = findViewById(R.id.btnEndSession);
        
        // Genera ID sessione
        currentSessionId = generateSessionId();
        textSessionInfo.setText("Sessione: " + currentSessionId);
        updateScannedCount();
        
        // Setup listener area
        radioGroupArea.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = findViewById(checkedId);
                selectedArea = radioButton.getText().toString();
            }
        });
        
        // Button Scansiona QR
        btnScanQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs()) {
                    startScanning();
                }
            }
        });
        
        // Button Segna Vuoto
        btnMarkEmpty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                markSpotAsEmpty();
            }
        });
        
        // Button Termina Sessione
        btnEndSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endSession();
            }
        });
        
        // Richiedi permessi camera
        checkCameraPermission();
    }
    
    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) 
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, 
                new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, 
                                          @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permesso camera necessario", Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    private boolean validateInputs() {
        if (selectedArea.isEmpty()) {
            Toast.makeText(this, "Seleziona un'area", Toast.LENGTH_SHORT).show();
            return false;
        }
        
        String spotStr = editSpotNumber.getText().toString().trim();
        if (spotStr.isEmpty()) {
            Toast.makeText(this, "Inserisci numero postazione", Toast.LENGTH_SHORT).show();
            return false;
        }
        
        int spotNumber = Integer.parseInt(spotStr);
        if (spotNumber < 1 || spotNumber > 100) {
            Toast.makeText(this, "Numero postazione deve essere tra 1 e 100", Toast.LENGTH_SHORT).show();
            return false;
        }
        
        return true;
    }
    
    private void startScanning() {
        if (isScanning) {
            return;
        }
        
        isScanning = true;
        barcodeView.setVisibility(View.VISIBLE);
        barcodeView.decodeContinuous(new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {
                if (result != null && result.getText() != null) {
                    handleQRCodeScanned(result.getText());
                }
            }
            
            @Override
            public void possibleResultPoints(List<ResultPoint> resultPoints) {
                // Non utilizzato
            }
        });
        
        barcodeView.resume();
        btnScanQR.setText("Scansione in corso...");
        btnScanQR.setEnabled(false);
    }
    
    private void stopScanning() {
        if (isScanning) {
            barcodeView.pause();
            barcodeView.setVisibility(View.GONE);
            isScanning = false;
            btnScanQR.setText("Scansiona QR Code");
            btnScanQR.setEnabled(true);
        }
    }
    
    private void handleQRCodeScanned(String qrCode) {
        stopScanning();
        
        // Verifica duplicati
        DatabaseHelper.ScanResult scanResult = dbHelper.checkDuplicateScan(qrCode, currentSessionId);
        
        if (scanResult != null && scanResult.isDuplicate) {
            // QR Code già scansionato in questa sessione
            showDuplicateAlert(qrCode, scanResult.area, scanResult.spotNumber);
        } else {
            // Salva scansione
            int spotNumber = Integer.parseInt(editSpotNumber.getText().toString().trim());
            long result = dbHelper.insertScan(qrCode, selectedArea, spotNumber, currentSessionId, false);
            
            if (result != -1) {
                scannedCount++;
                updateScannedCount();
                Toast.makeText(this, "QR Code scansionato: " + qrCode, Toast.LENGTH_SHORT).show();
                
                // Auto-incrementa numero postazione
                editSpotNumber.setText(String.valueOf(spotNumber + 1));
            } else {
                Toast.makeText(this, "Errore nel salvare la scansione", Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    private void showDuplicateAlert(String qrCode, String previousArea, int previousSpot) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("QR Code Duplicato!");
        builder.setMessage("Questo QR Code è già stato scansionato:\n\n" +
                          "Area: " + previousArea + "\n" +
                          "Postazione: " + previousSpot + "\n\n" +
                          "QR Code: " + qrCode);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
    
    private void markSpotAsEmpty() {
        if (!validateInputs()) {
            return;
        }
        
        int spotNumber = Integer.parseInt(editSpotNumber.getText().toString().trim());
        
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Conferma Postazione Vuota");
        builder.setMessage("Confermi che la postazione " + spotNumber + 
                          " nell'area " + selectedArea + " è vuota?");
        builder.setPositiveButton("Sì", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Salva come postazione vuota
                long result = dbHelper.insertScan("VUOTA", selectedArea, spotNumber, 
                                                 currentSessionId, true);
                
                if (result != -1) {
                    Toast.makeText(ScanActivity.this, "Postazione " + spotNumber + 
                                 " segnata come vuota", Toast.LENGTH_SHORT).show();
                    // Auto-incrementa
                    editSpotNumber.setText(String.valueOf(spotNumber + 1));
                }
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }
    
    private void endSession() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Termina Sessione");
        builder.setMessage("Vuoi terminare la sessione corrente?\n\n" +
                          "Totale scansioni: " + scannedCount);
        builder.setPositiveButton("Termina", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(ScanActivity.this, "Sessione terminata", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        builder.setNegativeButton("Annulla", null);
        builder.show();
    }
    
    private void updateScannedCount() {
        textScannedCount.setText("Scansioni effettuate: " + scannedCount);
    }
    
    private String generateSessionId() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        return "SESSION_" + sdf.format(new Date());
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        if (isScanning) {
            barcodeView.resume();
        }
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        if (isScanning) {
            barcodeView.pause();
        }
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopScanning();
    }
}
