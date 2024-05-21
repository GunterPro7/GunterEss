package com.GunterPro7.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.ArrayList;
import java.util.List;

// This Class if for Utils related to Minecraft
public class McUtils {
    private static final List<String> ignoredMessages = new ArrayList<>();

    public static boolean isTextFieldHovered(GuiTextField textField, int mouseX, int mouseY) {
        return mouseX >= textField.xPosition && mouseX <= textField.xPosition + textField.width &&
                mouseY >= textField.yPosition && mouseY <= textField.yPosition + textField.height;
    }

    public static NBTTagList getItemLore(ItemStack itemStack) {
        NBTTagCompound nbt = itemStack.getTagCompound();

        if (nbt != null && nbt.hasKey("display", 10)) {
            NBTTagCompound display = nbt.getCompoundTag("display");

            if (display.hasKey("Lore", 9)) {
                return display.getTagList("Lore", 8);
            }
        }

        return null;
    }

    public static boolean mcLoaded() {
        return Minecraft.getMinecraft().thePlayer != null;
    }

    public static int getMaxGuiScale() {
        Minecraft mc = Minecraft.getMinecraft();
        int scaleFactor = 1;

        while (mc.displayWidth / (scaleFactor + 1) >= 320 && mc.displayHeight / (scaleFactor + 1) >= 240) {
            scaleFactor++;
        }

        return scaleFactor;
    }

    public static int getPing() {
        NetworkPlayerInfo networkPlayerInfo = Minecraft.getMinecraft().getNetHandler().getPlayerInfo(Minecraft.getMinecraft().thePlayer.getUniqueID());
        if (networkPlayerInfo != null) {
            return networkPlayerInfo.getResponseTime();
        } else {
            return -1;
        }
    }

    public static boolean isUselessMessage(String text) {
        return text.matches("▬+|-+") || ignoredMessages.contains(text);
    }
}
