package com.GunterPro7uDerKatzenLord.Gui;

import com.GunterPro7uDerKatzenLord.Setting;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

public class GunterMoneyOverlay extends AbstractOverlay {
    private GuiTextField textField0;
    private GuiButton button1;
    private GuiButton button0;

    public GunterMoneyOverlay(GuiScreen lastScreen) {
        super(lastScreen);
    }

    public GunterMoneyOverlay() {
        this(new GunterOverlay());
    }

    public GuiScreen getLastScreen() {
        return lastScreen;
    }

    @Override
    public void initGui() {
        super.initGui();

        GuiLabel label = new GuiLabel(fontRendererObj, 0, width / 2 - 50, 40, 100, 20, 0xFFFFFF).setCentered();
        label.func_175202_a("§lGunter Essentials");
        label.func_175202_a("");
        label.func_175202_a("-> Money Tracker");
        labelList.add(label);

        button0 = new GuiButton(1, width / 2 - 100, height / 2 - 24, "Money Tracker: " + (Setting.MONEY_OVERLAY.isEnabled() ? "§a§lEnabled" : "§c§lDisabled"));
        button1 = new GuiButton(0, width / 2 - 100, height / 2 - 0, "Edit Position");

        buttonList.add(button0);
        buttonList.add(button1);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawCenteredString(fontRendererObj, "§lGunter Essentials", width / 2, 40, 0xFFFFFF);
        drawCenteredString(fontRendererObj, "-> Money Tracker", width / 2, 60, 0xFFFFFF);
        //textField1.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }


    @Override
    protected void actionPerformed(GuiButton button) {
        if (button == button1) {
            CustomIngameUI customIngameUI = new CustomIngameUI(0x80000000, 0xFF000000, "§e§lMoney Tracker:", "§22.3m/h", "§2Crops: 400");
            mc.displayGuiScreen(new MoveObjectOverlay(customIngameUI, Setting.MONEY_OVERLAY, this));
        } else if (button == button0) {
            Setting.MONEY_OVERLAY.switchEnabled();
            button0 = new GuiButton(1, width / 2 - 100, height / 2 - 24, "Money Tracker: " + (Setting.MONEY_OVERLAY.isEnabled() ? "§a§lEnabled" : "§c§lDisabled"));
            buttonList.set(1, button1);
        }
    }
}
