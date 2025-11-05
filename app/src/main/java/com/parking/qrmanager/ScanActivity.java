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
    private TextView textCurrentSpot;
    private TextView textSessionInfo;
    private TextView textScannedCount;
    private TextView textProgressInfo;
    private Button btnStartSequentialScan;
    private Button btnMarkEmpty;
    private Button btnSkipSpot;
    private Button btnEndSession;
    private Button btnManualSpotEntry;

    private DatabaseHelper dbHelper;
    private String currentSessionId;
    private String selectedArea = "";
    private int currentSpotNumber = 1;
    private int scannedCount = 0;
    private int totalSpotsInArea = 100; // Default, può essere configurato
    private boolean isSequentialMode = false;
    private boolean isScanning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        dbHelper = new DatabaseHelper(this);

        // Inizializza views
        barcodeView = findViewById(R.id.barcodeView);
        radioGroupArea = findViewById(R.id.radioGroupArea);
        textCurrentSpot = findViewById(R.id.textCurrentSpot);
        textSessionInfo = findViewById(R.id.textSessionInfo);
        textScannedCount = findViewById(R.id.textScannedCount);
        textProgressInfo = findViewById(R.id.textProgressInfo);
        btnStartSequentialScan = findViewById(R.id.btnStartSequentialScan);
        btnMarkEmpty = findViewById(R.id.btnMarkEmpty);
        btnSkipSpot = findViewById(R.id.btnSkipSpot);
        btnEndSession = findViewById(R.id.btnEndSession);
        btnManualSpotEntry = findViewById(R.id.btnManualSpotEntry);

        // Genera ID sessione
        currentSessionId = generateSessionId();
        textSessionInfo.setText("Sessione: " + currentSessionId);
        updateDisplay();

        // Setup listener area
        radioGroupArea.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = findViewById(checkedId);
                selectedArea = radioButton.getText().toString();
                // Reset alla postazione 1 quando si cambia area
                currentSpotNumber = 1;
                updateDisplay();
                // Configura il numero totale di postazioni per area
                configureTotalSpotsForArea(selectedArea);
            }
        });

        // Button Inizia Scansione Sequenziale
        btnStartSequentialScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateAreaSelection()) {
                    startSequentialScanning();
                }
            }
        });

        // Button Segna Vuoto
        btnMarkEmpty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                markCurrentSpotAsEmpty();
            }
        });

        // Button Salta Postazione
        btnSkipSpot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skipCurrentSpot();
            }
        });

        // Button Inserimento Manuale
        btnManualSpotEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showManualSpotEntry();
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
        
        // Inizializza la UI
        setSequentialMode(false);
    }

    private void configureTotalSpotsForArea(String area) {
        // Configura il numero totale di postazioni per ogni area
        // Questi valori possono essere personalizzati per ogni area
        switch (area) {
            case "Area A":
                totalSpotsInArea = 50;
                break;
            case "Area B":
                totalSpotsInArea = 75;
                break;
            case "Area C":
                totalSpotsInArea = 100;
                break;
            case "Area D":
                totalSpotsInArea = 60;
                break;
            default:
                totalSpotsInArea = 100;
                break;
        }
        updateDisplay();
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
                Toast.makeText(this, "Permesso camera necessario per la scansione", Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean validateAreaSelection() {
        if (selectedArea.isEmpty()) {
            Toast.makeText(this, "Seleziona prima un'area da scansionare", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void startSequentialScanning() {
        if (isScanning) {
            return;
        }

        setSequentialMode(true);
        currentSpotNumber = 1; // Inizia sempre dalla postazione 1
        updateDisplay();
        startScanning();

        Toast.makeText(this, "Scansione sequenziale avviata dall'Area " + selectedArea + ", Postazione 1", Toast.LENGTH_LONG).show();
    }

    private void setSequentialMode(boolean enabled) {
        isSequentialMode = enabled;
        
        // Mostra/nasconde controlli appropriati
        btnStartSequentialScan.setVisibility(enabled ? View.GONE : View.VISIBLE);
        btnMarkEmpty.setVisibility(enabled ? View.VISIBLE : View.GONE);
        btnSkipSpot.setVisibility(enabled ? View.VISIBLE : View.GONE);
        btnManualSpotEntry.setVisibility(enabled ? View.GONE : View.VISIBLE);
        
        // Abilita/disabilita selezione area durante scansione
        for (int i = 0; i < radioGroupArea.getChildCount(); i++) {
            radioGroupArea.getChildAt(i).setEnabled(!enabled);
        }
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
        updateDisplay();
    }

    private void stopScanning() {
        if (isScanning) {
            barcodeView.pause();
            barcodeView.setVisibility(View.GONE);
            isScanning = false;
            updateDisplay();
        }
    }

    private void handleQRCodeScanned(String qrCode) {
        stopScanning();

        // Verifica duplicati
        DatabaseHelper.ScanResult scanResult = dbHelper.checkDuplicateScan(qrCode, currentSessionId);

        if (scanResult != null && scanResult.isDuplicate) {
            // QR Code già scansionato in questa sessione
            showDuplicateAlert(qrCode, scanResult.area, scanResult.spotNumber);
            // In modalità sequenziale, riprendi automaticamente la scansione
            if (isSequentialMode) {
                startScanning();
            }
        } else {
            // Salva scansione
            long result = dbHelper.insertScan(qrCode, selectedArea, currentSpotNumber, currentSessionId, false);

            if (result != -1) {
                scannedCount++;
                Toast.makeText(this, "✓ Postazione " + currentSpotNumber + " scansionata: " + qrCode, Toast.LENGTH_SHORT).show();
                
                // In modalità sequenziale, passa automaticamente alla prossima postazione
                if (isSequentialMode) {
                    moveToNextSpot();
                }
            } else {
                Toast.makeText(this, "Errore nel salvare la scansione", Toast.LENGTH_SHORT).show();
                // Riprendi la scansione anche in caso di errore
                if (isSequentialMode) {
                    startScanning();
                }
            }
        }
        
        updateDisplay();
    }

    private void moveToNextSpot() {
        currentSpotNumber++;
        updateDisplay();
        
        if (currentSpotNumber <= totalSpotsInArea) {
            // Continua con la prossima postazione
            startScanning();
        } else {
            // Scansione dell'area completata
            showAreaCompletedDialog();
        }
    }

    private void showAreaCompletedDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Area Completata!");
        builder.setMessage("Hai completato la scansione dell'Area " + selectedArea + "\n\n" +
                          "Postazioni scansionate: " + scannedCount + " di " + totalSpotsInArea + "\n\n" +
                          "Vuoi iniziare con un'altra area?");
        builder.setPositiveButton("Nuova Area", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setSequentialMode(false);
                currentSpotNumber = 1;
                updateDisplay();
            }
        });
        builder.setNegativeButton("Termina Sessione", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                endSession();
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    private void markCurrentSpotAsEmpty() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Conferma Postazione Vuota");
        builder.setMessage("Confermi che la postazione " + currentSpotNumber +
                          " nell'area " + selectedArea + " è vuota?");
        builder.setPositiveButton("Sì, Vuota", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Salva come postazione vuota
                long result = dbHelper.insertScan("VUOTA", selectedArea, currentSpotNumber,
                                                 currentSessionId, true);

                if (result != -1) {
                    Toast.makeText(ScanActivity.this, "⭕ Postazione " + currentSpotNumber +
                                 " segnata come vuota", Toast.LENGTH_SHORT).show();
                    // Passa alla prossima postazione
                    if (isSequentialMode) {
                        moveToNextSpot();
                    }
                } else {
                    Toast.makeText(ScanActivity.this, "Errore nel salvare", Toast.LENGTH_SHORT).show();
                }
                updateDisplay();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Riprendi la scansione
                if (isSequentialMode) {
                    startScanning();
                }
            }
        });
        builder.show();
    }

    private void skipCurrentSpot() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Salta Postazione");
        builder.setMessage("Vuoi saltare la postazione " + currentSpotNumber + " e passare alla successiva?");
        builder.setPositiveButton("Salta", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(ScanActivity.this, "➡️ Postazione " + currentSpotNumber + " saltata", Toast.LENGTH_SHORT).show();
                if (isSequentialMode) {
                    moveToNextSpot();
                }
            }
        });
        builder.setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Riprendi la scansione
                if (isSequentialMode) {
                    startScanning();
                }
            }
        });
        builder.show();
    }

    private void showManualSpotEntry() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Inserimento Manuale");
        
        EditText editText = new EditText(this);
        editText.setHint("Numero postazione (1-" + totalSpotsInArea + ")");
        editText.setText(String.valueOf(currentSpotNumber));
        builder.setView(editText);
        
        builder.setPositiveButton("Vai alla Postazione", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    int spotNumber = Integer.parseInt(editText.getText().toString().trim());
                    if (spotNumber >= 1 && spotNumber <= totalSpotsInArea) {
                        currentSpotNumber = spotNumber;
                        updateDisplay();
                        Toast.makeText(ScanActivity.this, "Postazione impostata a: " + spotNumber, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ScanActivity.this, "Numero postazione non valido", Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(ScanActivity.this, "Inserisci un numero valido", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Annulla", null);
        builder.show();
    }

    private void showDuplicateAlert(String qrCode, String previousArea, int previousSpot) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("⚠️ QR Code Duplicato!");
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

    private void endSession() {
        stopScanning();
        
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Termina Sessione");
        builder.setMessage("Vuoi terminare la sessione corrente?\n\n" +
                          "Area corrente: " + selectedArea + "\n" +
                          "Postazione corrente: " + currentSpotNumber + "\n" +
                          "Totale scansioni: " + scannedCount);
        builder.setPositiveButton("Termina", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(ScanActivity.this, "Sessione terminata", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        builder.setNegativeButton("Continua", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Se era in modalità sequenziale, riprendi la scansione
                if (isSequentialMode) {
                    startScanning();
                }
            }
        });
        builder.show();
    }

    private void updateDisplay() {
        textCurrentSpot.setText("Postazione Corrente: " + currentSpotNumber);
        textScannedCount.setText("Scansioni effettuate: " + scannedCount);
        
        if (!selectedArea.isEmpty()) {
            textProgressInfo.setText("Area: " + selectedArea + " | Progresso: " + 
                                   currentSpotNumber + "/" + totalSpotsInArea);
        } else {
            textProgressInfo.setText("Seleziona un'area per iniziare");
        }
        
        // Aggiorna il testo del bottone principale
        if (isSequentialMode) {
            if (isScanning) {
                btnStartSequentialScan.setText("Scansione in corso...");
            } else {
                btnStartSequentialScan.setText("Modalità Sequenziale Attiva");
            }
        } else {
            btnStartSequentialScan.setText("Inizia Scansione Sequenziale");
        }
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

    @Override
    public void onBackPressed() {
        if (isSequentialMode) {
            // Conferma prima di uscire dalla modalità sequenziale
            endSession();
        } else {
            super.onBackPressed();
        }
    }
}