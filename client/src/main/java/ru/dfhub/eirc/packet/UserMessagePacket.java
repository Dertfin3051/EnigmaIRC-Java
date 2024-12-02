package ru.dfhub.eirc.packet;

import org.json.JSONObject;

public record UserMessagePacket(String user, String message) implements Packet {
    public static String getType() {
        return "user-message";
    }

    @Override
    public JSONObject serialize() {
        final JSONObject content = new JSONObject()
                .put("user", user)
                .put("message", message);

        return new JSONObject()
                .put("type", getType())
                .put("content", content);
    }

    public static UserMessagePacket deserialize(JSONObject object) {
        if (!object.getString("type").equals(getType()))
            throw new IllegalArgumentException("Attempt to deserialize a packet that is not user-message packet using user-message deserializer");

        final JSONObject content = object.getJSONObject("content");
        return new UserMessagePacket(content.getString("user"), content.getString("message"));
    }
}
