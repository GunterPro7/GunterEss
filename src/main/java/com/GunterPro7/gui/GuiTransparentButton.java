package com.GunterPro7.gui;

import com.GunterPro7.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.EnumChatFormatting;
import scala.collection.generic.BitOperations;

public class GuiTransparentButton extends GuiButton {
    private boolean clicked;
    private final String content;

    public GuiTransparentButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText, boolean clicked) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
        this.content = buttonText;
        this.clicked = clicked;
        this.displayString = clicked ? Setting.INFO_VALUE_COLOR + content : Setting.INFO_PREFIX_COLOR + content;
    }

    public GuiTransparentButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
        this(buttonId, x, y, widthIn, heightIn, buttonText, false);
    }

    public boolean isClicked() {
        return clicked;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        Gui.drawRect(xPosition, yPosition, xPosition + width, yPosition + height, Integer.MIN_VALUE);
        this.drawCenteredString(mc.fontRendererObj, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, 0xFFFFFF);
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        boolean valid = super.mousePressed(mc, mouseX, mouseY);

        if (valid) {
            clicked = !clicked;
            this.displayString = clicked ? Setting.INFO_VALUE_COLOR + content : Setting.INFO_PREFIX_COLOR + content;
        }

        return valid;
    }
}
