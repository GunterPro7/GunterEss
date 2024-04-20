package com.GunterPro7uDerKatzenLord;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public class Setting {
    public static final Position COLLECTION_OVERLAY = new Position(false);
    public static final Position MONEY_OVERLAY = new Position(false);
    public static final Setting AUTO_KICKER = new Setting(true);
    public static final Setting COPY_CHAT = new Setting(true);
    public static final Setting STACK_CHAT_MESSAGES = new Setting(true);
    public static final Setting REMOVE_BLANK_LINES = new Setting(true);
    public static final Setting DONT_CHECK_USELESS_CHAT_MESSAGES = new Setting(true);
    public static final Setting COPY_WITH_STACK = new Setting(true);
    public static final Setting SEND_CHECK_FOR_7MESSAGE = new Setting(true);
    public static final Setting AUTO_FISHING = new Setting(false);
    public static final Setting ITEM_LORE_SCROLL = new Setting(true);
    public static final Position GEMSTONE_DISPLAY = new Position(true, 50, 200);
    public static final Setting AUTO_UPDATES = new Setting(true);
    public static final Setting HARP_ACTIVE = new Setting(false);

    public static File settingsFile;

    private boolean enabled;
    public Setting(boolean enabled) {
        this.enabled = enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        saveSettings();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public Setting() {}

    public void switchEnabled() {
        enabled = !enabled;
        saveSettings();
    }

    public static class Position extends Setting {
        private int offsetX;
        private int offsetY;

        public Position(boolean enabled, int offsetX, int offsetY) {
            super(enabled);
            this.offsetX = offsetX;
            this.offsetY = offsetY;
        }

        public Position(int offsetX, int offsetY) {
            this(true, offsetX, offsetY);
        }

        public Position(boolean enabled) {
            this(enabled, 0, 0);
        }

        public int getOffsetX() {
            return offsetX;
        }

        public void setOffsetX(int offsetX) {
            this.offsetX = offsetX;
            saveSettings();
        }

        public int getOffsetY() {
            return offsetY;
        }

        public void setOffsetY(int offsetY) {
            this.offsetY = offsetY;
            saveSettings();
        }

        @Override
        public String toString() {
            return super.toString() + ";" + this.offsetX + ";" + this.offsetY;
        }
    }

    @Override
    public String toString() {
        return String.valueOf(this.enabled);
    }

    private static Map<String, String> initSettingsFile(File settingsFile) throws IOException {
        if (!settingsFile.exists()) {
            settingsFile.createNewFile();
            return new HashMap<>();
        }
        Map<String, String> settings = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(settingsFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(": ");
                settings.put(parts[0], parts[1]);
            }
        }

        return settings;
    }

    public static void initSettings() throws IllegalAccessException, IOException {
        settingsFile = new File(Main.configDirectory.getAbsolutePath() + "/settings.txt");
        Map<String, String> settings = initSettingsFile(settingsFile);

        Field[] fields = Setting.class.getDeclaredFields();

        for (Field field : fields) {
            if (Modifier.isStatic(field.getModifiers())) {
                Object o = field.get(null);
                if (o instanceof Setting) {
                    Setting setting = (Setting) o;
                    String fieldName = field.getName();

                    settings.forEach((name, info) -> {
                        if (fieldName.equals(name)) {
                            String[] parts = info.split(";");
                            setting.enabled = Boolean.parseBoolean(parts[0]);

                            if (setting instanceof Position) {
                                Position pos = (Position) setting;
                                pos.offsetX = Integer.parseInt(parts[1]);
                                pos.offsetX = Integer.parseInt(parts[2]);
                            }
                        }
                    });
                }
            }
        }

    }

    public static void saveSettings() {
        Field[] fields = Setting.class.getDeclaredFields();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(settingsFile))) {
            for (Field field : fields) {
                if (Modifier.isStatic(field.getModifiers())) {
                    Object o = field.get(null);
                    if (o instanceof Setting) {
                        writer.write(field.getName() + ": " + o + "\n");
                    }
                }
            }
        } catch (IOException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}