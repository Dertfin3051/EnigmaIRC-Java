package ru.dfhub.eirc;

import org.json.JSONObject;
import ru.dfhub.eirc.packet.UserMessagePacket;
import ru.dfhub.eirc.packet.UserSessionPacket;
import ru.dfhub.eirc.util.Encryption;
import ru.dfhub.eirc.util.NotificationSound;

/**
 * Class for working with data and processing it
 */
public class DataParser {
    /**
     * Parse the incoming message and take the necessary action to work with it
     * @param data Raw data from server
     */
    public static void handleInputData(String data) {
        JSONObject dataObj;
        try {
            dataObj = new JSONObject(data);
        } catch (Exception e) { return; } // Null message from server


        switch (dataObj.getString("type")) {
            case "user-message" -> handleUserMessage(UserMessagePacket.deserialize(dataObj));
            case "user-session" -> handleUserSession(UserSessionPacket.deserialize(dataObj));
            case "server-shutdown" -> Main.handleServerShutdown();
        }
    }

    /**
     * Collect a user message into a data type accepted by the client
     * @param message Message
     */
    public static void handleOutputMessage(String message) {
        String encryptedMessage;
        try {
            encryptedMessage = Encryption.encrypt(message);
        } catch (Exception e) {
            Gui.showNewMessage("There was an error sending the message (encrypt process)", Gui.MessageType.SYSTEM_ERROR);
            e.printStackTrace();
            return;
        }

        String msg = new UserMessagePacket(Main.getConfig().getString("username"), encryptedMessage).toStr();
        Main.getServerConnection().sendToServer(msg);
    }

    /**
     * Process and send a message about your session (join/leave)
     * @param isJoin Is join
     */
    public static void handleOutputSession(boolean isJoin) {
        UserSessionPacket msg = new UserSessionPacket(Main.getConfig().getString("username"), isJoin);
        Main.getServerConnection().sendToServer(msg.toStr());
    }

    /**
     * Processing an incoming user message
     * @param data Data's "content" object
     */
    private static void handleUserMessage(UserMessagePacket data) {
        String sender = data.user();
        String encryptedMessage = data.message(); // In ftr, decrypt and handle decryption errors here

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
        if (Gui.isMinimized() && Main.getConfig().optBoolean("notification-sounds", true)) NotificationSound.play();
    }

    /**
     * Handle input user-session(join/leave) message and show it
     * @param data Data's "content" object
     */
    private static void handleUserSession(UserSessionPacket data) {
        String user = data.user();
        String status = data.isJoin() ? "joined!" : "left.";

        String formattedMessage = "%s %s".formatted(user, status);

        Gui.showNewMessage(formattedMessage, Gui.MessageType.USER_SESSION);
        Gui.scrollDown();
    }
}
