package com.GunterPro7uDerKatzenLord.Utils;

import java.util.HashMap;
import java.util.Map;

public class MessageInformation {
    public static Map<String, MessageInformation> instances = new HashMap<>();
    private final String message;
    private final int id;
    private int count;
    private final long time;

    public MessageInformation(String message, int id) {
        this.message = message;
        this.id = id;
        count = 0;
        time = System.currentTimeMillis();
        instances.put(message, this);
    }

    public void count() {
        count++;
    }

    public String getMessage() {
        return message;
    }

    public int getId() {
        return id;
    }

    public int getCount() {
        return count;
    }

    public long getTime() {
        return time;
    }
}
