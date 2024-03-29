package com.GunterPro7uDerKatzenLord.Utils;

import com.GunterPro7uDerKatzenLord.Listener.AdvancedChat;
import net.minecraft.client.Minecraft;
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

public class Utils {
    public static final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    public static final Map<Long, Runnable> commandTasks = new HashMap<>();
    private static final List<String> ignoredMessages = new ArrayList<>();

    public static void copyToClipBoard(String text) {
        clipboard.setContents(new StringSelection(text), null);
    }

    public static int getLengthPerSecond(List<Long> cropTimeList) {
        if (cropTimeList.size() == 0) {
            return 0;
        }
        int count = 0;
        for (int i = cropTimeList.size() - 1; i >= 0; i--) {
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
        AdvancedChat.sendPrivateMessage("§rSending Command '" + command + "'");
    }

    public static void execute(Runnable f, int waitTime) {
        commandTasks.put(System.currentTimeMillis() + waitTime, f);
    }

    public static boolean isIgnoredMessage(String text) {
        return text.matches("▬+|-+") || ignoredMessages.contains(text);
    }

    public static String formatTime(long time, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        dateFormat.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));

        return dateFormat.format(new Date(time));
    }

    public static <K, V> HashMap<K, V> createMap(Class<K> keyClass, Class<V> valueClass, Object... elements) {
        if (elements.length % 2 != 0) {
            throw new IllegalArgumentException("An even number of elements is required to create a key-value map.");
        }

        final HashMap<K, V> map = new HashMap<>();
        for (int i = 0; i < elements.length; i += 2) {
            if (keyClass.isInstance(elements[i]) && valueClass.isInstance(elements[i + 1])) {
                K key = keyClass.cast(elements[i]);
                V value = valueClass.cast(elements[i + 1]);
                map.put(key, value);
            } else {
                throw new IllegalArgumentException("Invalid key-value pair at index " + i);
            }
        }

        return map;
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
}
