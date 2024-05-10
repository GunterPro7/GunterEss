package com.GunterPro7uDerKatzenLord.overlay;

import com.GunterPro7uDerKatzenLord.Setting;
import com.GunterPro7uDerKatzenLord.gui.Align;
import com.GunterPro7uDerKatzenLord.gui.CustomIngameUI;
import com.GunterPro7uDerKatzenLord.listener.AdvancedChat;
import com.GunterPro7uDerKatzenLord.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;

public class MoveObjectOverlay extends AbstractOverlay {
    private final CustomIngameUI customIngameUI;
    private int offsetX;
    private int offsetY;
    private boolean allowMove;
    private GuiButton saveButton;
    private GuiButton alignButton;
    private final Setting.Position position;

    private Align align;

    public MoveObjectOverlay(CustomIngameUI customIngameUI, Setting.Position position, GuiScreen lastScreen) {
        super(lastScreen);
        this.customIngameUI = customIngameUI;
        this.position = position;
        position.setLacy(true);

        this.offsetX = position.getOffsetX();
        this.offsetY = position.getOffsetY();

        this.align = position.getAlign();
        customIngameUI.align(align);
    }

    @Override
    public void initGui() {
        super.initGui();

        saveButton = new GuiButton(0, width / 2 - 100, (int) (height / 1.25), "Save");
        alignButton = new GuiButton(0, width / 2 - 100, (int) (height / 1.25) - BUTTON_HEIGHT, "Align: " + Utils.toTitleCase(align.name()));
        buttonList.add(saveButton);
        buttonList.add(alignButton);
    }


    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        customIngameUI.drawInfoBox(offsetX, offsetY, true);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        mouseX = reCalcMouseX(mouseX);

        if (mouseX > offsetX && mouseX < offsetX + customIngameUI.boxWidth && mouseY > offsetY && mouseY < offsetY + customIngameUI.boxHeight) {
            allowMove = true;
            if (mouseX + customIngameUI.boxWidth + CustomIngameUI.PADDING >= reCalcMouseX(width)) { // TODO split this into 2 methods
                mouseX = reCalcMouseX(width - customIngameUI.boxWidth - CustomIngameUI.PADDING);
            }
            offsetX = mouseX;

            if (mouseY + customIngameUI.boxHeight - CustomIngameUI.PADDING >= height) {
                mouseY = height - customIngameUI.boxHeight + CustomIngameUI.PADDING;
            }
            offsetY = mouseY;
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
            position.setAlign(align);
            position.update();
            position.setLacy(false);
            Minecraft.getMinecraft().displayGuiScreen(lastScreen);
        } else if (button == alignButton) {
            align = Align.nextAlign(align);

            customIngameUI.align(align);

            if (align == Align.MIDDLE || align == Align.RIGHT) {
                offsetX += customIngameUI.boxWidth / 2 + CustomIngameUI.PADDING + (align == Align.RIGHT ? 1 : 0);
            } else {
                offsetX -= customIngameUI.boxWidth + CustomIngameUI.PADDING * 2;
            }
            button.displayString = "Align: " + Utils.toTitleCase(align.name());
        }
    }

    @Override
    public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        mouseX = reCalcMouseX(mouseX);

        if (allowMove) {
            if (mouseX + customIngameUI.boxWidth + CustomIngameUI.PADDING >= reCalcMouseX(width)) {
                mouseX = reCalcMouseX(width - customIngameUI.boxWidth - CustomIngameUI.PADDING);
            }
            offsetX = mouseX;

            if (mouseY + customIngameUI.boxHeight - CustomIngameUI.PADDING >= height) {
                mouseY = height - customIngameUI.boxHeight + CustomIngameUI.PADDING; // TODO rechts ist 1 oder 2 pixel mehr, unten unten glaub ich 1 pixel mehr wo man es schieben kann...
            }
            offsetY = mouseY; // TODO execute this in renderScreen if the Mouse.MOVE_METHODE() is true or something...
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        allowMove = false;
    }

    private int reCalcMouseX(int mouseX) {
        if (align == Align.MIDDLE) {
            return mouseX + customIngameUI.boxWidth / 2 + CustomIngameUI.PADDING;
        } else if (align == Align.RIGHT) {
            return mouseX + customIngameUI.boxWidth + CustomIngameUI.PADDING * 2;
        } else {
            return mouseX;
        }
    }
}
