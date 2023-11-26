package com.GunterPro7uDerKatzenLord.Utils;

import com.GunterPro7uDerKatzenLord.Gui.CustomIngameUI;

import java.util.HashMap;
import java.util.Map;

public class MessageInformation {
    private static final Map<String, MessageInformation> instances = new HashMap<>();
    private final String message;
    private final int id;
    private int count;
    private long time;

    public MessageInformation(String message, int id) {
        this.message = message;
        this.id = id;
        count = 0;
        time = System.currentTimeMillis();
        instances.put(message, this);
    }

    public void count() {
        count++;
        time = System.currentTimeMillis();
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

    public void drawTimeInfoBox(int x, int y, boolean background) { // TODO das geht eine stunde nach
        CustomIngameUI customIngameUI = new CustomIngameUI(0xFF1E1E1E, 0xFF121212, Utils.formatTime(time, "HH:mm:ss"));
        customIngameUI.drawInfoBox(x, y, background);
    }

    public static Map<String, MessageInformation> getInstances() {
        return instances;
    }

    public static MessageInformation getInstance(String text) {
        return instances.get(text);
    }
}
