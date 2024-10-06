package ru.dfhub.eirc;

import org.json.JSONObject;
import ru.dfhub.eirc.util.Encryption;

import java.io.IOException;

public class Main {

    private static ServerConnection serverConnection;
    private static JSONObject config;

    public static void main(String[] args) {
        Gui.init();
        Gui.showWelcomeMessage();

        try {
            Config.init();
            config = Config.getConfig();
        } catch (Exception e)
        {
            Gui.breakInput();
            Gui.showNewMessage("An error occurred while reading the config!", Gui.MessageType.SYSTEM_ERROR);
            return;
        }

        try {
            Encryption.init();
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
        }

        Gui.reapplyTheme();
        Gui.show();

        try {
            serverConnection = new ServerConnection(config.getString("server-address"), config.getInt("server-port"));
        } catch (Exception e)
        {
            Gui.breakInput();
            Gui.showNewMessage("Failed connect to the server!", Gui.MessageType.SYSTEM_ERROR);
            return;
        }

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