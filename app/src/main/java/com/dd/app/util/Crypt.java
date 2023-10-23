package com.dd.app.util;

import static com.dd.app.network.APIConstants.URLs.SECRET_KEY;

import android.os.Build;
import android.util.Log;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Crypt {

    private static final String ALGO = "AES"; // Default uses ECB PKCS5Padding

    public static String encrypt(String Data, String secret) throws Exception {
        Key key = generateKey(secret);
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(Data.getBytes());
        String encryptedValue = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            encryptedValue = Base64.getEncoder().encodeToString(encVal);
        }else
        {
            encryptedValue = new String(android.util.Base64.encode(encVal, android.util.Base64.DEFAULT), StandardCharsets.UTF_8);
        }
        return encryptedValue;
    }

    public static String decrypt(String strToDecrypt, String secret) {
        try {
            Key key = generateKey(secret);
            Cipher cipher = Cipher.getInstance(ALGO);
            cipher.init(Cipher.DECRYPT_MODE, key);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
            }else
            {
                return new String(cipher.doFinal(android.util.Base64.decode(strToDecrypt, android.util.Base64.DEFAULT)), StandardCharsets.UTF_8);
            }
        } catch (Exception e) {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }

    private static Key generateKey(String secret) throws Exception {
        byte[] decoded = new byte[0];
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            decoded = Base64.getDecoder().decode(secret.getBytes());
        }else
        {
            decoded = android.util.Base64.decode(secret.getBytes(), android.util.Base64.DEFAULT);
        }
        Key key = new SecretKeySpec(decoded, ALGO);
        return key;
    }

    public static String decodeKey(String str) {
        byte[] decoded = new byte[0];
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            decoded = Base64.getDecoder().decode(str.getBytes());
        }else
        {
            decoded = android.util.Base64.decode(str.getBytes(), android.util.Base64.DEFAULT);
        }
        return new String(decoded);
    }

    public static String encodeKey(String str) {
        byte[] encoded = new byte[0];
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            encoded = Base64.getEncoder().encode(str.getBytes());
        }else
        {
            encoded = android.util.Base64.encode(str.getBytes(), android.util.Base64.DEFAULT);
        }
        return new String(encoded);
    }
    //sample code
    /*String secretKey = SECRET_KEY;
    String encodedBase64Key = encodeKey(secretKey);
        UiUtils.log(TAG,"EncodedBase64Key = " + encodedBase64Key);

    String toEncrypt = "21";//""Please encrypt this message!";
        UiUtils.log(TAG,"Plain text = " + toEncrypt);

    // AES Encryption based on above secretKey
    String encrStr = null;
        try {
        encrStr = Crypt.encrypt(toEncrypt, encodedBase64Key);
    } catch (Exception e) {
        UiUtils.log(TAG, Log.getStackTraceString(e));
    }
        UiUtils.log(TAG,"Cipher Text: Encryption of str = " + encrStr);

    // AES Decryption based on above secretKey
    String decrStr = Crypt.decrypt(encrStr, encodedBase64Key);
        UiUtils.log(TAG,"Decryption of str = " + decrStr);*/
}
