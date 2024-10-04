package ru.dfhub.eirc;

import org.json.JSONObject;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Config {

    private static JSONObject config;

    public static void init() throws Exception {

        File configFile = new File("config.json");
        if (!configFile.exists()) {
            Gui.breakInput();
            Gui.showNewMessage("Configuration file not found!", Gui.MessageType.SYSTEM_ERROR);
            Gui.showNewMessage("A new configuration file has been generated. Set it up and restart the program", Gui.MessageType.SYSTEM_INFO);

            generateNewConfig();
            config = new JSONObject(getDefaultConfig());

            return;
        }

        config = new JSONObject(readConfig());
    }

    /**
     * Get config (access method)
     * @return Config
     */
    public static JSONObject getConfig() {
        return config;
    }

    /**
     * Generate new config.json file
     */
    private static void generateNewConfig() throws Exception {
        Files.writeString(Paths.get("config.json"), getDefaultConfig());
    }

    /**
     * Get default config from resources
     * @return Config
     */
    private static String getDefaultConfig() throws Exception {
        return Files.readString(
                Paths.get(Main.class.getClassLoader().getResource("config.json").toURI())
        );
    }

    /**
     * Get config from config.json file
     * @return Config
     */
    private static String readConfig() throws Exception {
        return Files.readString(Paths.get("config.json"));
    }
}
