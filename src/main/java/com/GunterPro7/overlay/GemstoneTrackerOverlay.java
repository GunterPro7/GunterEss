package com.GunterPro7.overlay;

import com.GunterPro7.Setting;
import com.GunterPro7.gui.CustomIngameUI;
import com.GunterPro7.moneyTracker.GemstoneDisplay;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

public class GemstoneTrackerOverlay extends AbstractOverlay {
    private GuiButton activeButton;
    private GuiButton moveObjectButton;
    private GuiButton resetButton;

    public GemstoneTrackerOverlay(GuiScreen lastScreen) {
        super(lastScreen);
    }

    @Override
    public void initGui() {
        super.initGui();

        title.func_175202_a("");
        title.func_175202_a("-> Gemstone Tracker");

        activeButton = new GuiButton(0, width / 2 - 100, pageContentHeight += BUTTON_HEIGHT, Setting.GEMSTONE_DISPLAY.isEnabled() ? "§a§lEnabled" : "§c§lDisabled");
        moveObjectButton = new GuiButton(0, width / 2 - 100, pageContentHeight += BUTTON_HEIGHT, "Move Object");

        pageContentHeight += BUTTON_HEIGHT;
        resetButton = new GuiButton(0, width / 2 - 100, pageContentHeight += BUTTON_HEIGHT, "Reset Gemstone Tracker");

        buttonList.add(activeButton);
        buttonList.add(moveObjectButton);
        buttonList.add(resetButton);
    }


    @Override
    protected void actionPerformed(GuiButton button) {
        if (button == activeButton) {
            Setting.GEMSTONE_DISPLAY.switchEnabled();
            button.displayString = Setting.GEMSTONE_DISPLAY.isEnabled() ? "§a§lEnabled" : "§c§lDisabled";
        } else if (button == moveObjectButton) {
            mc.displayGuiScreen(new MoveObjectOverlay(new CustomIngameUI(0x00000000, 0x80000000, "§b1 §1Fine§f, §b1 §aFlawed §e✧ Topaz Gemstone"), Setting.GEMSTONE_DISPLAY, this));
        } else if (button == resetButton) {
            GemstoneDisplay.resetAll();
        }
    }
}
