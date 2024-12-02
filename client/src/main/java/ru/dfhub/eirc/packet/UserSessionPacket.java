package ru.dfhub.eirc.packet;

import org.json.JSONObject;

public record UserSessionPacket(String user, boolean isJoin) implements Packet {
    public static String getType() {
        return "user-session";
    }

    @Override
    public JSONObject serialize() {
        final JSONObject content = new JSONObject()
                .put("user", user)
                .put("status", isJoin ? "join" : "leave");

        return new JSONObject()
                .put("type", getType())
                .put("content", content);
    }

    public static UserSessionPacket deserialize(JSONObject object) {
        if (!object.getString("type").equals(getType()))
            throw new IllegalArgumentException("Attempt to deserialize a packet that is not user-message packet using user-message deserializer");

        final JSONObject content = object.getJSONObject("content");
        return new UserSessionPacket(content.getString("user"), parseStatus(content.getString("status")));
    }

    private static boolean parseStatus(String status) {
        if (status.equals("join")) return true;
        if (status.equals("leave")) return false;
        throw new IllegalArgumentException("Unknown status: " + status);
    }
}
