package ru.dfhub.eirc;

import org.json.JSONObject;
import ru.dfhub.eirc.util.Encryption;

import java.util.Arrays;
import java.util.List;

public class Main {

    private static ServerConnection serverConnection;
    private static JSONObject config;

    public static void main(String[] args) {
        // Key generator
        for (int i = 0; i < args.length; i++) {
            if (List.of(
                    "-keygen",
                    "--keygen",
                    "-generate",
                    "--generate",
                    "-g"
            ).contains(args[i])) {
                if (i+2 > args.length) { Encryption.generateEncryptionKeys(1); return; } // Boolean arg
                Encryption.generateEncryptionKeys(Integer.parseInt(args[i+1])); return;
            }
        }


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
            Encryption.showNullKeyErrorAndGenerateNewOne();
        } catch (IllegalArgumentException e) {
            Encryption.showIncorrectKeyError();
        }

        /*
        Trying to initialize encryption.
        At this stage you can find out that the key is invalid
         */
        try {
            Encryption.initEncryption();
        } catch (Exception e)
        {
            Encryption.showIncorrectKeyError();
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

    public static void handleServerShutdown() {
        Gui.showNewMessage("The server has shut down!", Gui.MessageType.SYSTEM_ERROR);
        Gui.breakInput();
    }
}