package ru.dfhub.eirc;

import org.json.JSONObject;

/**
 * Class for working with data and processing it
 */
public class DataParser {

    /**
     * Types of incoming and outgoing messages
     */
    public enum MessageType {
        USER_MESSAGE, USER_SESSION
    }

    /**
     * Parse the incoming message and take the necessary action to work with it
     * @param data Raw data from server
     */
    public static void handleInputData(String data) {
        JSONObject dataObj = new JSONObject(data);

        switch (dataObj.getString("type")) {
            case "user-message" -> handleUserMessage(dataObj.getJSONObject("content"));
        }
    }

    /**
     * Collect a user message into a data type accepted by the client
     * @param message Message
     */
    public static void handleOutputMessage(String message) {
        // In ftr replace with sampler
        String template = "{\"type\":\"user-message\", \"content\":{\"user\":\"%s\", \"message\":\"%s\"}}";

        Main.getServerConnection().sendToServer(
                template.formatted(Main.getConfig().getString("username"), message)
        );
    }

    /**
     * Processing an incoming user message
     * @param data Data's "content" object
     */
    private static void handleUserMessage(JSONObject data) {
        String sender = data.getString("user");
        String message = data.getString("message"); // In ftr, decrypt and handle decryption errors here

        String formattedMessage = "%s -> %s".formatted(sender, message); // In ftr, handle timestamps here

        if (sender.equals(Main.getConfig().getString("username"))) {
            Gui.showNewMessage(formattedMessage, Gui.MessageType.SELF_USER_MESSAGE);
        } else {
            Gui.showNewMessage(formattedMessage, Gui.MessageType.USER_MESSAGE);
        }
    }
}
