package com.GunterPro7.overlay;

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
    protected static final int SCROLL_SIZE = 9;
    protected static final int BUTTON_HEIGHT = 25;
    protected static final int CHECKBOX_HEIGHT = 16;

    protected final GuiScreen lastScreen;
    protected GuiLabel title;
    protected int pageContentHeight;
    protected int scrollOffset;

    protected final List<GuiTextField> textFieldList = new ArrayList<>();

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

        textFieldList.forEach(textField -> {
            if (textField.isFocused()) {
                textFieldKeyTyped(textField, typedChar, keyCode);
            }
        });
    }

    protected void textFieldKeyTyped(GuiTextField guiTextField, char typedChar, int keyCode) {
        guiTextField.textboxKeyTyped(typedChar, keyCode);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int i = Mouse.getEventDWheel();

        if (i != 0) {
            if (i > 0) {
                if (scrollOffset > 0) {
                    scrollOffset = Math.max(scrollOffset - SCROLL_SIZE, 0);
                }
            } else {
                if (scrollOffset < pageContentHeight - height + 64) {
                    scrollOffset = Math.min(scrollOffset + SCROLL_SIZE, pageContentHeight);
                }
            }
        }
    }

    @Override
    public void initGui() {
        super.initGui();
        buttonList.clear();
        labelList.clear();
        title = new GuiLabel(fontRendererObj, 0, width / 2 - 50, 40, 100, 20, 0xFFFFFF).setCentered();
        labelList.add(title);

        textFieldList.clear();
        pageContentHeight = 85;
    }

    // LABEL:
    //    protected int field_146167_a = 200;    <-- width
    //    protected int field_146161_f = 20;     <-- height
    //    public int field_146162_g;             <-- x
    //    public int field_146174_h;             <-- y
    //    private List<String> field_146173_k;   <-- message
    //
    //                                 id,  x,  y, width, height, color
    //    new GuiLabel(fontRendererObj, 0, 50, 50,    60,     20,     1)

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        if (fontRendererObj == null) return;

        int j;
        for (j = 0; j < this.buttonList.size(); ++j) {
            GuiButton button = this.buttonList.get(j);
            if (button.yPosition - scrollOffset >= -button.height) {
                button.yPosition -= scrollOffset;
                button.drawButton(this.mc, mouseX, mouseY);
                button.yPosition += scrollOffset;
            }
        }

        for (j = 0; j < this.labelList.size(); ++j) {
            GuiLabel label = this.labelList.get(j);
            if (label.field_146174_h - scrollOffset >= -20 /* Label Height */) {
                label.field_146174_h -= scrollOffset;
                label.drawLabel(this.mc, mouseX, mouseY);
                label.field_146174_h += scrollOffset;
            }
        }

        for (j = 0; j < this.textFieldList.size(); ++j) {
            GuiTextField guiTextField = this.textFieldList.get(j);
            if (guiTextField.yPosition - scrollOffset >= guiTextField.height) {
                guiTextField.yPosition -= scrollOffset;
                guiTextField.drawTextBox();
                guiTextField.yPosition += scrollOffset;
            }
        }

    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY + scrollOffset, mouseButton);
        textFieldList.forEach(textField -> textField.mouseClicked(mouseX, mouseY + scrollOffset, 0));
    }


    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
