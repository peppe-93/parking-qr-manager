package com.parking.qrmanager;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class QrRepository {
    private final DatabaseHelper db;
    public QrRepository(Context ctx) { this.db = new DatabaseHelper(ctx); }

    public boolean deleteQRCodeCompletely(String code) {
        SQLiteDatabase rdb = db.getReadableDatabase();
        String imagePath = null;
        Cursor c = rdb.rawQuery("SELECT image_path FROM qr_codes WHERE qr_code=? LIMIT 1", new String[]{code});
        try { if (c.moveToFirst()) imagePath = c.getString(0); } finally { c.close(); }

        // delete DB row
        SQLiteDatabase wdb = db.getWritableDatabase();
        int rows = wdb.delete("qr_codes", "qr_code=?", new String[]{code});
        
        // delete file if present
        if (imagePath != null && !imagePath.isEmpty()) {
            try {
                java.io.File f = new java.io.File(imagePath);
                if (f.exists()) { // best-effort delete
                    //noinspection ResultOfMethodCallIgnored
                    f.delete();
                }
            } catch (Exception ignored) {}
        }
        return rows > 0;
    }
}
