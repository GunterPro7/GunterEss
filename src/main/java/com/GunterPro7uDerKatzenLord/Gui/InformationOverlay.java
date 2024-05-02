package com.GunterPro7uDerKatzenLord.Gui;

import com.GunterPro7uDerKatzenLord.Setting;
import com.GunterPro7uDerKatzenLord.Utils.CollectionUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.client.config.GuiCheckBox;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class InformationOverlay extends AbstractOverlay {
    private static final Map<String, List<Gui>> informationsAndButtonList;
    private static final List<String> colors = Arrays.stream(EnumChatFormatting.values()).filter(EnumChatFormatting::isColor).map(e -> e + e.getFriendlyName()).collect(Collectors.toList());
    private static final Map<String, EnumChatFormatting> colorsByPrefix = CollectionUtils.mapOf(colors, Arrays.stream(EnumChatFormatting.values()).filter(EnumChatFormatting::isColor).collect(Collectors.toList()));

    private static final List<Boolean> dropdownButtonsOpened = CollectionUtils.listOf(() -> false, 5);
    private static final int pixelsPerButton = 25;
    private static final int pixelsPerCheckBox = 16;

    private static GuiButton prefixColorButton;
    private static GuiButton suffixColorButton;
    private static GuiButton valueColorButton;

    private static final List<GuiButton> dropdownButtons = new ArrayList<>();

    static {
        Map<String, Setting> settings = Setting.infoSettings;

        informationsAndButtonList = CollectionUtils.mapOf(
                "Ping", CollectionUtils.listOf(new GuiCheckBox(100, 0, 0, "Ping Overlay Enabled", settings.get("Ping").isEnabled()),
                        new GuiButton(2, 0, 0, "Move Ping Overlay")),
                "Day, Time", CollectionUtils.listOf(new GuiCheckBox(101, 0, 0, "Day Overlay Enabled", settings.get("Day").isEnabled()),
                        new GuiButton(2, 0, 0, "Move Day Overlay"),
                        new GuiCheckBox(102, 0, 0, "Time Overlay Enabled", settings.get("Time").isEnabled()),
                        new GuiButton(2, 0, 0, "Move Time Overlay")),
                "Position", CollectionUtils.listOf(new GuiCheckBox(103, 0, 0, "Position Overlay Enabled", settings.get("Position").isEnabled()),
                        new GuiButton(2, 0, 0, "Move X Overlay"),
                        new GuiButton(2, 0, 0, "Move Y Overlay"),
                        new GuiButton(2, 0, 0, "Move Z Overlay")),
                "Fps", CollectionUtils.listOf(new GuiCheckBox(104, 0, 0, "Fps Overlay Enabled", settings.get("Fps").isEnabled()),
                        new GuiButton(2, 0, 0, "Move Fps Overlay")),
                "Facing", CollectionUtils.listOf(new GuiCheckBox(105, 0, 0, "Facing Overlay Enabled", settings.get("Facing").isEnabled()),
                        new GuiButton(2, 0, 0, "Move Facing Overlay")));
    }

    public InformationOverlay(GuiScreen lastScreen) {
        super(lastScreen);
    }

    @Override
    public void initGui() {
        super.initGui();
        dropdownButtons.clear();

        GuiLabel label = new GuiLabel(fontRendererObj, 0, width / 2 - 50, 40, 100, 20, 50).setCentered();
        label.func_175202_a("§f§lGunter Essentials");
        label.func_175202_a("");
        label.func_175202_a("§f-> Information Overlay");

        labelList.add(label);

        prefixColorButton = new GuiButton(0, width / 2 - 100, pageContentHeight += pixelsPerButton, "Prefix Color: " + Setting.infoPrefixColor + Setting.infoPrefixColor.getFriendlyName());
        suffixColorButton = new GuiButton(0, width / 2 - 100, pageContentHeight += pixelsPerButton, "Suffix Color: " + Setting.infoSuffixColor + Setting.infoSuffixColor.getFriendlyName());
        valueColorButton = new GuiButton(0, width / 2 - 100, pageContentHeight += pixelsPerButton, "Value Color: " + Setting.infoValueColor + Setting.infoValueColor.getFriendlyName());
        buttonList.add(prefixColorButton);
        buttonList.add(suffixColorButton);
        buttonList.add(valueColorButton);

        AtomicInteger curIndex = new AtomicInteger();

        informationsAndButtonList.forEach((key, value) -> {
            dropdownButtons.add(new GuiButton(0, width / 2 - 100, pageContentHeight += pixelsPerButton, key));


            if (dropdownButtonsOpened.get(curIndex.getAndAdd(1))) {
                value.forEach(gui -> {
                    if (gui instanceof GuiCheckBox) {
                        pageContentHeight += pixelsPerCheckBox;
                        GuiCheckBox checkBox = (GuiCheckBox) gui;
                        checkBox.xPosition = width / 2 - 50;
                        checkBox.yPosition = pageContentHeight += pixelsPerCheckBox / 2;
                        buttonList.add(checkBox);
                    } else if (gui instanceof GuiButton) {
                        GuiButton button = (GuiButton) gui;
                        button.xPosition = width / 2 - 100;
                        button.yPosition = pageContentHeight += pixelsPerButton;
                        buttonList.add(button);
                    }

                });

                pageContentHeight += pixelsPerButton;
            }
        });

        buttonList.addAll(dropdownButtons);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button == prefixColorButton || button == valueColorButton || button == suffixColorButton) {
            int idx = colors.indexOf(button.displayString.split("Color: ")[1]);
            String color = colors.get(idx == colors.size() - 1 ? 0 : idx + 1);

            button.displayString = button.displayString.split(" ")[0] + " Color: " + color;

            if (button == prefixColorButton) {
                Setting.infoPrefixColor = colorsByPrefix.get(color);
            } else if (button == suffixColorButton) {
                Setting.infoSuffixColor = colorsByPrefix.get(color);
            } else if (button == valueColorButton) {
                Setting.infoValueColor = colorsByPrefix.get(color);
            }

            return;
        }

        int index = 0;
        for (GuiButton curButton : dropdownButtons) {
            if (curButton == button) {
                dropdownButtonsOpened.set(index, !dropdownButtonsOpened.get(index));
                InformationOverlay informationOverlay = new InformationOverlay(lastScreen);
                informationOverlay.scrollOffset = this.scrollOffset;
                Minecraft.getMinecraft().displayGuiScreen(informationOverlay);
                return;
            }
            index++;
        }

        if (button instanceof GuiCheckBox) {
            Setting.infoSettings.get(button.displayString.split(" ")[0]).switchEnabled();
            return;
        }

        if (button.id == 2) {
            String key = button.displayString.split(" ")[1];
            MoveObjectOverlay moveObjectOverlay = new MoveObjectOverlay(new CustomIngameUI(0x00000000, 0x00000000,
                    Setting.infoPrefixColor + key + Setting.infoSuffixColor + ": " + Setting.infoValueColor + "<Value>"), Setting.infoPositions.get(key), this);

            Minecraft.getMinecraft().displayGuiScreen(moveObjectOverlay);
        }
    }
}
