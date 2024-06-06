package com.GunterPro7.gui;

import com.GunterPro7.Main;
import com.GunterPro7.utils.DrawUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

public class GuiTransparentImageButton extends GuiTransparentButton {
    private final ResourceLocation buttonImage;

    public GuiTransparentImageButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText, ResourceLocation buttonImage) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
        this.buttonImage = buttonImage;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        super.drawButton(mc, mouseX, mouseY);
        if (this.visible) { // TODO check if it works
            DrawUtils.drawModalRectWithCustomSizedTexture(buttonImage, xPosition + 1, yPosition + 1, 0, 0, 7, 7, 7, 7, true);
        }
    }
}
