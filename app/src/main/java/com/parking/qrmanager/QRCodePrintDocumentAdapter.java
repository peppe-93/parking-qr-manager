package com.parking.qrmanager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.pdf.PrintedPdfDocument;
import java.io.FileOutputStream;
import java.io.IOException;

public class QRCodePrintDocumentAdapter extends PrintDocumentAdapter {

    private Context context;
    private Bitmap qrBitmap;
    private String qrCode;
    private PrintedPdfDocument pdfDocument;

    public QRCodePrintDocumentAdapter(Context context, Bitmap qrBitmap, String qrCode) {
        this.context = context;
        this.qrBitmap = qrBitmap;
        this.qrCode = qrCode;
    }

    @Override
    public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes,
                        CancellationSignal cancellationSignal, LayoutResultCallback callback,
                        Bundle extras) {

        // Crea un nuovo documento PDF con gli attributi specificati
        pdfDocument = new PrintedPdfDocument(context, newAttributes);

        // Controlla se l'operazione è stata cancellata
        if (cancellationSignal.isCanceled()) {
            callback.onLayoutCancelled();
            return;
        }

        // Informazioni sul documento
        PrintDocumentInfo.Builder builder = new PrintDocumentInfo.Builder("QRCode_" + qrCode + ".pdf")
                .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                .setPageCount(1);

        PrintDocumentInfo info = builder.build();
        callback.onLayoutFinished(info, true);
    }

    @Override
    public void onWrite(PageRange[] pages, ParcelFileDescriptor destination,
                       CancellationSignal cancellationSignal, WriteResultCallback callback) {

        try {
            // Inizia una pagina
            PdfDocument.Page page = pdfDocument.startPage(0);

            // Controlla se l'operazione è stata cancellata
            if (cancellationSignal.isCanceled()) {
                callback.onWriteCancelled();
                pdfDocument.close();
                return;
            }

            // Disegna il contenuto sulla pagina
            drawQRCodeOnPage(page);

            // Finisce la pagina
            pdfDocument.finishPage(page);

            // Scrive il documento PDF nel file di destinazione
            try (FileOutputStream out = new FileOutputStream(destination.getFileDescriptor())) {
                pdfDocument.writeTo(out);
            }

            callback.onWriteFinished(pages);

        } catch (IOException e) {
            callback.onWriteFailed(e.toString());
        } finally {
            if (pdfDocument != null) {
                pdfDocument.close();
            }
        }
    }

    private void drawQRCodeOnPage(PdfDocument.Page page) {
        Canvas canvas = page.getCanvas();
        
        // Dimensioni della pagina
        int pageWidth = canvas.getWidth();
        int pageHeight = canvas.getHeight();
        
        // Sfondo bianco
        canvas.drawColor(Color.WHITE);
        
        // Calcola le dimensioni e la posizione del QR code
        int qrSize = Math.min(pageWidth - 200, pageHeight - 300); // Margini di 100px per lato, spazio per testo
        int qrX = (pageWidth - qrSize) / 2;
        int qrY = 100; // Margine superiore
        
        // Ridimensiona il bitmap del QR code se necessario
        Bitmap scaledQR = Bitmap.createScaledBitmap(qrBitmap, qrSize, qrSize, true);
        
        // Disegna il QR code
        canvas.drawBitmap(scaledQR, qrX, qrY, null);
        
        // Disegna il testo sotto il QR code
        Paint textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(48);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setAntiAlias(true);
        
        // Titolo
        Paint titlePaint = new Paint(textPaint);
        titlePaint.setTextSize(56);
        titlePaint.setFakeBoldText(true);
        canvas.drawText("QR CODE PARKING", pageWidth / 2f, 80, titlePaint);
        
        // Codice QR
        int textY = qrY + qrSize + 80;
        canvas.drawText("Codice: " + qrCode, pageWidth / 2f, textY, textPaint);
        
        // Data di generazione
        textPaint.setTextSize(36);
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", java.util.Locale.getDefault());
        String dateText = "Generato il: " + sdf.format(new java.util.Date());
        canvas.drawText(dateText, pageWidth / 2f, textY + 60, textPaint);
        
        // Istruzioni
        textPaint.setTextSize(28);
        textPaint.setColor(Color.GRAY);
        String instructions = "Applicare sulla postazione e scansionare per verificare l'occupazione";
        canvas.drawText(instructions, pageWidth / 2f, textY + 120, textPaint);
    }

    @Override
    public void onFinish() {
        super.onFinish();
        if (pdfDocument != null) {
            pdfDocument.close();
        }
    }
}