package ru.dfhub.eirc;

import org.json.JSONObject;

public class DataParser {

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
        Gui.showNewMessage(data, Gui.MessageType.USER_MESSAGE);
    }

    private static void handleUserMessage(JSONObject data) {
        // Format message to string, check if it's self message and show it correctly
    }
}
