package com.GunterPro7uDerKatzenLord.Gui;

import com.GunterPro7uDerKatzenLord.Setting;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

import java.io.IOException;

public class GunterCollectionOverlay extends GuiScreen {
    private final GuiScreen lastScreen;
    private GuiTextField textField0;
    private GuiButton button0;
    private GuiButton button1;

    public GunterCollectionOverlay(GuiScreen lastScreen) {
        this.lastScreen = lastScreen;
    }

    public GunterCollectionOverlay() {
        this(new GunterOverlay());
    }

    public GuiScreen getLastScreen() {
        return lastScreen;
    }

    @Override
    public void initGui() {
        buttonList.clear();
        button0 = new GuiButton(0, width / 2 - 100, height / 2 - 0, "Edit Position");
        button1 = new GuiButton(1, width / 2 - 100, height / 2 + 24, "Collection Tracker: " + (Setting.COLLECTION_OVERLAY.isEnabled() ? "§a§lEnabled" : "§c§lDisabled"));

        buttonList.add(button0);
        buttonList.add(button1);

        textField0 = new GuiTextField(0, fontRendererObj, width / 2 - 100, height / 2 - 24, 200, 20);
        textField0.setMaxStringLength(64);
        textField0.setFocused(true);
//
        //textField1 = new GuiTextField(1, fontRendererObj, width / 2 - 160, height / 2 + 24, 100, 20);
        //textField1.setMaxStringLength(100);


    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        drawCenteredString(fontRendererObj, "§lGunter Essentials", width / 2, 40, 0xFFFFFF);
        drawCenteredString(fontRendererObj, "-> Collection Tracker", width / 2, 60, 0xFFFFFF);
        drawCenteredString(fontRendererObj, "Collection:", width / 2 - 70, height / 2 - 42, 0xFFFFFF);
        textField0.drawTextBox();
        //textField1.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        textField0.textboxKeyTyped(typedChar, keyCode);

        super.keyTyped(typedChar, keyCode);
    }


    @Override
    protected void actionPerformed(GuiButton button) {
        if (button == button0) {
            CustomIngameUI customIngameUI = new CustomIngameUI(0x80000000, 0xFF000000, "§e§lCollection Tracker:", "§2Wheat", "LVL 10 -> 11", "49.428 / 75.000");
            mc.displayGuiScreen(new MoveObjectOverlay(customIngameUI, Setting.COLLECTION_OVERLAY, this));
        } else if (button == button1) {
            button1 = new GuiButton(1, width / 2 - 100, height / 2 + 24, "Collection Tracker: " + (Setting.COLLECTION_OVERLAY.isEnabled() ? "§a§lEnabled" : "§c§lDisabled"));
            buttonList.set(1, button1);
            Setting.COLLECTION_OVERLAY.switchEnabled();
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
