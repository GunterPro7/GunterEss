package com.GunterPro7uDerKatzenLord.Gui;

import akka.serialization.Serialization;
import com.GunterPro7uDerKatzenLord.Main;
import com.GunterPro7uDerKatzenLord.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class GunterOverlay extends AbstractOverlay {
    private GuiButton autoFisherButton;
    private GuiButton autoUpdateButton;
    private final Map<GuiButton, Supplier<? extends GuiScreen>> buttonRelations = new HashMap<>();

    public GuiScreen getLastScreen() {
        return lastScreen;
    }


    public GunterOverlay(GuiScreen lastScreen) {
        super(lastScreen);
    }

    public GunterOverlay() {
        this(Minecraft.getMinecraft().currentScreen);
    }

    @Override
    public void initGui() {
        super.initGui();
        buttonRelations.clear();

        GuiLabel label = new GuiLabel(fontRendererObj, 0, width / 2 - 50, 40, 100, 20, 0xFFFFFF).setCentered();
        label.func_175202_a("§lGunter Essentials");
        label.func_175202_a("");
        label.func_175202_a("by GunterPro7 f. DerKatzenLord");
        labelList.add(label);

        buttonRelations.put(new GuiButton(0, width / 2 - 100, height / 2 - 24, "Gemstone Tracker"), () -> new GunterGemstoneTrackerOverlay(this));
        buttonRelations.put(new GuiButton(0, width / 2 - 100, height / 2 + 0, "Chat Features"), () -> new GunterChatOverlay(this));
        buttonRelations.put(new GuiButton(0, width / 2 - 100, height / 2 + 24, "Collection Tracker"), () -> null); // Not ready yet
        buttonRelations.put(new GuiButton(0, width / 2 - 100, height / 2 + 48, "Money Tracker"), () -> null); // Not ready yet
        buttonRelations.put(new GuiButton(0, width / 2 - 100, height / 2 + 72, "Auto Kicker"), () -> new GunterAutoKickOverlay(this));
        buttonRelations.put(new GuiButton(0, width / 2 - 100, height / 2 + 96, "Auto Harp"), () -> new GunterAutoHarpOverlay(this));
        buttonRelations.put(new GuiButton(0, width / 2 - 100, height / 2 + 120, "Information Overlay"), () -> new InformationOverlay(this));
        autoFisherButton = new GuiButton(0, width / 2 - 100, height / 2 + 144, "Auto Fisher: " + (Setting.AUTO_FISHING.isEnabled() ? "§a§lEnabled" : "§c§lDisabled"));
        autoUpdateButton = new GuiButton(0, width / 2 - 100, height / 2 + 168, "Auto Updates: " + (Setting.AUTO_UPDATES.isEnabled() ? "§a§lEnabled" : "§c§lDisabled"));

        buttonList.add(autoFisherButton);
        buttonList.add(autoUpdateButton);

        buttonRelations.forEach((key, value) -> buttonList.add(key));

        //textField0 = new GuiTextField(0, fontRendererObj, width / 2 - 100, height / 2 + 24, 100, 20);
        //textField0.setMaxStringLength(100);
//
        //textField1 = new GuiTextField(1, fontRendererObj, width / 2 - 160, height / 2 + 24, 100, 20);
        //textField1.setMaxStringLength(100);


    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        //if (textField0.isFocused()) {
        //    textField0.textboxKeyTyped(typedChar, keyCode);
        //    offsetX = Integer.parseInt(textField0.getText());
        //} else if (textField1.isFocused()) {
        //    textField1.textboxKeyTyped(typedChar, keyCode);
        //    offsetY = Integer.parseInt(textField1.getText());
        //}

        super.keyTyped(typedChar, keyCode);
    }


    @Override
    protected void actionPerformed(GuiButton button) {
        buttonRelations.forEach((key, value) -> {
            if (button == key) {
                mc.displayGuiScreen(value.get());
            }
        });

        if (button == autoFisherButton) {
            Setting.AUTO_FISHING.switchEnabled();
            autoFisherButton.displayString = "Auto Fisher: " + (Setting.AUTO_FISHING.isEnabled() ? "§a§lEnabled" : "§c§lDisabled");
        } else if (button == autoUpdateButton) {
            Setting.AUTO_UPDATES.switchEnabled();
            autoUpdateButton.displayString = "Auto Updates: " + (Setting.AUTO_UPDATES.isEnabled() ? "§a§lEnabled" : "§c§lDisabled");
        }
    }
}
