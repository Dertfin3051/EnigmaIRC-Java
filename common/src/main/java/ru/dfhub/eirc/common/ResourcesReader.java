package ru.dfhub.eirc.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Class for reading a "resources" file
 */
public class ResourcesReader {

    private final BufferedReader reader;

    public ResourcesReader(String resourcesFilePath) {
        try {
            InputStream inStream = getClass().getClassLoader().getResourceAsStream(resourcesFilePath);
            reader = new BufferedReader(new InputStreamReader(inStream));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Read file as String. Returns empty string if something went wrong
     * @return File contents
     */
    public String readString() {
        StringBuilder builder = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return builder.toString();
    }
}
