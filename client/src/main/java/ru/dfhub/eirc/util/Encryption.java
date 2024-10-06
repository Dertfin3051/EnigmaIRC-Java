package ru.dfhub.eirc.util;

import ru.dfhub.eirc.Main;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Encryption {

    /**
     * Various encryption related errors.
     * The essence of the errors is conveyed in the message
     */
    public static class EncryptionException extends Exception {
        public EncryptionException(String message) {
            super(message);
        }
    }

    private static SecretKey key;

    public static void init() throws EncryptionException {
        String keyString = Main.getConfig().getString("security-key");

        if (keyString.isEmpty()) throw new EncryptionException("Encryption key not specified");

        key = new SecretKeySpec(keyString.getBytes(), "AES");
    }

    public static SecretKey getKey() {
        return key;
    }

    public static String generateNewKey() {
        try {
            KeyGenerator keygen = KeyGenerator.getInstance("AES");
            keygen.init(256);
            return Base64.getEncoder().encodeToString(
                    keygen.generateKey().getEncoded()
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void generateNewKeyFile() throws IOException {
        Files.writeString(Paths.get("newkey.txt"), generateNewKey());
    }
}
