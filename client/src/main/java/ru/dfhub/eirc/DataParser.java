package ru.dfhub.eirc;

import org.json.JSONObject;
import ru.dfhub.eirc.util.Encryption;
import ru.dfhub.eirc.util.NotificationSound;
import ru.dfhub.eirc.util.ResourcesReader;

/**
 * Class for working with data and processing it
 */
public class DataParser {

    /**
     * Types of incoming and outgoing messages
     */
    public enum MessageType {
        USER_MESSAGE("user_message"), // User text messages
        USER_SESSION("user_session"); // Messages about user join/leave

        private String fileName;

        MessageType(String fileName) {
            this.fileName = fileName;
        }

        private String getResourcesPath() {
            return "message_templates/%s.json".formatted(this.fileName);
        }

        public String getTemplate() {
            return new ResourcesReader(this.getResourcesPath()).readString().replace("\n", "");
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
            e.printStackTrace();
            return;
        }

        String encryptedMessage;
        try {
            encryptedMessage = Encryption.encrypt(message);
        } catch (Exception e) {
            Gui.showNewMessage("There was an error sending the message (encrypt process)", Gui.MessageType.SYSTEM_ERROR);
            e.printStackTrace();
            return;
        }

        Main.getServerConnection().sendToServer(template
            .replace("%user%", Main.getConfig().getString("username"))
            .replace("%message%", encryptedMessage)
        );
    }

    /**
     * Process and send a message about your session (join/leave)
     * @param isJoin Is join
     */
    public static void handleOutputSession(boolean isJoin) {
        String status = isJoin ? "join" : "leave";

        String template;
        try {
            template = MessageType.USER_SESSION.getTemplate();
        } catch (Exception e) {
            Gui.showNewMessage("There was an error sending the session status (receiving template)", Gui.MessageType.SYSTEM_ERROR);
            e.printStackTrace();
            return;
        }

        Main.getServerConnection().sendToServer(template
                .replace("%user%", Main.getConfig().getString("username"))
                .replace("%status%", status)
        );
    }

    /**
     * Processing an incoming user message
     * @param data Data's "content" object
     */
    private static void handleUserMessage(JSONObject data) {
        String sender = data.getString("user");
        String encryptedMessage = data.getString("message"); // In ftr, decrypt and handle decryption errors here

        String message;
        try {
            message = Encryption.decrypt(encryptedMessage);
        } catch (Exception e) {
            Gui.showNewMessage("Failed to decrypt the incoming message! (wrong encryption key)", Gui.MessageType.SYSTEM_ERROR);
            return;
        }

        String formattedMessage = "%s -> %s".formatted(sender, message); // In ftr, handle timestamps here

        if (sender.equals(Main.getConfig().getString("username"))) {
            Gui.showNewMessage(formattedMessage, Gui.MessageType.SELF_USER_MESSAGE);
        } else {
            Gui.showNewMessage(formattedMessage, Gui.MessageType.USER_MESSAGE);
        }
        Gui.scrollDown();
        if (Gui.isMinimized()) NotificationSound.play();
    }

    /**
     * Handle input user-session(join/leave) message and show it
     * @param data Data's "content" object
     */
    private static void handleUserSession(JSONObject data) {
        String user = data.getString("user");
        String status = data.getString("status").equals("join") ? "joined!" : "left.";

        String formattedMessage = "%s %s".formatted(user, status);

        Gui.showNewMessage(formattedMessage, Gui.MessageType.USER_SESSION);
        Gui.scrollDown();
    }
}
