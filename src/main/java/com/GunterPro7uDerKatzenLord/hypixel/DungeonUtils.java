package com.GunterPro7uDerKatzenLord.hypixel;

public class DungeonUtils {
    public static int parseFloor(String string) {
        int value = 0;

        for (char part : string.split(" ")[1].toCharArray()) {
            value += part == 'V' ? 5 : 1;
        }

        return value;
    }

    public enum Class {
        MAGE,
        ARCHER,
        BERSERK,
        HEALER,
        TANK
    }

    public enum Mode {
        CATACOMBS,
        MASTER_MODE;

        public static Mode parse(String mode) {
            switch (mode) {
                case "The Catacombs":
                    return CATACOMBS;
                case "Master Mode":
                    return MASTER_MODE;
                default:
                    return null;
            }
        }
    }
}
