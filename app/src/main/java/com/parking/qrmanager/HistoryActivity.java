package com.parking.qrmanager;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {
    
    private ListView listViewHistory;
    private TextView textTotalScans;
    private DatabaseHelper dbHelper;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        
        dbHelper = new DatabaseHelper(this);
        
        listViewHistory = findViewById(R.id.listViewHistory);
        textTotalScans = findViewById(R.id.textTotalScans);
        
        loadHistory();
    }
    
    private void loadHistory() {
        List<DatabaseHelper.Scan> scans = dbHelper.getAllScans();
        
        textTotalScans.setText("Totale scansioni: " + scans.size());
        
        List<String> scanStrings = new ArrayList<>();
        for (DatabaseHelper.Scan scan : scans) {
            String scanInfo = scan.date + " " + scan.time + "\n" +
                            "Area: " + scan.area + " - Posto: " + scan.spotNumber + "\n" +
                            (scan.isEmpty ? "POSTAZIONE VUOTA" : "QR: " + scan.qrCode) + "\n" +
                            "Sessione: " + scan.sessionId;
            scanStrings.add(scanInfo);
        }
        
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, 
            android.R.layout.simple_list_item_1, scanStrings);
        listViewHistory.setAdapter(adapter);
    }
}
