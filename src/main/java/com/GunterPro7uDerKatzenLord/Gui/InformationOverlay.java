package com.GunterPro7uDerKatzenLord.Gui;

import com.GunterPro7uDerKatzenLord.Setting;
import com.GunterPro7uDerKatzenLord.Utils.CollectionUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.client.config.GuiCheckBox;

import java.io.IOException;
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
        Map<String, Setting> settings = Setting.INFO_SETTINGS;

        GuiLabel labelTime = new GuiLabel(Minecraft.getMinecraft().fontRendererObj, 0, 0, 0, 60, 20, 0xFFFFFF).setCentered();
        labelTime.func_175202_a("Time Format:");
        GuiLabel labelDate = new GuiLabel(Minecraft.getMinecraft().fontRendererObj, 0, 0, 0, 60, 20, 0xFFFFFF).setCentered();
        labelDate.func_175202_a("Date Format:");

        GuiTextField textFieldDateFormat = new GuiTextField(1, Minecraft.getMinecraft().fontRendererObj, 0, 0, 100, 20);
        textFieldDateFormat.setText(Setting.INFO_DATE_FORMAT);
        GuiTextField textFieldTimeFormat = new GuiTextField(2, Minecraft.getMinecraft().fontRendererObj, 0, 0, 100, 20);
        textFieldTimeFormat.setText(Setting.INFO_TIME_FORMAT);

        informationsAndButtonList = CollectionUtils.mapOf(
                "Ping", CollectionUtils.listOf(new GuiCheckBox(100, 0, 0, "Ping Overlay Enabled", settings.get("Ping").isEnabled()),
                        new GuiButton(2, 0, 0, "Move Ping Overlay")),
                "Day, Time", CollectionUtils.listOf(new GuiCheckBox(101, 0, 0, "Day Overlay Enabled", settings.get("Day").isEnabled()),
                        new GuiButton(2, 0, 0, "Move Day Overlay"),
                        labelDate,
                        textFieldDateFormat,
                        new GuiCheckBox(102, 0, 0, "Time Overlay Enabled", settings.get("Time").isEnabled()),
                        new GuiButton(2, 0, 0, "Move Time Overlay"),
                        labelTime,
                        textFieldTimeFormat),
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
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);

        for (GuiTextField textField : textFieldList) {
            String message = textField.getText();
            if (textField.getId() == 1) {
                Setting.INFO_DATE_FORMAT = message;
            } else {
                Setting.INFO_TIME_FORMAT = message;
            }
        }
    }

    @Override
    public void initGui() {
        super.initGui();
        dropdownButtons.clear();

        GuiLabel label = new GuiLabel(fontRendererObj, 0, width / 2 - 50, 40, 100, 20, 0xFFFFFF).setCentered();
        label.func_175202_a("§lGunter Essentials");
        label.func_175202_a("");
        label.func_175202_a("-> Information Overlay");
        labelList.add(label);

        prefixColorButton = new GuiButton(0, width / 2 - 100, pageContentHeight += pixelsPerButton, "Prefix Color: " + Setting.INFO_PREFIX_COLOR + Setting.INFO_PREFIX_COLOR.getFriendlyName());
        suffixColorButton = new GuiButton(0, width / 2 - 100, pageContentHeight += pixelsPerButton, "Suffix Color: " + Setting.INFO_SUFFIX_COLOR + Setting.INFO_SUFFIX_COLOR.getFriendlyName());
        valueColorButton = new GuiButton(0, width / 2 - 100, pageContentHeight += pixelsPerButton, "Value Color: " + Setting.INFO_VALUE_COLOR + Setting.INFO_VALUE_COLOR.getFriendlyName());
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
                    } else if (gui instanceof GuiLabel) {
                        GuiLabel guiLabel = (GuiLabel) gui;
                        guiLabel.field_146162_g = width / 2 - 80;
                        guiLabel.field_146174_h = pageContentHeight + pixelsPerButton;
                        labelList.add(guiLabel);
                    } else if (gui instanceof GuiTextField) {
                        GuiTextField guiTextField = (GuiTextField) gui;
                        guiTextField.xPosition = width / 2;
                        guiTextField.yPosition = pageContentHeight += pixelsPerButton;
                        textFieldList.add(guiTextField);
                    }

                });

                pageContentHeight += pixelsPerButton;
            }
        });

        buttonList.addAll(dropdownButtons);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button == prefixColorButton || button == valueColorButton || button == suffixColorButton) {
            int idx = colors.indexOf(button.displayString.split("Color: ")[1]);
            String color = colors.get(idx == colors.size() - 1 ? 0 : idx + 1);

            button.displayString = button.displayString.split(" ")[0] + " Color: " + color;

            if (button == prefixColorButton) {
                Setting.INFO_PREFIX_COLOR = colorsByPrefix.get(color);
            } else if (button == suffixColorButton) {
                Setting.INFO_SUFFIX_COLOR = colorsByPrefix.get(color);
            } else if (button == valueColorButton) {
                Setting.INFO_VALUE_COLOR = colorsByPrefix.get(color);
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
            Setting.INFO_SETTINGS.get(button.displayString.split(" ")[0]).switchEnabled();
            return;
        }

        if (button.id == 2) {
            String key = button.displayString.split(" ")[1];
            MoveObjectOverlay moveObjectOverlay = new MoveObjectOverlay(new CustomIngameUI(0x00000000, 0x00000000,
                    Setting.INFO_PREFIX_COLOR + key + Setting.INFO_SUFFIX_COLOR + ": " + Setting.INFO_VALUE_COLOR + "<Value>"), Setting.INFO_POSITIONS.get(key), this);

            Minecraft.getMinecraft().displayGuiScreen(moveObjectOverlay);
        }
    }
}
