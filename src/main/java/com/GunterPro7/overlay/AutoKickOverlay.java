package com.GunterPro7.overlay;

import com.GunterPro7.Main;
import com.GunterPro7.listener.AdvancedChat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AutoKickOverlay extends AbstractOverlay {
    private GuiTextField textField0;
    private final List<GuiButton> localButtonList = new ArrayList<>();
    public final static boolean[] values = new boolean[]{false, false, false, false, false};
    private static GuiTextField textField1 = new GuiTextField(0, Main.mc.fontRendererObj, 0, 0, 200, 20);

    public AutoKickOverlay(GuiScreen lastScreen) {
        super(lastScreen);
    }

    public GuiScreen getLastScreen() {
        return lastScreen;
    }

    @Override
    public void initGui() {
        super.initGui();

        title.func_175202_a("");
        title.func_175202_a("-> Dungeon Utils");

        GuiLabel labelIgnoredPlayers = new GuiLabel(fontRendererObj, 0, width / 2 - 60, height / 2 + 120, 100, 20, 0xFFFFFF);
        labelIgnoredPlayers.func_175202_a("Ignored Players (with ';' between):");
        labelList.add(labelIgnoredPlayers);

        GuiLabel labelAutoJoin = new GuiLabel(fontRendererObj, 0, width / 2 - 60, height / 2 + 180, 100, 20, 0xFFFFFF);
        labelAutoJoin.func_175202_a("Auto Join:");
        labelList.add(labelAutoJoin);

        addButton(new GuiButton(0, width / 2 - 100, pageContentHeight += BUTTON_HEIGHT, "Mage: " + (values[0] ? "§a§lEnabled" : "§c§lDisabled")));
        addButton(new GuiButton(1, width / 2 - 100, pageContentHeight += BUTTON_HEIGHT, "Berserk: " + (values[1] ? "§a§lEnabled" : "§c§lDisabled")));
        addButton(new GuiButton(2, width / 2 - 100, pageContentHeight += BUTTON_HEIGHT, "Healer: " + (values[2] ? "§a§lEnabled" : "§c§lDisabled")));
        addButton(new GuiButton(3, width / 2 - 100, pageContentHeight += BUTTON_HEIGHT, "Tank: " + (values[3] ? "§a§lEnabled" : "§c§lDisabled")));
        addButton(new GuiButton(4, width / 2 - 100, pageContentHeight += BUTTON_HEIGHT, "Archer: " + (values[4] ? "§a§lEnabled" : "§c§lDisabled")));
        pageContentHeight += 100;
        addButton(new GuiButton(5, width / 2 - 100, pageContentHeight += BUTTON_HEIGHT, "Enabled / Disabled IMPLEMENT"));
        addButton(new GuiButton(5, width / 2 - 100, pageContentHeight += BUTTON_HEIGHT, "MIN LVL INPUT"));
        addButton(new GuiButton(5, width / 2 - 0, pageContentHeight, "MAX LVL INPUT"));
        addButton(new GuiButton(5, width / 2 - 100, pageContentHeight += BUTTON_HEIGHT, "MAX LVL INPUT"));
        textField1.xPosition = width / 2 - 100;
        textField1.yPosition = height / 2 + 140;
        textField1.setFocused(true);
        textField1.setMaxStringLength(65536);

        textFieldList.add(textField1);
    }

    private void addButton(GuiButton button) {
        localButtonList.add(button);
        buttonList.add(button);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        textField1.textboxKeyTyped(typedChar, keyCode);

        super.keyTyped(typedChar, keyCode);
    }


    @Override
    protected void actionPerformed(GuiButton button) {
        for (GuiButton localButton : localButtonList) {
            if (localButton == button) {
                String displayString = localButton.displayString;
                boolean b = displayString.endsWith("Disabled");
                localButton.displayString = AdvancedChat.clearChatMessage(displayString.split("Disabled|Enabled")[0]) + (b ? "§a§lEnabled" : "§c§lDisabled");
                values[localButton.id] = b;
            }
        }
    }

    public static List<String> getIgnoredPlayers() {
        return new ArrayList<>(java.util.Arrays.asList(textField1.getText().split(";")));
    }

    public static void addIgnoredPlayer(String player) {
        textField1.setText(textField1.getText() + ";" + player);
    }
}
