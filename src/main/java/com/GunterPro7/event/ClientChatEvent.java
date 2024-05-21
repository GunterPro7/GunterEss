package com.GunterPro7.event;

import net.minecraft.client.gui.GuiTextField;
import net.minecraftforge.fml.common.eventhandler.Event;

public class ClientChatEvent extends Event {
    private final GuiTextField guiTextField;
    private String text;

    public ClientChatEvent(GuiTextField guiTextField) {
        this.guiTextField = guiTextField;
        text = guiTextField.getText();
    }

    public void setText(String text) {
        if (!super.isCanceled()) {
            guiTextField.setText(text);
        }
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public boolean isCancelable() {
        return true;
    }

    @Override
    public boolean isCanceled() {
        return super.isCanceled();
    }

    @Override
    public void setCanceled(boolean cancel) {
        if (cancel) {
            guiTextField.setText("");
        } else {
            guiTextField.setText(text);
        }
        super.setCanceled(cancel);
    }
}
