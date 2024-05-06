package com.GunterPro7uDerKatzenLord.Gui;

import com.GunterPro7uDerKatzenLord.Setting;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GunterChatOverlay extends AbstractOverlay {
    private GuiButton button0;
    private GuiButton button1;
    private GuiButton button2;
    private GuiButton button3;
    private GuiButton button4;

    public GunterChatOverlay(GuiScreen lastScreen) {
        super(lastScreen);
    }

    public GuiScreen getLastScreen() {
        return lastScreen;
    }

    @Override
    public void initGui() {
        super.initGui();

        title.func_175202_a("§lGunter Essentials");
        title.func_175202_a("");
        title.func_175202_a("-> Chat Features");

        button0 = new GuiButton(0, width / 2 - 100, pageContentHeight += pixelsPerButton, "Copy Chat: " + (Setting.COPY_CHAT.isEnabled() ? "§a§lEnabled" : "§c§lDisabled"));
        button1 = new GuiButton(0, width / 2 - 100, pageContentHeight += pixelsPerButton, "Copy with Stack: " + (Setting.COPY_WITH_STACK.isEnabled() ? "§a§lEnabled" : "§c§lDisabled"));
        button2 = new GuiButton(0, width / 2 - 100, pageContentHeight += pixelsPerButton, "Stack Chat Messages: " + (Setting.STACK_CHAT_MESSAGES.isEnabled() ? "§a§lEnabled" : "§c§lDisabled"));
        button3 = new GuiButton(0, width / 2 - 100, pageContentHeight += pixelsPerButton, "Remove Blank Lines: " + (Setting.REMOVE_BLANK_LINES.isEnabled() ? "§a§lEnabled" : "§c§lDisabled"));
        button4 = new GuiButton(0, width / 2 - 100, pageContentHeight += pixelsPerButton, "Dont Stack useless Chat Messages: " + (Setting.DONT_CHECK_USELESS_CHAT_MESSAGES.isEnabled() ? "§a§lEnabled" : "§c§lDisabled"));

        buttonList.add(button0);
        buttonList.add(button1);
        buttonList.add(button2);
        buttonList.add(button3);
        buttonList.add(button4);
    }


    @Override
    protected void actionPerformed(GuiButton button) {
        if (button == button0) {
            Setting.COPY_CHAT.switchEnabled();
            button0.displayString = "Copy Chat: " + (Setting.COPY_CHAT.isEnabled() ? "§a§lEnabled" : "§c§lDisabled");
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
}
