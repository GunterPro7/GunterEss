package com.GunterPro7uDerKatzenLord.Gui;

import com.GunterPro7uDerKatzenLord.Listener.AdvancedChat;
import com.GunterPro7uDerKatzenLord.Utils.TimeUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractOverlay extends GuiScreen {
    private static final int SCROLL_SIZE = 9;

    protected final GuiScreen lastScreen;
    protected int pageContentHeight = 1000;
    private int scrollOffset;

    private final List<GuiTextField> textFieldList = new ArrayList<>();

    public AbstractOverlay(GuiScreen lastScreen) {
        this.lastScreen = lastScreen;
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == 1 && lastScreen != null) {
            Minecraft.getMinecraft().displayGuiScreen(lastScreen);
        } else {
            super.keyTyped(typedChar, keyCode);
        }
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int i = Mouse.getEventDWheel();

        if (i != 0) {
            System.out.println("Mouse scroll: " + i);

            if (i > 0) {
                if (scrollOffset > 0) {
                    scrollOffset = Math.max(scrollOffset - SCROLL_SIZE, 0);
                }
            } else {
                if (scrollOffset < pageContentHeight) {
                    scrollOffset = Math.min(scrollOffset + SCROLL_SIZE, pageContentHeight);
                }
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int j;
        for(j = 0; j < this.buttonList.size(); ++j) {
            GuiButton button = this.buttonList.get(j);
            System.out.println("before check");
            if (button.yPosition - scrollOffset >= 0) {
                System.out.println("IN CHECK; SHOULD WORK BUT SAME ERROR AS BEFORE");
                System.out.println("scrollOffset: " + scrollOffset + ", contentheight: " + pageContentHeight);
                button.yPosition -= scrollOffset;
                button.drawButton(this.mc, mouseX, button.yPosition - scrollOffset);
                button.yPosition += scrollOffset;
            }
        }

        for(j = 0; j < this.labelList.size(); ++j) {
            GuiLabel label = this.labelList.get(j);
            if (label.field_146174_h - scrollOffset >= 0) {
                this.labelList.get(j).drawLabel(this.mc, mouseX, mouseY - scrollOffset); // TODO wenn geht hier auch
            }
        }

        for(j = 0; j < this.textFieldList.size(); ++j) {
            GuiTextField guiTextField = this.textFieldList.get(j);
            if (guiTextField.yPosition - scrollOffset >= 0) {
                guiTextField.yPosition -= scrollOffset;
                guiTextField.drawTextBox();
                guiTextField.yPosition += scrollOffset;
            }
        }

    }


    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
