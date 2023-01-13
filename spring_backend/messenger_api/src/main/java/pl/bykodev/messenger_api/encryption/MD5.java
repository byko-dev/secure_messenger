package pl.bykodev.messenger_api.encryption;

import org.springframework.beans.factory.annotation.Value;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {

    @Value("${md5.passphrase.salt}")
    private static String passphraseSalt;

    private static String md5(String salt, String plainText)
            throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");

        if (salt != null) {
            md.update(salt.getBytes());
        }
        md.update(plainText.getBytes());

        byte byteData[] = md.digest();

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16)
                    .substring(1));
        }
        return sb.toString();
    }

    public static String getSaltedMD5(String password) throws NoSuchAlgorithmException {
        return md5(passphraseSalt, password);
    }
}
