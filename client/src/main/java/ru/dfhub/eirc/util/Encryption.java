package ru.dfhub.eirc.util;

import ru.dfhub.eirc.Gui;
import ru.dfhub.eirc.Main;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
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
    private static Cipher encryptCipher;
    private static Cipher decryptCipher;

    /**
     * Getting the encryption key from the config
     * @throws EncryptionException Encryption key is empty
     */
    public static void initKey() throws EncryptionException {
        String keyString = Main.getConfig().getString("security-key");

        if (keyString.isEmpty()) throw new EncryptionException("Encryption key not specified");

        key = new SecretKeySpec(
                Base64.getDecoder().decode(keyString),
                "AES");
    }

    /**
     * Initializing encryption and decryption methods
     * @throws InvalidKeyException Invalid key
     */
    public static void initEncryption() throws InvalidKeyException {
        try {
            encryptCipher = Cipher.getInstance("AES");
            decryptCipher = Cipher.getInstance("AES");
        } catch (Exception e) {
            Gui.breakInput();
            Gui.showNewMessage("An unexpected error occurred while initializing encryption (%s)".formatted(e.getMessage()), Gui.MessageType.SYSTEM_ERROR);
        }

        encryptCipher.init(Cipher.ENCRYPT_MODE, getKey());
        decryptCipher.init(Cipher.DECRYPT_MODE, getKey());
    }

    public static SecretKey getKey() {
        return key;
    }

    /**
     * Generate new valid key and encode id to String(base64)
     * @return Encoded key
     */
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

    /**
     * Generate new key and write it to file
     * @throws IOException Errors when writing key to file
     */
    public static void generateNewKeyFile() throws IOException {
        Files.writeString(Paths.get("newkey.txt"), "New encryption key: %s".formatted(generateNewKey()));
    }
}
