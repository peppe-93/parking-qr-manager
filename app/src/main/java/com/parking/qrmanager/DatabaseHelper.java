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
    private static final int DATABASE_VERSION = 2; // bump for revoke/path/hmac

    // Tables
    private static final String TABLE_QR_CODES = "qr_codes";
    private static final String TABLE_SCANS = "scans";

    // qr_codes columns
    private static final String COL_QR_ID = "id";
    private static final String COL_QR_CODE = "qr_code";
    private static final String COL_COMPANY_NAME = "company_name";
    private static final String COL_DATE_CREATED = "date_created";
    private static final String COL_IMAGE_PATH = "image_path"; // new
    private static final String COL_IS_REVOKED = "is_revoked";  // new
    private static final String COL_HMAC = "hmac";              // new

    // scans columns
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
        db.execSQL("CREATE TABLE " + TABLE_QR_CODES + " (" +
                COL_QR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_QR_CODE + " TEXT UNIQUE NOT NULL, " +
                COL_COMPANY_NAME + " TEXT NOT NULL, " +
                COL_DATE_CREATED + " TEXT NOT NULL, " +
                COL_IMAGE_PATH + " TEXT, " +
                COL_IS_REVOKED + " INTEGER DEFAULT 0, " +
                COL_HMAC + " TEXT)"
        );

        db.execSQL("CREATE TABLE " + TABLE_SCANS + " (" +
                COL_SCAN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_SCAN_QR_CODE + " TEXT NOT NULL, " +
                COL_SCAN_AREA + " TEXT NOT NULL, " +
                COL_SCAN_SPOT + " INTEGER NOT NULL, " +
                COL_SCAN_SESSION + " TEXT NOT NULL, " +
                COL_SCAN_DATE + " TEXT NOT NULL, " +
                COL_SCAN_TIME + " TEXT NOT NULL, " +
                COL_IS_EMPTY + " INTEGER DEFAULT 0)"
        );
        db.execSQL("CREATE INDEX IF NOT EXISTS idx_qr_unique ON " + TABLE_QR_CODES + "(" + COL_QR_CODE + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            try { db.execSQL("ALTER TABLE " + TABLE_QR_CODES + " ADD COLUMN " + COL_IMAGE_PATH + " TEXT"); } catch (Exception ignored) {}
            try { db.execSQL("ALTER TABLE " + TABLE_QR_CODES + " ADD COLUMN " + COL_IS_REVOKED + " INTEGER DEFAULT 0"); } catch (Exception ignored) {}
            try { db.execSQL("ALTER TABLE " + TABLE_QR_CODES + " ADD COLUMN " + COL_HMAC + " TEXT"); } catch (Exception ignored) {}
        }
    }

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

    public long insertQRCodeWithMeta(String qrCode, String companyName, String imagePath, String hmac) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_QR_CODE, qrCode);
        values.put(COL_COMPANY_NAME, companyName);
        values.put(COL_DATE_CREATED, getCurrentDateTime());
        values.put(COL_IMAGE_PATH, imagePath);
        values.put(COL_HMAC, hmac);
        long id = db.insertWithOnConflict(TABLE_QR_CODES, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
        return id;
    }

    public boolean revokeQRCode(String qrCode) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_IS_REVOKED, 1);
        int rows = db.update(TABLE_QR_CODES, cv, COL_QR_CODE + "=?", new String[]{qrCode});
        db.close();
        return rows > 0;
    }

    public boolean isQRCodeRevoked(String qrCode) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COL_IS_REVOKED + " FROM " + TABLE_QR_CODES + " WHERE " + COL_QR_CODE + "=? LIMIT 1", new String[]{qrCode});
        boolean revoked = false;
        if (cursor.moveToFirst()) revoked = cursor.getInt(0) == 1;
        cursor.close();
        db.close();
        return revoked;
    }

    public String getQRCodeHmac(String qrCode) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COL_HMAC + " FROM " + TABLE_QR_CODES + " WHERE " + COL_QR_CODE + "=? LIMIT 1", new String[]{qrCode});
        String h = null;
        if (cursor.moveToFirst()) h = cursor.getString(0);
        cursor.close();
        db.close();
        return h;
    }

    public String getQRCodeImagePath(String qrCode) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COL_IMAGE_PATH + " FROM " + TABLE_QR_CODES + " WHERE " + COL_QR_CODE + "=? LIMIT 1", new String[]{qrCode});
        String p = null;
        if (cursor.moveToFirst()) p = cursor.getString(0);
        cursor.close();
        db.close();
        return p;
    }

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

    public static class ScanResult {
        public boolean isDuplicate;
        public String area;
        public int spotNumber;
        public ScanResult(boolean isDuplicate, String area, int spotNumber) {
            this.isDuplicate = isDuplicate; this.area = area; this.spotNumber = spotNumber;
        }
    }

    // Utilities (unchanged)
    private String getCurrentDateTime() { return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()); }
    private String getCurrentDate() { return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()); }
    private String getCurrentTime() { return new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date()); }
}
