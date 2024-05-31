package com.GunterPro7.overlay;

import com.GunterPro7.Main;
import com.GunterPro7.Setting;
import com.GunterPro7.gui.Align;
import com.GunterPro7.gui.CustomIngameUI;
import com.GunterPro7.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;

import static com.GunterPro7.Main.mc;

public class MoveObjectOverlay extends AbstractOverlay {
    private final CustomIngameUI customIngameUI;
    private int offsetX;
    private int offsetY;
    private boolean allowMove;
    private GuiButton saveButton;
    private GuiButton alignButton;
    private GuiButton resetButton;
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
        resetButton = new GuiButton(0, width / 2 - 100, (int) (height / 1.25) - BUTTON_HEIGHT * 2, "Reset");

        buttonList.add(saveButton);
        buttonList.add(alignButton);
        buttonList.add(resetButton);
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
            mc.displayGuiScreen(lastScreen);
        } else if (button == alignButton) {
            align = Align.nextAlign(align);

            customIngameUI.align(align);

            if (align == Align.MIDDLE || align == Align.RIGHT) {
                offsetX += customIngameUI.boxWidth / 2 + CustomIngameUI.PADDING + (align == Align.RIGHT ? 1 : 0);
            } else {
                offsetX -= customIngameUI.boxWidth + CustomIngameUI.PADDING * 2;
            }
            button.displayString = "Align: " + Utils.toTitleCase(align.name());
        } else if (button == resetButton) {
            align = Align.LEFT;
            offsetX = 50;
            offsetY = 50;
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
