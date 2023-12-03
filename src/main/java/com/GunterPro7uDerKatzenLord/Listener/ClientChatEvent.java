package com.GunterPro7uDerKatzenLord.Listener;

import net.minecraft.client.gui.GuiTextField;
import net.minecraftforge.fml.common.eventhandler.Event;

public class ClientChatEvent extends Event {
    private final GuiTextField guiTextField;

    public ClientChatEvent(GuiTextField guiTextField) {
        this.guiTextField = guiTextField;
    }

    public void setText(String text) {
        guiTextField.setText(text);
    }

    public String getText() {
        return guiTextField.getText();
    }
}
