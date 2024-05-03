package com.GunterPro7uDerKatzenLord.Gui;

import com.GunterPro7uDerKatzenLord.Setting;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

public class GunterGemstoneTrackerOverlay extends AbstractOverlay {
    private GuiTextField textField0;
    private GuiButton activeButton;
    private GuiButton moveObjectButton;

    public GunterGemstoneTrackerOverlay(GuiScreen lastScreen) {
        super(lastScreen);
    }

    public GunterGemstoneTrackerOverlay() {
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
        label.func_175202_a("-> Gemstone Tracker");
        labelList.add(label);

        activeButton = new GuiButton(0, width / 2 - 100, height / 2 + 0, Setting.GEMSTONE_DISPLAY.isEnabled() ? "§a§lEnabled" : "§c§lDisabled");
        moveObjectButton = new GuiButton(0, width / 2 - 100, height / 2 + 24, "Move Object");
        buttonList.add(activeButton);
        buttonList.add(moveObjectButton);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
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
