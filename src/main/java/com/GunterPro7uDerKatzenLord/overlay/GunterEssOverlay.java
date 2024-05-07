package com.GunterPro7uDerKatzenLord.overlay;

import com.GunterPro7uDerKatzenLord.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class GunterEssOverlay extends AbstractOverlay {
    private GuiButton autoFisherButton;
    private GuiButton autoUpdateButton;
    private final Map<GuiButton, Supplier<? extends GuiScreen>> buttonRelations = new HashMap<>();

    public GuiScreen getLastScreen() {
        return lastScreen;
    }


    public GunterEssOverlay(GuiScreen lastScreen) {
        super(lastScreen);
    }

    public GunterEssOverlay() {
        this(Minecraft.getMinecraft().currentScreen);
    }

    @Override
    public void initGui() {
        super.initGui();
        buttonRelations.clear();

        title.func_175202_a("§lGunter Essentials");
        title.func_175202_a("");
        title.func_175202_a("by GunterPro7 f. DerKatzenLord");

        buttonRelations.put(new GuiButton(0, width / 2 - 100, pageContentHeight += pixelsPerButton, "Gemstone Tracker"), () -> new GemstoneTrackerOverlay(this));
        buttonRelations.put(new GuiButton(0, width / 2 - 100, pageContentHeight += pixelsPerButton, "Chat Features"), () -> new ChatOverlay(this));
        buttonRelations.put(new GuiButton(0, width / 2 - 100, pageContentHeight += pixelsPerButton, "Collection Tracker"), () -> null); // Not ready yet
        buttonRelations.put(new GuiButton(0, width / 2 - 100, pageContentHeight += pixelsPerButton, "Money Tracker"), () -> new FarmingTrackerOverlay(this)); // Not ready yet
        buttonRelations.put(new GuiButton(0, width / 2 - 100, pageContentHeight += pixelsPerButton, "Auto Kicker"), () -> new AutoKickOverlay(this));
        buttonRelations.put(new GuiButton(0, width / 2 - 100, pageContentHeight += pixelsPerButton, "Auto Harp"), () -> new AutoHarpOverlay(this));
        buttonRelations.put(new GuiButton(0, width / 2 - 100, pageContentHeight += pixelsPerButton, "Information Overlay"), () -> new InformationOverlay(this));
        autoFisherButton = new GuiButton(0, width / 2 - 100, pageContentHeight += pixelsPerButton, "Auto Fisher: " + (Setting.AUTO_FISHING.isEnabled() ? "§a§lEnabled" : "§c§lDisabled"));
        autoUpdateButton = new GuiButton(0, width / 2 - 100, pageContentHeight += pixelsPerButton, "Auto Updates: " + (Setting.AUTO_UPDATES.isEnabled() ? "§a§lEnabled" : "§c§lDisabled"));

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
