package pl.bykodev.messenger_api.encryption;

import java.util.Base64;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Aes256 extends AbstractAes256{
    private static String passwd;

    public static void setDefaultPassword(String password){
        passwd = password;
    }

    public static String encrypt(String input) throws Exception {

        return Base64.getEncoder().encodeToString(_encrypt(input.getBytes(UTF_8), passwd.getBytes(UTF_8)));

    }

    public static String decrypt(String crypted) throws Exception {
        return new String(_decrypt(Base64.getDecoder().decode(crypted), passwd.getBytes(UTF_8)), UTF_8);

    }
}
