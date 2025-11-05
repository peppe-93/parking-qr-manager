package com.parking.qrmanager;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.print.PrintHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.util.ArrayList;
import java.util.List;

public class QrLibraryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private QRAdapter adapter;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_library);
        db = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerQRCodes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new QRAdapter(this, loadQRCodes());
        recyclerView.setAdapter(adapter);
    }

    private List<QRItem> loadQRCodes() {
        List<QRItem> list = new ArrayList<>();
        // Query diretta per evitare di dover estendere ancora DatabaseHelper ora
        Cursor c = db.getReadableDatabase().rawQuery(
                "SELECT id, qr_code, company_name, date_created, is_revoked FROM qr_codes ORDER BY date_created DESC",
                null);
        try {
            while (c.moveToNext()) {
                QRItem it = new QRItem();
                it.id = c.getInt(0);
                it.code = c.getString(1);
                it.company = c.getString(2);
                it.createdAt = c.getString(3);
                it.revoked = c.getInt(4) == 1;
                list.add(it);
            }
        } finally { c.close(); }
        return list;
    }

    private static class QRItem {
        int id;
        String code;
        String company;
        String createdAt;
        boolean revoked;
    }

    private class QRAdapter extends RecyclerView.Adapter<QRVH> {
        private final Context ctx;
        private final List<QRItem> data;
        QRAdapter(Context c, List<QRItem> d) { ctx = c; data = d; }
        @NonNull @Override public QRVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_qr, parent, false);
            return new QRVH(v);
        }
        @Override public void onBindViewHolder(@NonNull QRVH h, int pos) {
            QRItem it = data.get(pos);
            h.txtCode.setText(it.code);
            h.txtMeta.setText(it.company + " • " + it.createdAt + (it.revoked ? " • REVOCATO" : ""));
            // anteprima veloce (piccola)
            try {
                h.preview.setImageBitmap(renderQRPreview(it.code, 200, 230));
            } catch (Exception e) { h.preview.setImageBitmap(null); }

            h.btnOpen.setOnClickListener(v -> showPreviewDialog(it.code));
            h.btnPrint.setOnClickListener(v -> printCode(it.code));
            h.btnShare.setOnClickListener(v -> shareCode(it.code));
            h.btnRevoke.setOnClickListener(v -> {
                if (it.revoked) {
                    Toast.makeText(ctx, "Già revocato", Toast.LENGTH_SHORT).show();
                } else {
                    boolean ok = db.revokeQRCode(it.code);
                    if (ok) { it.revoked = true; notifyItemChanged(pos); }
                    Toast.makeText(ctx, ok ? "QR revocato" : "Errore revoca", Toast.LENGTH_SHORT).show();
                }
            });
        }
        @Override public int getItemCount() { return data.size(); }
    }

    private static class QRVH extends RecyclerView.ViewHolder {
        ImageView preview; TextView txtCode; TextView txtMeta;
        ImageButton btnOpen, btnPrint, btnShare, btnRevoke;
        QRVH(@NonNull View v) {
            super(v);
            preview = v.findViewById(R.id.imgPreview);
            txtCode = v.findViewById(R.id.txtCode);
            txtMeta = v.findViewById(R.id.txtMeta);
            btnOpen = v.findViewById(R.id.btnOpen);
            btnPrint = v.findViewById(R.id.btnPrint);
            btnShare = v.findViewById(R.id.btnShare);
            btnRevoke = v.findViewById(R.id.btnRevoke);
        }
    }

    private Bitmap renderQRPreview(String content, int width, int height) throws WriterException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        int qrSize = width;
        BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, qrSize, qrSize);
        Bitmap qrBitmap = Bitmap.createBitmap(qrSize, qrSize, Bitmap.Config.RGB_565);
        for (int x = 0; x < qrSize; x++) {
            for (int y = 0; y < qrSize; y++) {
                qrBitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
            }
        }
        Bitmap finalBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(finalBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(qrBitmap, 0, 0, null);
        Paint paint = new Paint();
        paint.setColor(Color.BLACK); paint.setTextSize(16); paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD)); paint.setTextAlign(Paint.Align.CENTER);
        int textY = qrSize + 28;
        String[] parts = content.split("_");
        for (int i = 0; i < Math.min(parts.length, 2); i++) { canvas.drawText(parts[i], width/2f, textY, paint); textY += 22; }
        return finalBitmap;
    }

    private void showPreviewDialog(String code) {
        try {
            Bitmap bmp = renderQRPreview(code, 600, 700);
            ImageView iv = new ImageView(this);
            iv.setImageBitmap(bmp);
            new AlertDialog.Builder(this)
                    .setTitle("QR Code")
                    .setView(iv)
                    .setPositiveButton("Chiudi", (d, w) -> d.dismiss())
                    .show();
        } catch (Exception e) { Toast.makeText(this, "Errore anteprima", Toast.LENGTH_SHORT).show(); }
    }

    private void printCode(String code) {
        try {
            Bitmap bmp = renderQRPreview(code, 800, 900);
            PrintHelper ph = new PrintHelper(this);
            ph.setScaleMode(PrintHelper.SCALE_MODE_FIT);
            ph.printBitmap("QR_" + code, bmp);
        } catch (Exception e) { Toast.makeText(this, "Errore stampa", Toast.LENGTH_SHORT).show(); }
    }

    private void shareCode(String code) {
        try {
            Bitmap bmp = renderQRPreview(code, 600, 700);
            ShareHelper.shareImage(this, bmp, "qr_" + code);
        } catch (Exception e) { Toast.makeText(this, "Errore condivisione", Toast.LENGTH_SHORT).show(); }
    }
}
