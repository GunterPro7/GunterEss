package com.GunterPro7uDerKatzenLord.Gui;

import com.GunterPro7uDerKatzenLord.Setting;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

public class GunterGemstoneTrackerOverlay extends GuiScreen {
    private final GuiScreen lastScreen;
    private GuiTextField textField0;
    private GuiButton activeButton;
    private GuiButton moveObjectButton;

    public GunterGemstoneTrackerOverlay(GuiScreen lastScreen) {
        this.lastScreen = lastScreen;
    }

    public GunterGemstoneTrackerOverlay() {
        this(new GunterOverlay());
    }

    public GuiScreen getLastScreen() {
        return lastScreen;
    }

    @Override
    public void initGui() {
        buttonList.clear();
        activeButton = new GuiButton(0, width / 2 - 100, height / 2 + 0, Setting.GEMSTONE_DISPLAY.isEnabled() ? "§a§lEnabled" : "§c§lDisabled");
        moveObjectButton = new GuiButton(0, width / 2 - 100, height / 2 + 24, "Move Object");
        buttonList.add(activeButton);
        buttonList.add(moveObjectButton);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        drawCenteredString(fontRendererObj, "§lGunter Essentials", width / 2, 40, 0xFFFFFF);
        drawCenteredString(fontRendererObj, "-> Gemstone Tracker", width / 2, 60, 0xFFFFFF);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }


    @Override
    protected void actionPerformed(GuiButton button) {
        if (button == activeButton) {
            Setting.GEMSTONE_DISPLAY.switchEnabled();
            button.displayString = Setting.GEMSTONE_DISPLAY.isEnabled() ? "§a§lEnabled" : "§c§lDisabled";
        } else if (button == moveObjectButton) {
            mc.displayGuiScreen(new MoveObjectOverlay(new CustomIngameUI(0x00000000, 0x80000000, "§b1 §1Fine§f, §b1 §aFlawed §e✧ Topaz Gemstone"), Setting.GEMSTONE_DISPLAY, this));
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
