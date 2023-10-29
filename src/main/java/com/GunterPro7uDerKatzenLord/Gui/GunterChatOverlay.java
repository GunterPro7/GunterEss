package com.GunterPro7uDerKatzenLord.Gui;

import com.GunterPro7uDerKatzenLord.Setting;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;

public class GunterChatOverlay extends GuiScreen {
    private final GuiScreen lastScreen;
    private GuiButton button0;
    private GuiButton button1;
    private GuiButton button2;
    private GuiButton button3;
    private GuiButton button4;

    public GunterChatOverlay(GuiScreen lastScreen) {
        this.lastScreen = lastScreen;
    }

    public GunterChatOverlay() {
        this(new GunterOverlay());
    }

    public GuiScreen getLastScreen() {
        return lastScreen;
    }

    @Override
    public void initGui() {
        buttonList.clear();
        button0 = new GuiButton(0, width / 2 - 100, height / 2 - 0, "Copy Chat: " + (Setting.COPY_CHAT_ENABLED.isEnabled() ? "§a§lEnabled" : "§c§lDisabled"));
        button1 = new GuiButton(0, width / 2 - 100, height / 2 + 24, "Copy with Stack: " + (Setting.COPY_WITH_STACK.isEnabled() ? "§a§lEnabled" : "§c§lDisabled"));
        button2 = new GuiButton(0, width / 2 - 100, height / 2 + 48, "Stack Chat Messages: " + (Setting.STACK_CHAT_MESSAGES.isEnabled() ? "§a§lEnabled" : "§c§lDisabled"));
        button3 = new GuiButton(0, width / 2 - 100, height / 2 + 72, "Remove Blank Lines: " + (Setting.REMOVE_BLANK_LINES.isEnabled() ? "§a§lEnabled" : "§c§lDisabled"));
        button4 = new GuiButton(0, width / 2 - 100, height / 2 + 96, "Dont Stack useless Chat Messages: " + (Setting.DONT_CHECK_USELESS_CHAT_MESSAGES.isEnabled() ? "§a§lEnabled" : "§c§lDisabled"));

        buttonList.add(button0);
        buttonList.add(button1);
        buttonList.add(button2);
        buttonList.add(button3);
        buttonList.add(button4);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        drawCenteredString(fontRendererObj, "§lGunter Essentials", width / 2, 40, 0xFFFFFF);
        drawCenteredString(fontRendererObj, "-> Chat Features", width / 2, 60, 0xFFFFFF);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }


    @Override
    protected void actionPerformed(GuiButton button) {
        if (button == button0) {
            Setting.COPY_CHAT_ENABLED.switchEnabled();
            button0.displayString = "Copy Chat: " + (Setting.COPY_CHAT_ENABLED.isEnabled() ? "§a§lEnabled" : "§c§lDisabled");
        } else if (button == button1) {
            Setting.COPY_WITH_STACK.switchEnabled();
            button1.displayString = "Copy with Stack: " + (Setting.COPY_WITH_STACK.isEnabled() ? "§a§lEnabled" : "§c§lDisabled");
        } else if (button == button2) {
            Setting.STACK_CHAT_MESSAGES.switchEnabled();
            button2.displayString = "Stack Chat Messages: " + (Setting.STACK_CHAT_MESSAGES.isEnabled() ? "§a§lEnabled" : "§c§lDisabled");
        } else if (button == button3) {
            Setting.REMOVE_BLANK_LINES.switchEnabled();
            button3.displayString = "Remove Blank Lines: " + (Setting.REMOVE_BLANK_LINES.isEnabled() ? "§a§lEnabled" : "§c§lDisabled");
        } else if (button == button4) {
            Setting.DONT_CHECK_USELESS_CHAT_MESSAGES.switchEnabled();
            button4.displayString = "Dont check useless chat messages: " + (Setting.DONT_CHECK_USELESS_CHAT_MESSAGES.isEnabled() ? "§a§lEnabled" : "§c§lDisabled");
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
