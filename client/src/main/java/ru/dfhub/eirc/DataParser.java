package ru.dfhub.eirc;

import org.json.JSONObject;

import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Class for working with data and processing it
 */
public class DataParser {

    /**
     * Types of incoming and outgoing messages
     */
    public enum MessageType {
        USER_MESSAGE("user_message"),
        USER_SESSION("user_session");

        private String fileName;

        MessageType(String fileName) {
            this.fileName = fileName;
        }

        private String getResourcesPath() {
            return "message_templates/%s.json".formatted(this.fileName);
        }

        public String getTemplate() throws Exception {
            return Files.readString(
                    Paths.get(Main.class.getClassLoader().getResource(this.getResourcesPath()).toURI())
            ).replace("\n", "");
        }
    }

    /**
     * Parse the incoming message and take the necessary action to work with it
     * @param data Raw data from server
     */
    public static void handleInputData(String data) {
        JSONObject dataObj = new JSONObject(data);

        switch (dataObj.getString("type")) {
            case "user-message" -> handleUserMessage(dataObj.getJSONObject("content"));
            case "user-session" -> handleUserSession(dataObj.getJSONObject("content"));
        }
    }

    /**
     * Collect a user message into a data type accepted by the client
     * @param message Message
     */
    public static void handleOutputMessage(String message) {
        String template;
        try {
            template = MessageType.USER_MESSAGE.getTemplate();
        } catch (Exception e) {
            Gui.showNewMessage("There was an error sending the message (receiving template)", Gui.MessageType.SYSTEM_ERROR);
            return;
        }


        Main.getServerConnection().sendToServer(template
            .replace("%user%", Main.getConfig().getString("username"))
            .replace("%message%", message)
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

    private static void handleUserSession(JSONObject data) {
        String user = data.getString("user");
        String status = data.getString("status").equals("join") ? "joined!" : "left.";

        String formattedMessage = "%s %s".formatted(user, status);

        Gui.showNewMessage(formattedMessage, Gui.MessageType.USER_SESSION);
    }
}
