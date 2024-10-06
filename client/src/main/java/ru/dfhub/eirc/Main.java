package ru.dfhub.eirc;

import org.json.JSONObject;
import ru.dfhub.eirc.util.Encryption;

import java.io.IOException;
import java.security.InvalidKeyException;

public class Main {

    private static ServerConnection serverConnection;
    private static JSONObject config;

    public static void main(String[] args) {
        Gui.init();
        Gui.showWelcomeMessage();

        // Trying to load config
        try {
            Config.init();
            config = Config.getConfig();
        } catch (Exception e)
        {
            Gui.showNewMessage("An error occurred while reading the config!", Gui.MessageType.SYSTEM_ERROR);
            Gui.breakInput();
        }

        /*
        Trying to load encryption key
        This occurs separately from encryption to distinguish between missing a key and an invalid key.
        If there is no key, an attempt will be made to generate a new one.
         */
        try {
            Encryption.initKey();
        } catch (Encryption.EncryptionException e)
        {
            Gui.showNewMessage("You haven't set the encryption key!", Gui.MessageType.SYSTEM_ERROR);
            try {
                Encryption.generateNewKeyFile();
                Gui.showNewMessage("The new key is saved to the file new_key.txt", Gui.MessageType.SYSTEM_INFO);
            } catch (IOException ex)
            {
                Gui.showNewMessage("An error occurred while generating and saving a new key", Gui.MessageType.SYSTEM_ERROR);
            }
            Gui.breakInput();
        }

        /*
        Trying to initialize encryption.
        At this stage you can find out that the key is invalid
         */
        try {
            Encryption.initEncryption();
        } catch (InvalidKeyException e)
        {
            Gui.showNewMessage("Your encryption key is damaged or incorrect!", Gui.MessageType.SYSTEM_ERROR);
            Gui.showNewMessage("Run the program with an empty encryption key to generate a new one", Gui.MessageType.SYSTEM_INFO);
            Gui.breakInput();
        }

        /*
        Trying to connect to the server
         */
        try {
            serverConnection = new ServerConnection(config.getString("server-address"), config.getInt("server-port"));
        } catch (Exception e)
        {
            Gui.showNewMessage("Failed connect to the server!", Gui.MessageType.SYSTEM_ERROR);
            Gui.breakInput();
        }
        Gui.show();

        DataParser.handleOutputSession(true);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> DataParser.handleOutputSession(false)));
    }

    public static ServerConnection getServerConnection() {
        return serverConnection;
    }

    public static JSONObject getConfig() {
        return config;
    }
}