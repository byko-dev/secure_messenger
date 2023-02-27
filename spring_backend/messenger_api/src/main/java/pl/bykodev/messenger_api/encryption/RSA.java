package pl.bykodev.messenger_api.encryption;

import org.springframework.stereotype.Component;
import pl.bykodev.messenger_api.pojos.RsaKeys;

import java.security.*;
import java.util.Base64;

@Component
public class RSA {

    private KeyPairGenerator generator;
    private Base64.Encoder encoder = Base64.getEncoder();

    private Base64.Decoder decoder = Base64.getDecoder();

    public RsaKeys generateRSAKeys(String secureRandomStr){
        try {
            generator = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        if(secureRandomStr.isBlank()) {
            SecureRandom secureRandom = new SecureRandom();
            secureRandom.nextBytes(decoder.decode(secureRandomStr));

            generator.initialize(2048, secureRandom);
        }else{
            generator.initialize(2048);
        }
        KeyPair pair = generator.generateKeyPair();

        PrivateKey privateKey = pair.getPrivate();
        PublicKey publicKey = pair.getPublic();

        return new RsaKeys(getPrivateKey(privateKey), getPublicKey(publicKey));
    }

    private String getPublicKey(PublicKey publicKey){
        return "-----BEGIN RSA PUBLIC KEY-----\n" +
                encoder.encodeToString(publicKey.getEncoded()) +
                "\n-----END RSA PUBLIC KEY-----\n";
    }

    private String getPrivateKey(PrivateKey privateKey){
        return "-----BEGIN RSA PRIVATE KEY-----\n" +
                encoder.encodeToString(privateKey.getEncoded()) +
                "\n-----END RSA PRIVATE KEY-----\n";
    }
}
