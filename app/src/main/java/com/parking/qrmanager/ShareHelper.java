package com.parking.qrmanager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import java.io.File;
import java.io.FileOutputStream;

public class ShareHelper {

    public static boolean shareImage(AppCompatActivity activity, Bitmap bmp, String nameBase) {
        try {
            File cacheDir = new File(activity.getCacheDir(), "share");
            if (!cacheDir.exists()) cacheDir.mkdirs();
            File out = new File(cacheDir, nameBase + ".png");
            try (FileOutputStream fos = new FileOutputStream(out)) {
                bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
            }
            Uri uri = FileProvider.getUriForFile(activity, "com.parking.qrmanager.fileprovider", out);
            Intent it = new Intent(Intent.ACTION_SEND);
            it.setType("image/png");
            it.putExtra(Intent.EXTRA_STREAM, uri);
            it.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            if (it.resolveActivity(activity.getPackageManager()) != null) {
                activity.startActivity(Intent.createChooser(it, "Condividi QR"));
                return true;
            } else {
                Toast.makeText(activity, "Nessuna app disponibile per condividere", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (Exception e) {
            Toast.makeText(activity, "Errore condivisione: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
