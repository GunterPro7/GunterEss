package com.GunterPro7uDerKatzenLord.Gui;

import com.GunterPro7uDerKatzenLord.Listener.AdvancedChat;
import com.GunterPro7uDerKatzenLord.Utils.CollectionUtils;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.EnumDyeColor;
import net.minecraftforge.fml.client.config.GuiCheckBox;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class InformationOverlay extends GuiScreen {
    private static final List<String> colors = Arrays.stream(EnumDyeColor.values()).map(EnumDyeColor::getName).collect(Collectors.toList());
    private static final Map<String, List<Gui>> informationsAndButtonList;
    private static final List<Boolean> dropdownButtonsOpened = CollectionUtils.listOf(false, false, false, false, false);
    private static final int pixelsPerButton = 25;

    private final GuiScreen lastScreen;
    private GuiButton prefixColorButton;
    private GuiButton valueColorButton;

    private final List<GuiButton> dropdownButtons = new ArrayList<>();

    static {
        // TODO fill dropdownButtonsOpened from Settings

        informationsAndButtonList = CollectionUtils.mapOf(
                "Ping", CollectionUtils.listOf(new GuiCheckBox(0, 0, 0, "Ping Overlay Enabled:", false),
                        new GuiButton(0, 0, 0, "Move Ping Overlay")),
                "Day, Time", CollectionUtils.listOf(new GuiCheckBox(0, 0, 0, "Day Overlay Enabled:", false),
                        new GuiButton(0, 0, 0, "Move Day Overlay"),
                        new GuiCheckBox(0, 0, 0, "Time Overlay Enabled:", false),
                        new GuiButton(0, 0, 0, "Move Time Overlay")),
                "Position", CollectionUtils.listOf(new GuiCheckBox(0, 0, 0, "Position Overlay Enabled:", false),
                        new GuiButton(0, 0, 0, "Move X Overlay"),
                        new GuiButton(0, 0, 0, "Move Y Overlay"),
                        new GuiButton(0, 0, 0, "Move Z Overlay")),
                "Fps", CollectionUtils.listOf(new GuiCheckBox(0, 0, 0, "Fps Overlay Enabled:", false),
                        new GuiButton(0, 0, 0, "Move Fps Overlay")),
                "Facing", CollectionUtils.listOf(new GuiCheckBox(0, 0, 0, "Facing Overlay Enabled:", false),
                        new GuiButton(0, 0, 0, "Move Facing Overlay")));
    }

    public InformationOverlay(GuiScreen lastScreen) {
        this.lastScreen = lastScreen;
    }

    @Override
    public void initGui() {
        super.initGui();
        reInitGui();
    }

    public void reInitGui() {
        super.initGui();
        buttonList.clear();
        dropdownButtons.clear();

        prefixColorButton = new GuiButton(0, width / 2 - 100, height / 2 - 53, "Prefix Color: §lorange");
        valueColorButton = new GuiButton(0, width / 2 - 100, height / 2 - 29, "Value Color: §lwhite");
        buttonList.add(prefixColorButton);
        buttonList.add(valueColorButton);

        AtomicInteger curHeight = new AtomicInteger();
        AtomicInteger curIndex = new AtomicInteger();

        informationsAndButtonList.forEach((key, value) -> {
            dropdownButtons.add(new GuiButton(0, width / 2 - 100, height / 2 + curHeight.getAndAdd(pixelsPerButton), key));


            if (dropdownButtonsOpened.get(curIndex.getAndAdd(1))) {
                value.forEach(gui -> {
                    if (gui instanceof GuiCheckBox) {
                        AdvancedChat.sendPrivateMessage("in GuiCheckBox: ");
                        GuiCheckBox checkBox = (GuiCheckBox) gui;
                        checkBox.xPosition = width / 2 - 5;
                        curHeight.addAndGet(31);
                        checkBox.yPosition = height / 2 + curHeight.getAndAdd(pixelsPerButton);
                        buttonList.add(checkBox);
                    } else if (gui instanceof GuiButton) {
                        AdvancedChat.sendPrivateMessage("in GuiButton: ");
                        GuiButton button = (GuiButton) gui;
                        button.xPosition = width / 2 - 100;
                        button.yPosition = height / 2 + curHeight.getAndAdd(pixelsPerButton);
                        buttonList.add(button);
                    }

                });

                curHeight.getAndAdd(5);
            }
        });

        buttonList.addAll(dropdownButtons);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();

        drawCenteredString(fontRendererObj, "§lGunter Essentials", width / 2, 40, 0xFFFFFF);
        drawCenteredString(fontRendererObj, "-> Information Overlay", width / 2, 60, 0xFFFFFF);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);

        if (button == prefixColorButton || button == valueColorButton) {
            int idx = colors.indexOf(button.displayString.substring("Prefix Color: §l".length()));

            button.displayString = "Prefix Color: §l" + colors.get(idx == colors.size() - 1 ? 0 : idx + 1);
        }

        int index = 0;
        for (GuiButton curButton : dropdownButtons) {
            if (curButton == button) {
                AdvancedChat.sendPrivateMessage(index + " in here!");
                dropdownButtonsOpened.set(index, !dropdownButtonsOpened.get(index));
                reInitGui();
                break;
            }
            index++;
        }
    }
}
