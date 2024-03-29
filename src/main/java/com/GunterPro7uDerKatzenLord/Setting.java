package com.GunterPro7uDerKatzenLord;

public class Setting {
    public static final Position COLLECTION_OVERLAY = new Position(false);
    public static final Position MONEY_OVERLAY = new Position(false);
    public static final Setting AUTO_KICKER_ENABLED = new Setting(true);
    public static final Setting COPY_CHAT_ENABLED = new Setting(true);
    public static final Setting STACK_CHAT_MESSAGES = new Setting(true);
    public static final Setting REMOVE_BLANK_LINES = new Setting(true);
    public static final Setting DONT_CHECK_USELESS_CHAT_MESSAGES = new Setting(true);
    public static final Setting COPY_WITH_STACK = new Setting(true);
    public static final Setting SEND_CHECK_FOR_7MESSAGE = new Setting(true);
    public static final Setting AUTO_FISHING = new Setting(false);
    public static final Setting ITEM_LORE_SCROLL = new Setting(true);
    public static final Position GEMSTONE_DISPLAY_POSITION = new Position(true, 50, 200);

    private boolean enabled;
    public Setting(boolean enabled) {
        this.enabled = enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public Setting() {}

    public void switchEnabled() {
        enabled = !enabled;
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
        }

        public int getOffsetY() {
            return offsetY;
        }

        public void setOffsetY(int offsetY) {
            this.offsetY = offsetY;
        }
    }
}