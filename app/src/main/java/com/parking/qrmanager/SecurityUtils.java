package com.parking.qrmanager;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

public class SecurityUtils {
    private static final String PREF_NAME = "sec_prefs";
    private static final String KEY_SECRET = "qr_hmac_key";

    public static String getOrCreateHmacKey(Context ctx) throws Exception {
        String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
        SharedPreferences prefs = EncryptedSharedPreferences.create(
                PREF_NAME,
                masterKeyAlias,
                ctx,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        );
        String k = prefs.getString(KEY_SECRET, null);
        if (k == null) {
            byte[] key = new byte[32];
            new java.security.SecureRandom().nextBytes(key);
            String base64 = android.util.Base64.encodeToString(key, android.util.Base64.NO_WRAP);
            prefs.edit().putString(KEY_SECRET, base64).apply();
            return base64;
        }
        return k;
    }

    public static String hmacSha256Base64(String keyBase64, String data) throws Exception {
        byte[] key = android.util.Base64.decode(keyBase64, android.util.Base64.NO_WRAP);
        javax.crypto.Mac mac = javax.crypto.Mac.getInstance("HmacSHA256");
        javax.crypto.spec.SecretKeySpec sk = new javax.crypto.spec.SecretKeySpec(key, "HmacSHA256");
        mac.init(sk);
        byte[] out = mac.doFinal(data.getBytes(java.nio.charset.StandardCharsets.UTF_8));
        return android.util.Base64.encodeToString(out, android.util.Base64.NO_WRAP);
    }
}
