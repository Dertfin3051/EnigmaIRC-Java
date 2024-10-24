package ru.dfhub.eirc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import ru.dfhub.eirc.util.ResourcesReader;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Class for working with config
 */
public class Config {

    private static JSONObject config;

    private static final Logger logger = LogManager.getLogger(Config.class);

    public static void init() throws Exception {
        logger.info("Trying to init config");

        File configFile = new File("config.json");
        if (!configFile.exists()) {
            logger.error("Configuration file not found!");
            generateNewConfig();
            logger.info("Using default config");
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
        logger.debug("Generating new config file...");
        Files.writeString(Paths.get("config.json"), getDefaultConfig());
        logger.info("New config file generated successfully!");
    }

    /**
     * Get default config from resources
     * @return Config
     */
    private static String getDefaultConfig() throws Exception {
        return new ResourcesReader("config.json").readString();
    }

    /**
     * Get config from config.json file
     * @return Config
     */
    private static String readConfig() throws Exception {
        return Files.readString(Paths.get("config.json"));
    }
}
