package com.GunterPro7uDerKatzenLord.Gui;

import com.GunterPro7uDerKatzenLord.Main;
import com.GunterPro7uDerKatzenLord.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlider;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class GunterOverlay extends GuiScreen {
    private GuiButton button0;
    private GuiButton button2;
    private GuiButton button3;
    private GuiButton button4;
    private GuiButton button5;
    private GuiButton button6;
    private GuiButton button7;
    private GuiTextField textField0;
    private GuiTextField textField1;
    private GuiSlider guiSlider;
    private final GuiScreen lastScreen;
    public static int offsetX;
    public static int offsetY;

    public GuiScreen getLastScreen() {
        return lastScreen;
    }


    public GunterOverlay(GuiScreen lastScreen) {
        this.lastScreen = lastScreen;
    }

    public GunterOverlay() {
        this(Minecraft.getMinecraft().currentScreen);
    }

    @Override
    public void initGui() {
        buttonList.clear();
        button6 = new GuiButton(0, width / 2 - 100, height / 2 - 24, "Gemstone Tracker");
        button0 = new GuiButton(0, width / 2 - 100, height / 2 + 0, "Chat Features");
        button2 = new GuiButton(0, width / 2 - 100, height / 2 + 24, "Collection Tracker");
        button3 = new GuiButton(0, width / 2 - 100, height / 2 + 48, "Money Tracker");
        button4 = new GuiButton(0, width / 2 - 100, height / 2 + 72, "Auto Kicker");
        button5 = new GuiButton(0, width / 2 - 100, height / 2 + 96, "Auto Fisher: " + (Setting.AUTO_FISHING.isEnabled() ? "§a§lEnabled" : "§c§lDisabled"));
        button7 = new GuiButton(0, width / 2 - 100, height / 2 + 120, "Auto Updates: " + (Setting.AUTO_UPDATES.isEnabled() ? "§a§lEnabled" : "§c§lDisabled"));

        buttonList.add(button0);
        buttonList.add(button2);
        buttonList.add(button3);
        buttonList.add(button4);
        buttonList.add(button5);
        buttonList.add(button6);
        buttonList.add(button7);

        //textField0 = new GuiTextField(0, fontRendererObj, width / 2 - 100, height / 2 + 24, 100, 20);
        //textField0.setMaxStringLength(100);
//
        //textField1 = new GuiTextField(1, fontRendererObj, width / 2 - 160, height / 2 + 24, 100, 20);
        //textField1.setMaxStringLength(100);


    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        drawCenteredString(fontRendererObj, "§lGunter Essentials", width / 2, 40, 0xFFFFFF);
        //textField0.drawTextBox();
        //textField1.drawTextBox();
        drawCenteredString(fontRendererObj, "GunterPro7 f. DerKatzenLord", width / 2, 60, 0xFFFFFF);
        super.drawScreen(mouseX, mouseY, partialTicks);
        ResourceLocation resourceLocation = new ResourceLocation(Main.MOD_ID, "textures/items/wheat.png");
        mc.getTextureManager().bindTexture(resourceLocation); // Set the texture (item's texture).
        drawTexturedModalRect(2, 2, 0, 0, 16, 16);

    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        //if (textField0.isFocused()) {
        //    textField0.textboxKeyTyped(typedChar, keyCode);
        //    offsetX = Integer.parseInt(textField0.getText());
        //} else if (textField1.isFocused()) {
        //    textField1.textboxKeyTyped(typedChar, keyCode);
        //    offsetY = Integer.parseInt(textField1.getText());
        //}

        super.keyTyped(typedChar, keyCode);
    }


    @Override
    protected void actionPerformed(GuiButton button) {
        if (button == button0) {
            mc.displayGuiScreen(new GunterChatOverlay(this));
        } else if (button == button2) {
            //mc.displayGuiScreen(new GunterCollectionOverlay(this));
        } else if (button == button3) {
            //mc.displayGuiScreen(new GunterMoneyOverlay(this));
        } else if (button == button4) {
            mc.displayGuiScreen(new GunterAutoKickOverlay(this));
        } else if (button == button5) {
            Setting.AUTO_FISHING.switchEnabled();
            button5.displayString = "Auto Fisher: " + (Setting.AUTO_FISHING.isEnabled() ? "§a§lEnabled" : "§c§lDisabled");
        } else if (button == button6) {
            mc.displayGuiScreen(new GunterGemstoneTrackerOverlay(this));
        } else if (button == button7) {
            Setting.AUTO_UPDATES.switchEnabled();
            button7.displayString = "Auto Updates: " + (Setting.AUTO_UPDATES.isEnabled() ? "§a§lEnabled" : "§c§lDisabled");
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
