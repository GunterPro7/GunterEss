package com.GunterPro7.utils;

import com.GunterPro7.gui.CustomIngameUI;
import com.GunterPro7.gui.Side;

import java.util.HashMap;
import java.util.Map;

public class MessageInformation {
    private static final Map<String, MessageInformation> instances = new HashMap<>();
    private final String message;
    private final String colorMessage;
    private final int id;
    private int count;
    private long time;

    public MessageInformation(String message, String colorMessage, int id) {
        this.message = message;
        this.colorMessage = colorMessage;
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

    public void drawTimeInfoBox(int x, int y, boolean background) {
        CustomIngameUI customIngameUI = new CustomIngameUI(0xFF1E1E1E, 0xFF121212, Utils.formatTime(time, "HH:mm:ss"));
        customIngameUI.drawInfoBox(x, y, background);
    }

    public static Map<String, MessageInformation> getInstances() {
        return instances;
    }

    public static MessageInformation getInstance(String text) {
        return instances.get(text);
    }

    public String getColorMessage() {
        return colorMessage;
    }
}
