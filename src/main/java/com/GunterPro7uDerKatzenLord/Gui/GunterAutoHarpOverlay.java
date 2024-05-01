package com.GunterPro7uDerKatzenLord.Gui;

import com.GunterPro7uDerKatzenLord.Setting;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

public class GunterAutoHarpOverlay extends GuiScreen {
    private final GuiScreen lastScreen;

    private GuiButton buttonEnabled;
    private GuiTextField textFieldPing;

    public GunterAutoHarpOverlay(GuiScreen lastScreen) {
        this.lastScreen = lastScreen;
    }

    @Override
    public void initGui() {
        super.initGui();

        buttonEnabled = new GuiButton(0, width / 2 - 100, height / 2, (Setting.AUTO_HARP.isEnabled() ? "§a§lEnabled" : "§c§lDisabled"));
        textFieldPing = new GuiTextField(1, fontRendererObj, width / 2 - 100, height / 2 + 24, 100, 20);
        textFieldPing.setMaxStringLength(5);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        drawCenteredString(fontRendererObj, "§lGunter Essentials", width / 2, 40, 0xFFFFFF);
        drawCenteredString(fontRendererObj, "-> Auto Harp", width / 2, 60, 0xFFFFFF);

        textFieldPing.drawTextBox();
    }
}
