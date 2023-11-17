package com.GunterPro7uDerKatzenLord.Listener;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.GuiChat;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.io.IOException;

public class GunterGuiChat extends GuiChat {
    private final SearchChat searchChat;

    public GunterGuiChat(SearchChat searchChat) {
        this.searchChat = searchChat;
    }


    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawString(fontRendererObj, "Search:", 1, (int) (this.mc.displayHeight / (this.mc.gameSettings.guiScale == 0 ? 4 : this.mc.gameSettings.guiScale) - Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT * 2.75), 0xFFFFFF);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void keyTyped(char eventChar, int keyCode) throws IOException {
        if (keyCode == 28 || keyCode == 156) {
            searchChat.sortChatLines(inputField.getText());
        } else {
            super.keyTyped(eventChar, keyCode);
        }
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int i = Mouse.getEventDWheel();
        if (i != 0) {
            if (i > 1) {
                i = 1;
            }

            if (i < -1) {
                i = -1;
            }

            if (!isShiftKeyDown()) {
                i *= 7;
            }

            searchChat.scroll(i);
        }
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
        searchChat.resetScroll();
    }
}
