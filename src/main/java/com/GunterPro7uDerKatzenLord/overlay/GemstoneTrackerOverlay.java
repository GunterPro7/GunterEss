package com.GunterPro7uDerKatzenLord.overlay;

import com.GunterPro7uDerKatzenLord.Setting;
import com.GunterPro7uDerKatzenLord.gui.CustomIngameUI;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

public class GemstoneTrackerOverlay extends AbstractOverlay {
    private GuiTextField textField0;
    private GuiButton activeButton;
    private GuiButton moveObjectButton;

    public GemstoneTrackerOverlay(GuiScreen lastScreen) {
        super(lastScreen);
    }

    public GemstoneTrackerOverlay() {
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
        title.func_175202_a("-> Gemstone Tracker");

        activeButton = new GuiButton(0, width / 2 - 100, pageContentHeight += BUTTON_HEIGHT, Setting.GEMSTONE_DISPLAY.isEnabled() ? "§a§lEnabled" : "§c§lDisabled");
        moveObjectButton = new GuiButton(0, width / 2 - 100, pageContentHeight += BUTTON_HEIGHT, "Move Object");
        buttonList.add(activeButton);
        buttonList.add(moveObjectButton);
    }


    @Override
    protected void actionPerformed(GuiButton button) {
        if (button == activeButton) {
            Setting.GEMSTONE_DISPLAY.switchEnabled();
            button.displayString = Setting.GEMSTONE_DISPLAY.isEnabled() ? "§a§lEnabled" : "§c§lDisabled";
        } else if (button == moveObjectButton) {
            mc.displayGuiScreen(new MoveObjectOverlay(new CustomIngameUI(0x00000000, 0x80000000, "§b1 §1Fine§f, §b1 §aFlawed §e✧ Topaz Gemstone"), Setting.GEMSTONE_DISPLAY, this));
        }
    }
}
