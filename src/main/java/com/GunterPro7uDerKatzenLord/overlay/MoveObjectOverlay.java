package com.GunterPro7uDerKatzenLord.overlay;

import com.GunterPro7uDerKatzenLord.Setting;
import com.GunterPro7uDerKatzenLord.gui.CustomIngameUI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;

public class MoveObjectOverlay extends AbstractOverlay {
    private final CustomIngameUI customIngameUI;
    private int offsetX = 50;
    private int offsetY = 50;
    private boolean allowMove;
    private GuiButton saveButton;
    private final Setting.Position position;

    public MoveObjectOverlay(CustomIngameUI customIngameUI, Setting.Position position, GuiScreen lastScreen) {
        super(lastScreen);
        this.customIngameUI = customIngameUI;
        this.position = position;
        position.setLacy(true);

        this.offsetX = position.getOffsetX();
        this.offsetY = position.getOffsetY();
    }

    @Override
    public void initGui() {
        super.initGui();

        saveButton = new GuiButton(0, width / 2 - 100, (int) (height / 1.25), "Save");
        buttonList.add(saveButton);
    }


    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        customIngameUI.drawInfoBox(offsetX, offsetY, true);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseX > offsetX && mouseX < offsetX + customIngameUI.boxWidth && mouseY > offsetY && mouseY < offsetY + customIngameUI.boxHeight) {
            allowMove = true;
        }
        if (mouseX > saveButton.xPosition && mouseX < saveButton.xPosition + saveButton.width && mouseY > saveButton.yPosition && mouseY < saveButton.yPosition + saveButton.height) {
            position.setOffsetX(offsetX);
            position.setOffsetY(offsetY);
            position.update();
            position.setLacy(false);
            Minecraft.getMinecraft().displayGuiScreen(lastScreen);
        }
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();

        position.update();
        position.setLacy(false);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);

        if (button == saveButton) {
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
