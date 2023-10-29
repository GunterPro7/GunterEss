package com.GunterPro7uDerKatzenLord.Utils;

import com.GunterPro7uDerKatzenLord.Gui.MoveObjectOverlay;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class Utils {
    public static final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    public static final Map<Long, Runnable> commandTasks = new HashMap<>();
    private static final List<String> ignoredMessages = new ArrayList<>();
    public static void copyToClipBoard(String text) {
        clipboard.setContents(new StringSelection(text), null);
    }

    public static void sendPrivateMessage(String text) {
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§a§lGunterEss > §r" + text));
    }

    public static int getLengthPerSecond(List<Long> cropTimeList) {
        if (cropTimeList.size() == 0) {
            return 0;
        }
        int count = 0;
        for (int i = cropTimeList.size()-1; i >= 0; i--) {
            if (System.currentTimeMillis() - cropTimeList.get(i) <= 1000) {
                count++;
            }
        }
        return count;
    }

    public static int parseInt(String text) {
        StringBuilder newInt = new StringBuilder();
        for (char part : text.toCharArray()) {
            if (Character.isDigit(part)) {
                newInt.append(part);
            }
        }
        return Integer.parseInt(newInt.toString());
    }

    public static String convertToMinecraftId(final int id) {
        final StringBuilder string = new StringBuilder();
        for (final char c : Integer.toString(id).toCharArray()) {
            string.append("§").append(c);
        }
        return string.toString();
    }

    public static String commaAfterThird(String input) {
        if (input == null || input.length() <= 3) {
            return input;
        }
        StringBuilder result = new StringBuilder(input);

        int length = input.length();
        for (int i = length - 3; i >= 1; i -= 3) {
            result.insert(i, ',');
        }
        return result.toString();
    }

    public static void executeCommand(String command, int waitTime) {
        execute(() -> Minecraft.getMinecraft().thePlayer.sendChatMessage(command), waitTime);
        sendPrivateMessage("§rSending Command '" + command + "'");
    }

    public static void execute(Runnable f, int waitTime) {
        commandTasks.put(System.currentTimeMillis() + waitTime, f);
    }

    public static boolean isIgnoredMessage(String text) {
        return false;
        // TODO
        // 1. Check if it contains only "-"
        // 2. Check if 'text' is in 'ignoredMessages'
        // 3. Fill 'ignoredMessages' List
    }
}
