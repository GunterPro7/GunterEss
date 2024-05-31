package com.GunterPro7.gui;

import com.GunterPro7.listener.AdvancedChat;
import com.GunterPro7.utils.McUtils;

import static com.GunterPro7.Main.mc;

public enum Side {
    LEFT_TOP(true, true),
    RIGHT_TOP(false, true),
    LEFT_BOTTOM(true, false),
    RIGHT_BOTTOM(false, false);

    private final boolean left;
    private final boolean top;

    Side(boolean left, boolean top) {
        this.left = left;
        this.top = top;
    }

    public static Side calcSide(int x, int y) {
        int guiScale = McUtils.getGuiScale();
        return calcSide(x, y, mc.displayWidth / guiScale, mc.displayHeight / guiScale);
    }

    public static Side calcSide(int x, int y, int screenWidth, int screenHeight) {
        int width = screenWidth / 2;
        int height = screenHeight / 2;

        AdvancedChat.sendPrivateMessage("width: " + width + ", height: " + height);
        AdvancedChat.sendPrivateMessage("x: " + x + ", y: " + y);

        if (x <= width && y <= height) {
            return Side.LEFT_TOP;
        } else if (x > width && y <= height) {
            return Side.RIGHT_TOP;
        } else if (x > width && y > height) {
            return Side.RIGHT_BOTTOM;
        } else {
            return Side.LEFT_BOTTOM;
        }
    }

    public boolean isLeft() {
        return left;
    }

    public boolean isTop() {
        return top;
    }
}
