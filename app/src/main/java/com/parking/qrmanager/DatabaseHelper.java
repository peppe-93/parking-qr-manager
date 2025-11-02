package com.parking.qrmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {
    
    private static final String DATABASE_NAME = "ParkingDB";
    private static final int DATABASE_VERSION = 1;
    
    // Tabella QR Codes generati
    private static final String TABLE_QR_CODES = "qr_codes";
    private static final String COL_QR_ID = "id";
    private static final String COL_QR_CODE = "qr_code";
    private static final String COL_COMPANY_NAME = "company_name";
    private static final String COL_DATE_CREATED = "date_created";
    
    // Tabella Scansioni
    private static final String TABLE_SCANS = "scans";
    private static final String COL_SCAN_ID = "scan_id";
    private static final String COL_SCAN_QR_CODE = "qr_code";
    private static final String COL_SCAN_AREA = "area";
    private static final String COL_SCAN_SPOT = "spot_number";
    private static final String COL_SCAN_SESSION = "session_id";
    private static final String COL_SCAN_DATE = "scan_date";
    private static final String COL_SCAN_TIME = "scan_time";
    private static final String COL_IS_EMPTY = "is_empty";
    
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crea tabella QR Codes
        String createQRTable = "CREATE TABLE " + TABLE_QR_CODES + " (" +
                COL_QR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_QR_CODE + " TEXT UNIQUE NOT NULL, " +
                COL_COMPANY_NAME + " TEXT NOT NULL, " +
                COL_DATE_CREATED + " TEXT NOT NULL)";
        db.execSQL(createQRTable);
        
        // Crea tabella Scansioni
        String createScansTable = "CREATE TABLE " + TABLE_SCANS + " (" +
                COL_SCAN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_SCAN_QR_CODE + " TEXT NOT NULL, " +
                COL_SCAN_AREA + " TEXT NOT NULL, " +
                COL_SCAN_SPOT + " INTEGER NOT NULL, " +
                COL_SCAN_SESSION + " TEXT NOT NULL, " +
                COL_SCAN_DATE + " TEXT NOT NULL, " +
                COL_SCAN_TIME + " TEXT NOT NULL, " +
                COL_IS_EMPTY + " INTEGER DEFAULT 0)";
        db.execSQL(createScansTable);
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QR_CODES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCANS);
        onCreate(db);
    }
    
    // Inserisci nuovo QR Code
    public long insertQRCode(String qrCode, String companyName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_QR_CODE, qrCode);
        values.put(COL_COMPANY_NAME, companyName);
        values.put(COL_DATE_CREATED, getCurrentDateTime());
        
        long result = db.insert(TABLE_QR_CODES, null, values);
        db.close();
        return result;
    }
    
    // Inserisci scansione
    public long insertScan(String qrCode, String area, int spotNumber, String sessionId, boolean isEmpty) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_SCAN_QR_CODE, qrCode);
        values.put(COL_SCAN_AREA, area);
        values.put(COL_SCAN_SPOT, spotNumber);
        values.put(COL_SCAN_SESSION, sessionId);
        values.put(COL_SCAN_DATE, getCurrentDate());
        values.put(COL_SCAN_TIME, getCurrentTime());
        values.put(COL_IS_EMPTY, isEmpty ? 1 : 0);
        
        long result = db.insert(TABLE_SCANS, null, values);
        db.close();
        return result;
    }
    
    // Verifica se QR code è già stato scansionato nella sessione corrente
    public ScanResult checkDuplicateScan(String qrCode, String sessionId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COL_SCAN_AREA + ", " + COL_SCAN_SPOT + 
                       " FROM " + TABLE_SCANS + 
                       " WHERE " + COL_SCAN_QR_CODE + " = ? AND " + 
                       COL_SCAN_SESSION + " = ? AND " +
                       COL_IS_EMPTY + " = 0";
        
        Cursor cursor = db.rawQuery(query, new String[]{qrCode, sessionId});
        
        ScanResult result = null;
        if (cursor.moveToFirst()) {
            String area = cursor.getString(0);
            int spot = cursor.getInt(1);
            result = new ScanResult(true, area, spot);
        }
        
        cursor.close();
        db.close();
        return result;
    }
    
    // Ottieni tutte le scansioni per esportazione
    public List<Scan> getAllScans() {
        List<Scan> scanList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        
        String query = "SELECT * FROM " + TABLE_SCANS + " ORDER BY " + COL_SCAN_DATE + " DESC, " + COL_SCAN_TIME + " DESC";
        Cursor cursor = db.rawQuery(query, null);
        
        if (cursor.moveToFirst()) {
            do {
                Scan scan = new Scan(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getInt(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getInt(7) == 1
                );
                scanList.add(scan);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        db.close();
        return scanList;
    }
    
    // Ottieni scansioni per sessione
    public List<Scan> getScansBySession(String sessionId) {
        List<Scan> scanList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        
        String query = "SELECT * FROM " + TABLE_SCANS + " WHERE " + COL_SCAN_SESSION + " = ? ORDER BY " + COL_SCAN_SPOT + " ASC";
        Cursor cursor = db.rawQuery(query, new String[]{sessionId});
        
        if (cursor.moveToFirst()) {
            do {
                Scan scan = new Scan(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getInt(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getInt(7) == 1
                );
                scanList.add(scan);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        db.close();
        return scanList;
    }
    
    // Elimina scansioni vecchie
    public int deleteOldScans(String beforeDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_SCANS, COL_SCAN_DATE + " < ?", new String[]{beforeDate});
        db.close();
        return rowsDeleted;
    }
    
    // Utility per ottenere data e ora correnti
    private String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }
    
    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(new Date());
    }
    
    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }
    
    // Classe per risultato verifica duplicati
    public static class ScanResult {
        public boolean isDuplicate;
        public String area;
        public int spotNumber;
        
        public ScanResult(boolean isDuplicate, String area, int spotNumber) {
            this.isDuplicate = isDuplicate;
            this.area = area;
            this.spotNumber = spotNumber;
        }
    }
    
    // Classe modello Scan
    public static class Scan {
        public int id;
        public String qrCode;
        public String area;
        public int spotNumber;
        public String sessionId;
        public String date;
        public String time;
        public boolean isEmpty;
        
        public Scan(int id, String qrCode, String area, int spotNumber, 
                   String sessionId, String date, String time, boolean isEmpty) {
            this.id = id;
            this.qrCode = qrCode;
            this.area = area;
            this.spotNumber = spotNumber;
            this.sessionId = sessionId;
            this.date = date;
            this.time = time;
            this.isEmpty = isEmpty;
        }
    }
}
