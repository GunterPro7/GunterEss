package com.GunterPro7.overlay;

import com.GunterPro7.Setting;
import com.GunterPro7.gui.CustomIngameUI;
import com.GunterPro7.utils.CollectionUtils;
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

    private static final List<Boolean> dropdownButtonsOpened;

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
                "Date, Time", CollectionUtils.listOf(new GuiCheckBox(101, 0, 0, "Date Overlay Enabled", settings.get("Date").isEnabled()),
                        new GuiButton(2, 0, 0, "Move Date Overlay"),
                        labelDate,
                        textFieldDateFormat,
                        new GuiCheckBox(102, 0, 0, "Time Overlay Enabled", settings.get("Time").isEnabled()),
                        new GuiButton(2, 0, 0, "Move Time Overlay"),
                        labelTime,
                        textFieldTimeFormat),
                "Position", CollectionUtils.listOf(new GuiCheckBox(103, 0, 0, "Position Overlay Enabled", settings.get("Position").isEnabled()),
                        new GuiButton(2, 0, 0, "Move X Overlay"),
                        new GuiButton(2, 0, 0, "Move Y Overlay"), // TODO wenn links, vom links speichern, wenn nahe an rechts, von rechts aus speichern, das selbe von oben und unten
                        new GuiButton(2, 0, 0, "Move Z Overlay")),
                "Fps", CollectionUtils.listOf(new GuiCheckBox(104, 0, 0, "Fps Overlay Enabled", settings.get("Fps").isEnabled()),
                        new GuiButton(2, 0, 0, "Move Fps Overlay")),
                "Facing", CollectionUtils.listOf(new GuiCheckBox(105, 0, 0, "Facing Overlay Enabled", settings.get("Facing").isEnabled()),
                        new GuiButton(2, 0, 0, "Move Facing Overlay")),
                "Tps", CollectionUtils.listOf(new GuiCheckBox(106, 0, 0, "Tps Overlay Enabled", settings.get("Tps").isEnabled()),
                        new GuiButton(2, 0, 0, "Move Tps Overlay")),
                "Speed", CollectionUtils.listOf(new GuiCheckBox(107, 0, 0, "Speed Overlay Enabled", settings.get("Speed").isEnabled()),
                        new GuiButton(2, 0, 0, "Move Speed Overlay")),
                "Blocks/s", CollectionUtils.listOf(new GuiCheckBox(108, 0, 0, "Blocks/s Overlay Enabled", settings.get("Blocks/s").isEnabled()),
                        new GuiButton(2, 0, 0, "Move Blocks/s Overlay")),
                "Cps", CollectionUtils.listOf(new GuiCheckBox(109, 0, 0, "Cps Overlay Enabled", settings.get("Cps").isEnabled()),
                        new GuiButton(2, 0, 0, "Move Cps Overlay")),
                "Gameday", CollectionUtils.listOf(new GuiCheckBox(110, 0, 0, "Gameday Overlay Enabled", settings.get("Gameday").isEnabled()),
                        new GuiButton(2, 0, 0, "Move Gameday Overlay"))

        );

        dropdownButtonsOpened = CollectionUtils.listOf(() -> false, informationsAndButtonList.size());
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

        title.func_175202_a("§lGunter Essentials");
        title.func_175202_a("");
        title.func_175202_a("-> Information Overlay");

        prefixColorButton = new GuiButton(1, width / 2 - 100, pageContentHeight += BUTTON_HEIGHT, "Prefix Color: " + Setting.INFO_PREFIX_COLOR + Setting.INFO_PREFIX_COLOR.getFriendlyName());
        suffixColorButton = new GuiButton(1, width / 2 - 100, pageContentHeight += BUTTON_HEIGHT, "Suffix Color: " + Setting.INFO_SUFFIX_COLOR + Setting.INFO_SUFFIX_COLOR.getFriendlyName());
        valueColorButton = new GuiButton(1, width / 2 - 100, pageContentHeight += BUTTON_HEIGHT, "Value Color: " + Setting.INFO_VALUE_COLOR + Setting.INFO_VALUE_COLOR.getFriendlyName());
        buttonList.add(prefixColorButton);
        buttonList.add(suffixColorButton);
        buttonList.add(valueColorButton);

        AtomicInteger curIndex = new AtomicInteger();

        informationsAndButtonList.forEach((key, value) -> {
            dropdownButtons.add(new GuiButton(0, width / 2 - 100, pageContentHeight += BUTTON_HEIGHT, key));


            if (dropdownButtonsOpened.get(curIndex.getAndAdd(1))) {
                value.forEach(gui -> {
                    if (gui instanceof GuiCheckBox) {
                        pageContentHeight += CHECKBOX_HEIGHT;
                        GuiCheckBox checkBox = (GuiCheckBox) gui;
                        checkBox.xPosition = width / 2 - 50;
                        checkBox.yPosition = pageContentHeight += CHECKBOX_HEIGHT / 2;
                        buttonList.add(checkBox);
                    } else if (gui instanceof GuiButton) {
                        GuiButton button = (GuiButton) gui;
                        button.xPosition = width / 2 - 80;
                        button.yPosition = pageContentHeight += BUTTON_HEIGHT;
                        buttonList.add(button);
                    } else if (gui instanceof GuiLabel) {
                        GuiLabel guiLabel = (GuiLabel) gui;
                        guiLabel.field_146162_g = width / 2 - 60;
                        guiLabel.field_146174_h = pageContentHeight + BUTTON_HEIGHT;
                        labelList.add(guiLabel);
                    } else if (gui instanceof GuiTextField) {
                        GuiTextField guiTextField = (GuiTextField) gui;
                        guiTextField.xPosition = width / 2 + 20;
                        guiTextField.yPosition = pageContentHeight += BUTTON_HEIGHT;
                        textFieldList.add(guiTextField);
                    }

                });

                pageContentHeight += BUTTON_HEIGHT;
            }
        });

        buttonList.addAll(dropdownButtons);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 1) {
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

            Setting.saveSettings();

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
