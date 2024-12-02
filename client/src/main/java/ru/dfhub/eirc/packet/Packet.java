package ru.dfhub.eirc.packet;

import org.json.JSONObject;

public interface Packet {
    static String getType() { return "packet"; }
    JSONObject serialize();
    static Packet deserialize(JSONObject object) { return null; }

    default String toStr() {
        return serialize().toString();
    }
}
