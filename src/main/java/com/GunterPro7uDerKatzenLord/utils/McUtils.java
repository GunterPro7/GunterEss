package com.GunterPro7uDerKatzenLord.utils;

import net.minecraft.client.gui.GuiTextField;

// This Class if for Utils related to Minecraft
public class McUtils {
    public static boolean isTextFieldHovered(GuiTextField textField, int mouseX, int mouseY) {
        return mouseX >= textField.xPosition && mouseX <= textField.xPosition + textField.width &&
                mouseY >= textField.yPosition && mouseY <= textField.yPosition + textField.height;
    }
}
