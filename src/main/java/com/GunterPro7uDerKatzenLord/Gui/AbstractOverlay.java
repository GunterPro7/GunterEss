package com.GunterPro7uDerKatzenLord.Gui;

import net.minecraft.client.gui.GuiScreen;

public abstract class AbstractOverlay extends GuiScreen {
    protected final GuiScreen lastScreen;

    public AbstractOverlay(GuiScreen lastScreen) {
        this.lastScreen = lastScreen;
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
