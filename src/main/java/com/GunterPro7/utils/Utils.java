package com.GunterPro7.utils;

import com.GunterPro7.Setting;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

// This Class if for Utils related to basic Java
public class Utils {
    public static final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

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
    }

    private static String addSpaceBeforeUppercase(String input) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char currentChar = input.charAt(i);
            if (i > 0 && Character.isUpperCase(currentChar)) {
                result.append(" ");
            }
            result.append(currentChar);
        }
        return result.toString();
    }

    private static String capitalizeFirstLetterAndLowercaseRest(String input) {
        StringBuilder result = new StringBuilder();
        boolean isFirstLetter = true;
        for (int i = 0; i < input.length(); i++) {
            char currentChar = input.charAt(i);
            if (!Character.isSpaceChar(currentChar)) {
                if (isFirstLetter) {
                    result.append(Character.toUpperCase(currentChar));
                    isFirstLetter = false;
                } else {
                    result.append(Character.toLowerCase(currentChar));
                }
            } else {
                result.append(currentChar);
                isFirstLetter = true;
            }
        }
        return result.toString();
    }

    public static String toTitleCase(String input) {
        return addSpaceBeforeUppercase(capitalizeFirstLetterAndLowercaseRest(input));
    }

    public static String toTitleCase2(String uppercaseInput) {
        return Arrays.stream(uppercaseInput.split("_"))
                .map(i -> i.substring(0, 1).toUpperCase() + i.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));
    }

    public static String toIngameTimeFormat(long time) {
        long days = time / 86400000, hours = (time % 86400000) / 3600000, minutes = (time % 3600000) / 60000, seconds = (time % 60000) / 1000;
        return (days > 0 ? days + "d " : "") + (hours == 0 && time >= 3600000 ? "0s" : hours > 0 ? hours + "h " : "") +
                (minutes == 0 && time >= 60000 ? "0s" : minutes > 0 ? minutes + "m " : "") + (seconds == 0 ? "0s" : seconds > 0 ? seconds + "s" : "");
    }

    public static String toIngameNumberFormat(long money) {
        String prefix;
        int div;

        if (money >= 1e9) {
            prefix = "b";
            div = (int) 1e9;
        } else if (money >= 1e6) {
            prefix = "m";
            div = (int) 1e6;
        } else if (money >= 1e3) {
            prefix = "k";
            div = (int) 1e3;
        } else {
            prefix = "";
            div = 1;
        }

        return roundToNDigits((double) money / div, 2) + prefix;
    }

    public static String roundToNDigits(double value, int digits) {
        return String.format("%." + digits + "f", value);
    }

    public static int safeToInteger(String numberString) {
        StringBuilder number = new StringBuilder();

        for (char c : numberString.toCharArray()) {
            if (Character.isDigit(c) || c == '.') {
                number.append(c);
            }
        }

        return number.length() <= 10 && number.length() > 0 ? Integer.parseInt(number.toString()) : -1;
    }

    public static String convertToColorString(String string) {
        String[] parts = string.split(":");

        return Setting.INFO_PREFIX_COLOR + parts[0] + Setting.INFO_SUFFIX_COLOR + ":" + Setting.INFO_VALUE_COLOR + parts[1];
    }
}
