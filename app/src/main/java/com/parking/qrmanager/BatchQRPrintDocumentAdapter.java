package com.parking.qrmanager;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.pdf.PrintedPdfDocument;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BatchQRPrintDocumentAdapter extends PrintDocumentAdapter {

    private final List<BitmapItem> items;
    private final String company;
    private PrintedPdfDocument pdfDocument;

    public static class BitmapItem {
        public final android.graphics.Bitmap bitmap;
        public BitmapItem(android.graphics.Bitmap b) { this.bitmap = b; }
    }

    public BatchQRPrintDocumentAdapter(List<BitmapItem> items, String company) {
        this.items = items;
        this.company = company;
    }

    @Override
    public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes, CancellationSignal cancellationSignal, LayoutResultCallback callback, Bundle extras) {
        pdfDocument = new PrintedPdfDocument(null, newAttributes);
        if (cancellationSignal.isCanceled()) { callback.onLayoutCancelled(); return; }
        int pages = (int) Math.ceil(items.size() / 6.0);
        if (pages <= 0) pages = 1;
        PrintDocumentInfo info = new PrintDocumentInfo.Builder("QR_Batch_" + company + ".pdf")
                .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                .setPageCount(pages)
                .build();
        callback.onLayoutFinished(info, true);
    }

    @Override
    public void onWrite(PageRange[] pages, ParcelFileDescriptor destination, CancellationSignal cancellationSignal, WriteResultCallback callback) {
        try {
            int total = (int) Math.ceil(items.size() / 6.0);
            if (total <= 0) total = 1;
            for (int p = 0; p < total; p++) {
                PdfDocument.Page page = pdfDocument.startPage(p);
                Canvas canvas = page.getCanvas();
                int pageWidth = canvas.getWidth();
                int pageHeight = canvas.getHeight();
                canvas.drawColor(Color.WHITE);

                // layout 2x3
                int qrWidth = pageWidth / 2 - 60; // margins
                int qrHeight = (pageHeight - 160) / 3; // leave footer
                int marginX = 40;
                int marginY = 40;
                int spacingX = 20;
                int spacingY = 20;

                int startIndex = p * 6;
                for (int i = 0; i < 6; i++) {
                    int idx = startIndex + i;
                    if (idx >= items.size()) break;
                    int row = i / 2;
                    int col = i % 2;
                    int x = marginX + col * (qrWidth + spacingX);
                    int y = marginY + row * (qrHeight + spacingY);
                    android.graphics.Bitmap src = items.get(idx).bitmap;
                    android.graphics.Bitmap scaled = android.graphics.Bitmap.createScaledBitmap(src, qrWidth, qrHeight, true);
                    canvas.drawBitmap(scaled, x, y, null);
                }

                // footer
                Paint textPaint = new Paint();
                textPaint.setColor(Color.GRAY);
                textPaint.setTextSize(28);
                textPaint.setTextAlign(Paint.Align.CENTER);
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                canvas.drawText("Pagina " + (p + 1) + " di " + total + " - " + company + " - " + sdf.format(new Date()), pageWidth / 2f, pageHeight - 30, textPaint);

                pdfDocument.finishPage(page);
            }

            try (FileOutputStream out = new FileOutputStream(destination.getFileDescriptor())) {
                pdfDocument.writeTo(out);
            }
            callback.onWriteFinished(new PageRange[]{PageRange.ALL_PAGES});
        } catch (IOException e) {
            callback.onWriteFailed(e.toString());
        } finally {
            if (pdfDocument != null) pdfDocument.close();
        }
    }
}
