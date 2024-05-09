package com.GunterPro7uDerKatzenLord;

import com.GunterPro7uDerKatzenLord.gui.Align;
import com.GunterPro7uDerKatzenLord.utils.CollectionUtils;
import net.minecraft.util.EnumChatFormatting;

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
    public static final Value AUTO_HARP = new Value(true, 250);
    public static final Map<String, Setting> INFO_SETTINGS = CollectionUtils.mapOf(
            "Ping", new Setting(false),
            "Fps", new Setting(false),
            "Day", new Setting(false),
            "Time", new Setting(false),
            "Position", new Setting(false),
            "Facing", new Setting(false),
            "Lag", new Setting(false));

    public static final Map<String, Position> INFO_POSITIONS = CollectionUtils.mapOf(
            "Ping", new Position(false),
            "Fps", new Position(false),
            "Day", new Position(false),
            "Time", new Position(false),
            "X", new Position(false),
            "Y", new Position(false),
            "Z", new Position(false),
            "Facing", new Position(false),
            "Lag", new Position(false));
    public static EnumChatFormatting INFO_PREFIX_COLOR = EnumChatFormatting.GRAY;
    public static EnumChatFormatting INFO_VALUE_COLOR = EnumChatFormatting.GOLD;
    public static EnumChatFormatting INFO_SUFFIX_COLOR = EnumChatFormatting.WHITE;
    public static String INFO_TIME_FORMAT = "HH:mm:ss";
    public static String INFO_DATE_FORMAT = "dd.MM.yyyy";

    public static File settingsFile;

    private boolean enabled;
    protected boolean lacy;

    public Setting(boolean enabled) {
        this.enabled = enabled;
    }

    // Lacy = only update on .update();
    public void setLacy(boolean lacy) {
        this.lacy = lacy;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (!lacy) {
            update();
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public Setting() {
    }

    public void switchEnabled() {
        enabled = !enabled;
        if (!lacy) {
            update();
        }
    }

    public void update() {
        saveSettings();
    }

    public static class Position extends Setting {
        private int offsetX;
        private int offsetY;
        private Align align = Align.LEFT;

        public Position(boolean enabled, int offsetX, int offsetY) {
            super(enabled);
            this.offsetX = offsetX;
            this.offsetY = offsetY;
        }

        public Position(int offsetX, int offsetY) {
            this(true, offsetX, offsetY);
        }

        public Position(boolean enabled) {
            this(enabled, 50, 50);
        }

        public int getOffsetX() {
            return offsetX;
        }

        public void setOffsetX(int offsetX) {
            this.offsetX = offsetX;
            if (!lacy) {
                update();
            }
        }

        public int getOffsetY() {
            return offsetY;
        }

        public void setOffsetY(int offsetY) {
            this.offsetY = offsetY;
            if (!lacy) {
                update();
            }
        }

        public Align getAlign() {
            return align;
        }

        public void setAlign(Align align) {
            this.align = align;
            if (!lacy) {
                update();
            }
        }

        @Override
        public String toString() {
            return super.toString() + ";" + this.offsetX + ";" + this.offsetY + ";" + (this.align == null ? "LEFT" : this.align.name());
        }
    }

    public static class Value extends Setting {
        private int value;

        public Value(boolean enabled, int value) {
            super(enabled);
            this.value = value;
        }

        public int getValue() {
            return value;
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
                if (parts.length > 1) {
                    settings.put(parts[0], parts[1]);
                }
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
                if (o instanceof Map) {
                    Map<?, ?> map = (Map<?, ?>) o;

                    for (Map.Entry<?, ?> entry : map.entrySet()) {
                        initRawSetting(entry.getValue(), field.getName() + ";" + entry.getKey(), settings, field);
                    }
                } else if (o instanceof List) {
                    List<?> list = (List<?>) o;

                    int index = 0;
                    for (Object element : list) {
                        initRawSetting(element, field.getName() + ";" + index++, settings, field);
                    }
                } else {
                    initRawSetting(o, field.getName(), settings, field);
                }
            }
        }

    }

    private static void initRawSetting(Object object, String name, Map<String, String> settings, Field field) {
        try {
            initSetting(object, name, settings, field);
        } catch (Exception e) {
            System.out.println("GunterEss > Exception occured while reading Settings file:");
            e.printStackTrace();
        }
    }

    private static void initSetting(Object object, String name, Map<String, String> settings, Field field) {
        settings.forEach((curName, info) -> {
            if (name.equals(curName)) {
                String[] parts = info.split(";");
                if (object instanceof Setting) {
                    Setting setting = (Setting) object;
                    setting.enabled = Boolean.parseBoolean(parts[0]);

                    if (setting instanceof Position) {
                        Position pos = (Position) setting;
                        if (parts.length > 2) {
                            pos.offsetX = Integer.parseInt(parts[1]);
                            pos.offsetY = Integer.parseInt(parts[2]);
                            pos.align = Align.valueOf(parts[3]);
                        }

                    } else if (setting instanceof Value) {
                        Value v = (Value) setting;
                        if (parts.length > 1) {
                            v.value = Integer.parseInt(parts[1]);
                        }
                    }
                } else if (object instanceof EnumChatFormatting) {
                    try {
                        field.set(null, EnumChatFormatting.valueOf(parts[0]));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }

                } else if (object instanceof String) {
                    try {
                        Object o = field.get(null);
                        if (o instanceof Map) {
                            String key = name.split(";")[1];

                            for (Map.Entry<String, ?> entry : ((Map<String, ?>) o).entrySet()) {
                                if (entry.getKey().equals(key)) {
                                    if (entry.getValue() instanceof Setting) {
                                        if (parts.length > 0) {
                                            Setting setting = (Setting) entry.getValue();
                                            setting.enabled = Boolean.parseBoolean(parts[0]);
                                        }

                                    }
                                    if (entry.getValue() instanceof Position) {
                                        if (parts.length > 2) {
                                            Position position = (Position) entry.getValue();

                                            position.offsetX = Integer.parseInt(parts[1]);
                                            position.offsetY = Integer.parseInt(parts[2]);
                                            position.align = Align.valueOf(parts[3]);
                                        }
                                    }
                                }
                            }

                        } else {
                            field.set(null, parts[0]);
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    public static void saveSettings() {
        Field[] fields = Setting.class.getDeclaredFields();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(settingsFile))) {
            for (Field field : fields) {
                if (Modifier.isStatic(field.getModifiers())) {
                    Object o = field.get(null);
                    if (o instanceof Setting) {
                        writer.write(field.getName() + ": " + o + "\n");
                    } else if (o instanceof EnumChatFormatting) {
                        writer.write(field.getName() + ": " + ((EnumChatFormatting) o).name() + "\n");
                    } else if (o instanceof String) {
                        writer.write(field.getName() + ": " + o + "\n");
                    } else if (o instanceof Map) {
                        Map<?, ?> map = (Map<?, ?>) o;

                        for (Map.Entry<?, ?> entry : map.entrySet()) {
                            writer.write(field.getName() + ";" + entry.getKey() + ": " + entry.getValue() + "\n");
                        }
                    } else if (o instanceof List) {
                        List<?> list = (List<?>) o;

                        int index = 0;
                        for (Object curSetting : list) {
                            writer.write(field.getName() + ";" + index++ + ": " + curSetting + "\n");
                        }
                    }
                }
            }
        } catch (IOException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}