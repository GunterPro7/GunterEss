package com.GunterPro7.overlay;

import com.GunterPro7.Setting;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

public class AutoHarpOverlay extends AbstractOverlay {

    private GuiButton buttonEnabled;
    private GuiTextField textFieldPing;

    public AutoHarpOverlay(GuiScreen lastScreen) {
        super(lastScreen);
    }

    @Override
    public void initGui() {
        super.initGui();

        title.func_175202_a("");
        title.func_175202_a("-> Auto Harp");

        buttonEnabled = new GuiButton(0, width / 2 - 100, pageContentHeight += BUTTON_HEIGHT, (Setting.AUTO_HARP.isEnabled() ? "§a§lEnabled" : "§c§lDisabled"));
        textFieldPing = new GuiTextField(1, fontRendererObj, width / 2 - 100, pageContentHeight += BUTTON_HEIGHT, 100, 20);
        textFieldPing.setMaxStringLength(5);

        textFieldList.add(textFieldPing); // TODO shit missing here
    }
}
