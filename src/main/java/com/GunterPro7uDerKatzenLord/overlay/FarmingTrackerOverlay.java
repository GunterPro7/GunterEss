package com.GunterPro7uDerKatzenLord.overlay;

import com.GunterPro7uDerKatzenLord.Setting;
import com.GunterPro7uDerKatzenLord.gui.CustomIngameUI;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

public class FarmingTrackerOverlay extends AbstractOverlay {
    private GuiTextField textField0;
    private GuiButton button1;
    private GuiButton button0;

    public FarmingTrackerOverlay(GuiScreen lastScreen) {
        super(lastScreen);
    }

    public FarmingTrackerOverlay() {
        this(new GunterEssOverlay());
    }

    public GuiScreen getLastScreen() {
        return lastScreen;
    }

    @Override
    public void initGui() {
        super.initGui();

        title.func_175202_a("§lGunter Essentials");
        title.func_175202_a("");
        title.func_175202_a("-> Money Tracker");

        button0 = new GuiButton(1, width / 2 - 100, pageContentHeight += BUTTON_HEIGHT, "Money Tracker: " + (Setting.MONEY_OVERLAY.isEnabled() ? "§a§lEnabled" : "§c§lDisabled"));
        button1 = new GuiButton(0, width / 2 - 100, pageContentHeight += BUTTON_HEIGHT, "Edit Position");

        buttonList.add(button0);
        buttonList.add(button1);
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
