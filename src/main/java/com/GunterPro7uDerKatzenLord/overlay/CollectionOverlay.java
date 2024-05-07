package com.GunterPro7uDerKatzenLord.overlay;

import com.GunterPro7uDerKatzenLord.Setting;
import com.GunterPro7uDerKatzenLord.gui.CustomIngameUI;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

import java.io.IOException;

public class CollectionOverlay extends AbstractOverlay {
    private GuiTextField textField0;
    private GuiButton button0;
    private GuiButton button1;

    public CollectionOverlay(GuiScreen lastScreen) {
        super(lastScreen);
    }

    public CollectionOverlay() {
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
        title.func_175202_a("-> Collection Tracker");

        GuiLabel label2 = new GuiLabel(fontRendererObj, 0, width / 2 - 70, height / 2 - 42, 50, 20, 0xFFFFFF);
        label2.func_175202_a("Collection:");
        labelList.add(label2);

        button0 = new GuiButton(0, width / 2 - 100, height / 2 - 0, "Edit Position");
        button1 = new GuiButton(1, width / 2 - 100, height / 2 + 24, "Collection Tracker: " + (Setting.COLLECTION_OVERLAY.isEnabled() ? "§a§lEnabled" : "§c§lDisabled"));

        buttonList.add(button0);
        buttonList.add(button1);

        textField0 = new GuiTextField(0, fontRendererObj, width / 2 - 100, height / 2 - 24, 200, 20);
        textField0.setMaxStringLength(64);
        textField0.setFocused(true);

        textFieldList.add(textField0);

        //textField1 = new GuiTextField(1, fontRendererObj, width / 2 - 160, height / 2 + 24, 100, 20);
        //textField1.setMaxStringLength(100);
        //textFieldList.add(textField1);


    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        textField0.textboxKeyTyped(typedChar, keyCode);

        super.keyTyped(typedChar, keyCode);
    }


    @Override
    protected void actionPerformed(GuiButton button) {
        if (button == button0) {
            CustomIngameUI customIngameUI = new CustomIngameUI(0x80000000, 0xFF000000, "§e§lCollection Tracker:", "§2Wheat", "LVL 10 -> 11", "49.428 / 75.000");
            mc.displayGuiScreen(new MoveObjectOverlay(customIngameUI, Setting.COLLECTION_OVERLAY, this));
        } else if (button == button1) {
            button1 = new GuiButton(1, width / 2 - 100, height / 2 + 24, "Collection Tracker: " + (Setting.COLLECTION_OVERLAY.isEnabled() ? "§a§lEnabled" : "§c§lDisabled"));
            buttonList.set(1, button1);
            Setting.COLLECTION_OVERLAY.switchEnabled();
        }
    }
}
