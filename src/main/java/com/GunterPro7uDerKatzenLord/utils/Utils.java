package com.GunterPro7uDerKatzenLord.utils;

import com.GunterPro7uDerKatzenLord.listener.AdvancedChat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;

// This Class if for Utils related to basic Java
// TODO move mc shit over to "McUtils"
public class Utils {
    public static final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    public static final Map<Long, Runnable> commandTasks = new HashMap<>();

    public static void copyToClipBoard(String text) {
        clipboard.setContents(new StringSelection(text), null);
    }

    public static int getLengthPerSecond(List<Long> timeList) {
        if (timeList.size() == 0) {
            return 0;
        }
        int count = 0;
        for (int i = timeList.size() - 1; i >= 0; i--) {
            if (System.currentTimeMillis() - timeList.get(i) <= 1000) {
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
        AdvancedChat.sendPrivateMessage("§rSending Command '" + command + "'");
    }

    public static void execute(Runnable f, int waitTime) {
        commandTasks.put(System.currentTimeMillis() + waitTime, f);
    }

    public static String formatTime(long time, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        dateFormat.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));

        return dateFormat.format(new Date(time));
    }

    public static String getJavaRuntime() throws IOException {
        String os = System.getProperty("os.name");
        String javaHome = System.getProperty("java.home");
        String fileSeparator = System.getProperty("file.separator");

        String java = javaHome + fileSeparator + "bin" + fileSeparator +
                (os != null && os.toLowerCase().startsWith("windows") ? "java.exe" : "java");

        if (!new File(java).isFile()) {
            throw new IOException("Unable to find suitable java runtime at $java");
        }
        return java;
    }

    public static int convertToNumberFromRomNumber(String romNumberString) {
        romNumberString = romNumberString.toUpperCase();

        Map<Character, Integer> romanValues = new HashMap<>();
        romanValues.put('I', 1);
        romanValues.put('V', 5);
        romanValues.put('X', 10);
        romanValues.put('L', 50);
        romanValues.put('C', 100);
        romanValues.put('D', 500);
        romanValues.put('M', 1000);

        int value = 0;
        int prevValue = 0;

        for (int i = romNumberString.length() - 1; i >= 0; i--) {
            char currentChar = romNumberString.charAt(i);
            if (!romanValues.containsKey(currentChar)) {
                // TODO implement if number still add (mods prop. changed the "x" to "10" for example)
            }
            int currentValue = romanValues.get(currentChar);

            if (currentValue < prevValue) {
                value -= currentValue;
            } else {
                value += currentValue;
            }

            prevValue = currentValue;
        }

        return value;
    } // TODO aufgehört in MCUtils zu geben

}
