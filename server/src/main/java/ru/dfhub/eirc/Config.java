package ru.dfhub.eirc;

import io.github.Dertfin3051.Color;
import io.github.Dertfin3051.Colored;
import org.json.JSONObject;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Config {

    private static JSONObject config;

    public static void init() throws Exception {
        new Colored("Trying to read config.json...", Color.YELLOW).safePrint();

        File configFile = new File("config.json");
        if (!configFile.exists()) {
            new Colored("Configuration file not found!", Color.RED).safePrint();
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
        new Colored("Generating new config file...", Color.YELLOW).safePrint();
        Files.writeString(Paths.get("config.json"), getDefaultConfig());
        new Colored("New config file generated successfully!", Color.GREEN).safePrint();
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
