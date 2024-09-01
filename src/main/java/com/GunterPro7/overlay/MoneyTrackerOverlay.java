package com.GunterPro7.overlay;

import com.GunterPro7.Setting;
import com.GunterPro7.gui.CustomIngameUI;
import com.GunterPro7.moneyTracker.MoneyHandler;
import com.GunterPro7.utils.Utils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class MoneyTrackerOverlay extends AbstractOverlay {
    private final Map<GuiButton, Supplier<? extends GuiScreen>> buttonRelations = new HashMap<>();

    private GuiButton enabledButton;
    private GuiButton moveButton;
    private GuiButton resetButton;
    private GuiLabel pauseAfterLabel;
    private GuiTextField pauseAfterTextField;

    public MoneyTrackerOverlay(GuiScreen lastScreen) {
        super(lastScreen);
    }

    @Override
    public void initGui() {
        super.initGui();
        buttonRelations.clear();

        title.func_175202_a("");
        title.func_175202_a("-> Money Tracker");

        enabledButton = new GuiButton(0, width / 2 - 100, pageContentHeight += BUTTON_HEIGHT, "Money Tracker: " + (Setting.MONEY_OVERLAY.isEnabled() ? "§a§lEnabled" : "§c§lDisabled"));
        moveButton = new GuiButton(0, width / 2 - 100, pageContentHeight += BUTTON_HEIGHT, "Move Money Tracker Overlay");

        pauseAfterLabel = new GuiLabel(fontRendererObj, 0, width / 2 - 100, pageContentHeight += BUTTON_HEIGHT, 100, 20, 0xFFFFFF);
        pauseAfterLabel.func_175202_a("Pause after: (sec)");
        pauseAfterTextField = new GuiTextField(0, fontRendererObj, width / 2, pageContentHeight, 100, 20);
        pauseAfterTextField.setText(String.valueOf(Setting.MONEY_PAUSE_AFTER.getValue()));

        pageContentHeight += BUTTON_HEIGHT;

        buttonRelations.put(new GuiButton(1, width / 2 - 100, pageContentHeight += BUTTON_HEIGHT, "Gemstone Tracker"), () -> new GemstoneTrackerOverlay(this));
        buttonRelations.put(new GuiButton(1, width / 2 - 100, pageContentHeight += BUTTON_HEIGHT, "Farming Tracker"), () -> new FarmingTrackerOverlay(this));

        pageContentHeight += BUTTON_HEIGHT;

        resetButton = new GuiButton(0, width / 2 - 100, pageContentHeight += BUTTON_HEIGHT, "Reset Money Tracker");

        buttonList.addAll(buttonRelations.keySet());
        buttonList.add(enabledButton);
        buttonList.add(moveButton);
        buttonList.add(resetButton);

        labelList.add(pauseAfterLabel);
        textFieldList.add(pauseAfterTextField);
    }

    @Override
    protected void textFieldKeyTyped(GuiTextField guiTextField, char typedChar, int keyCode) {
        super.textFieldKeyTyped(guiTextField, typedChar, keyCode);

        int i = Utils.safeToInteger(guiTextField.getText());
        if (i != -1) {
            Setting.MONEY_PAUSE_AFTER.setValue(i);
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button == enabledButton) {
            Setting.MONEY_OVERLAY.switchEnabled();
            button.displayString = "Money Tracker: " + (Setting.MONEY_OVERLAY.isEnabled() ? "§a§lEnabled" : "§c§lDisabled");
        } else if (button == moveButton) {
            mc.displayGuiScreen(new MoveObjectOverlay(new CustomIngameUI(0x501E1E1E, 0x501E1E1E,
                    Utils.convertToColorString("Time: 1h"), Utils.convertToColorString("Money: 1m"),
                    Utils.convertToColorString("Money/h: 1m")), Setting.MONEY_OVERLAY, this));
        } else if (button == resetButton) {
            MoneyHandler.getInstance().reset();
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
