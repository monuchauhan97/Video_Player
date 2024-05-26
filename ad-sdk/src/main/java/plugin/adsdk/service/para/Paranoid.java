package plugin.adsdk.service.para;

import android.util.Base64;

import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import io.michaelrocks.paranoid.Obfuscate;

@Obfuscate
public class Paranoid {
    private static final String q = "FjreovYAORuSyBYwKy9qQTVLD9lm9P0i";
    private static final String i = "rH5eQRMPQZfBzBAy";
    private static final String a = "AES";
    private static final String b = "AES/CBC/PKCS5Padding";

    public static String decrypt(String str) {
        return new String(decrypt(Base64.decode(str, Base64.DEFAULT)), StandardCharsets.US_ASCII);
    }

    private static byte[] decrypt(byte[] bArr) {
        SecretKeySpec secretKeySpec = new SecretKeySpec(q.getBytes(StandardCharsets.UTF_8), a);
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance(b);
            try {
                IvParameterSpec iv = new IvParameterSpec(Paranoid.i.getBytes(StandardCharsets.UTF_8));
                cipher.init(2, secretKeySpec, iv);
            } catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
                e.printStackTrace();
            }
            byte[] bArr2 = new byte[0];
            try {
                return cipher.doFinal(bArr);
            } catch (BadPaddingException | IllegalBlockSizeException e2) {
                e2.printStackTrace();
                return bArr2;
            }
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e4) {
            e4.printStackTrace();
            try {
                IvParameterSpec iv = new IvParameterSpec(Paranoid.i.getBytes(StandardCharsets.UTF_8));
                cipher.init(2, secretKeySpec, iv);
            } catch (InvalidKeyException | InvalidAlgorithmParameterException e5) {
                e5.printStackTrace();
            }
            try {
                return cipher.doFinal(bArr);
            } catch (BadPaddingException | IllegalBlockSizeException e6) {
                e6.printStackTrace();
            }
        }
        return bArr;
    }
}
