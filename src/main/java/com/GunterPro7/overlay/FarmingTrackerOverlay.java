package com.GunterPro7.overlay;

import com.GunterPro7.Setting;
import com.GunterPro7.gui.CustomIngameUI;
import com.GunterPro7.moneyTracker.FarmingTracker;
import com.GunterPro7.utils.Utils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class FarmingTrackerOverlay extends AbstractOverlay {
    private GuiButton activeButton;
    private GuiButton moveObjectButton;
    private GuiButton resetButton;

    public FarmingTrackerOverlay(GuiScreen lastScreen) {
        super(lastScreen);
    }

    @Override
    public void initGui() {
        super.initGui();

        title.func_175202_a("§lGunter Essentials");
        title.func_175202_a("");
        title.func_175202_a("-> Farming Tracker");

        activeButton = new GuiButton(0, width / 2 - 100, pageContentHeight += BUTTON_HEIGHT, Setting.FARMING_OVERLAY.isEnabled() ? "§a§lEnabled" : "§c§lDisabled");
        moveObjectButton = new GuiButton(0, width / 2 - 100, pageContentHeight += BUTTON_HEIGHT, "Move Object");

        pageContentHeight += BUTTON_HEIGHT;
        resetButton = new GuiButton(0, width / 2 - 100, pageContentHeight += BUTTON_HEIGHT, "Reset Money Tracker");

        buttonList.add(activeButton);
        buttonList.add(moveObjectButton);
        buttonList.add(resetButton);
    }


    @Override
    protected void actionPerformed(GuiButton button) {
        if (button == activeButton) {
            Setting.FARMING_OVERLAY.switchEnabled();
            button.displayString = Setting.FARMING_OVERLAY.isEnabled() ? "§a§lEnabled" : "§c§lDisabled";
        } else if (button == moveObjectButton) {
            mc.displayGuiScreen(new MoveObjectOverlay(new CustomIngameUI(0x00000000, 0x80000000,
                    Utils.convertToColorString("Wheat: 8,40k"), Utils.convertToColorString("Carrot: 2k")), // TODO stopped here everything untested
                    Setting.FARMING_OVERLAY, this));
        } else if (button == resetButton) {
            FarmingTracker.resetAll();
        }
    }
}

