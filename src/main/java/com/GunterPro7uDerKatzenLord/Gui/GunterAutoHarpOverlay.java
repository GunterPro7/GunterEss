package com.GunterPro7uDerKatzenLord.Gui;

import com.GunterPro7uDerKatzenLord.Setting;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

public class GunterAutoHarpOverlay extends AbstractOverlay {

    private GuiButton buttonEnabled;
    private GuiTextField textFieldPing;

    public GunterAutoHarpOverlay(GuiScreen lastScreen) {
        super(lastScreen);
    }

    @Override
    public void initGui() {
        super.initGui();

        GuiLabel label = new GuiLabel(fontRendererObj, 0, width / 2 - 50, 40, 100, 20, 0xFFFFFF).setCentered();
        label.func_175202_a("§lGunter Essentials");
        label.func_175202_a("");
        label.func_175202_a("-> Auto Harp");
        labelList.add(label);

        buttonEnabled = new GuiButton(0, width / 2 - 100, height / 2, (Setting.AUTO_HARP.isEnabled() ? "§a§lEnabled" : "§c§lDisabled"));
        textFieldPing = new GuiTextField(1, fontRendererObj, width / 2 - 100, height / 2 + 24, 100, 20);
        textFieldPing.setMaxStringLength(5);

        textFieldList.add(textFieldPing); // TODO shit missing here
    }
}
