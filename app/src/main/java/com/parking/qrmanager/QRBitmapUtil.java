package com.parking.qrmanager;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class QRBitmapUtil {
    public static Bitmap generateQRCodeWithText(String content, int width, int height) throws WriterException {
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
}
