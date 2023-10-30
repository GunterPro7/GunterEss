package com.GunterPro7uDerKatzenLord.Gui;

import com.GunterPro7uDerKatzenLord.Setting;
import com.GunterPro7uDerKatzenLord.Utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import javax.swing.text.Position;

public class MoveObjectOverlay extends GuiScreen {
    public final GuiScreen lastScreen;
    private final CustomIngameUI customIngameUI;
    private int offsetX = 50;
    private int offsetY = 50;
    private int mouseOffsetX;
    private int mouseOffsetY;
    private boolean allowMove;
    private GuiButton button;
    private final Setting.Position position;

    public MoveObjectOverlay(CustomIngameUI customIngameUI, Setting.Position position, GuiScreen lastScreen) {
        this.customIngameUI = customIngameUI;
        this.position = position;
        this.lastScreen = lastScreen;
    }

    @Override
    public void initGui() {
        button = new GuiButton(0, width / 2 - 100, (int) (height / 1.25), "Save");
        buttonList.add(button);
    }


    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        customIngameUI.drawInfoBox(offsetX + mouseOffsetX, offsetY + mouseOffsetY, true);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseX > offsetX && mouseX < offsetX + customIngameUI.boxWidth && mouseY > offsetY && mouseY < offsetY + customIngameUI.boxHeight) {
            allowMove = true;
        }
        if (mouseX > button.xPosition && mouseX < button.xPosition + button.width && mouseY > button.yPosition && mouseY < button.yPosition + button.height) {
            position.setOffsetX(offsetX);
            position.setOffsetY(offsetY);
            Minecraft.getMinecraft().displayGuiScreen(lastScreen);
        }
    }

    @Override
    public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        if (allowMove) {
            offsetX = mouseX;
            offsetY = mouseY;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        allowMove = false;
    }
}
