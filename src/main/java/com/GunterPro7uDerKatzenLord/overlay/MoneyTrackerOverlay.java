package com.GunterPro7uDerKatzenLord.overlay;

import com.GunterPro7uDerKatzenLord.Setting;
import com.GunterPro7uDerKatzenLord.gui.CustomIngameUI;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class MoneyTrackerOverlay extends AbstractOverlay {
    private final Map<GuiButton, Supplier<? extends GuiScreen>> buttonRelations = new HashMap<>();

    private GuiButton enabledButton;
    private GuiButton moveButton;

    public MoneyTrackerOverlay(GuiScreen lastScreen) {
        super(lastScreen);
    }

    @Override
    public void initGui() {
        super.initGui();
        buttonRelations.clear();

        title.func_175202_a("§lGunter Essentials");
        title.func_175202_a("");
        title.func_175202_a("-> Money Tracker");

        enabledButton = new GuiButton(0, width / 2 - 100, pageContentHeight += BUTTON_HEIGHT, "Money Tracker: " + (Setting.MONEY_OVERLAY.isEnabled() ? "§a§lEnabled" : "§c§lDisabled"));
        moveButton = new GuiButton(0, width / 2 - 100, pageContentHeight += BUTTON_HEIGHT, "Move Money Tracker Overlay");

        pageContentHeight += BUTTON_HEIGHT;

        buttonRelations.put(new GuiButton(1, width / 2 - 100, pageContentHeight += BUTTON_HEIGHT, "Gemstone Tracker"), () -> new GemstoneTrackerOverlay(this));

        buttonList.addAll(buttonRelations.keySet());
        buttonList.add(enabledButton);
        buttonList.add(moveButton);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button == enabledButton) {
            Setting.MONEY_OVERLAY.switchEnabled();
            button.displayString = "Money Tracker: " + (Setting.MONEY_OVERLAY.isEnabled() ? "§a§lEnabled" : "§c§lDisabled");
        } else if (button == moveButton) {
            mc.displayGuiScreen(new MoveObjectOverlay(new CustomIngameUI(0x00000000, 0x00000000,
                    "Time: 0", "Money/h: 0", "Money: 0"), Setting.MONEY_OVERLAY, this));
        }

        if (button.id == 1) {
            buttonRelations.forEach((key, value) -> {
                if (button == key) {
                    mc.displayGuiScreen(value.get());
                }
            });
        }
    }
}
