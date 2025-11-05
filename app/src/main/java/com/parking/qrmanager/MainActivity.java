package com.parking.qrmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CardView cardGenerate = findViewById(R.id.cardGenerateQR);
        cardGenerate.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, GenerateQRActivity.class)));

        CardView cardScan = findViewById(R.id.cardScanArea);
        cardScan.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ScanActivity.class)));

        CardView cardHistory = findViewById(R.id.cardHistory);
        cardHistory.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, HistoryActivity.class)));

        CardView cardExport = findViewById(R.id.cardExport);
        cardExport.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ExportActivity.class)));

        // Nuova card Libreria QR
        CardView cardLibrary = findViewById(R.id.cardQrLibrary);
        if (cardLibrary != null) {
            cardLibrary.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, QrLibraryActivity.class)));
        }
    }
}
