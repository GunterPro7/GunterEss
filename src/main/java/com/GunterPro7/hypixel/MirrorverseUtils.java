package com.GunterPro7.hypixel;

import java.util.ArrayList;
import java.util.List;

public class MirrorverseUtils {
    public enum DanceRoom {
        MOVE,
        STAND,
        SNEAK,
        JUMP,
        DONT_JUMP,
        PUNCH;

        public static List<DanceRoom> parse(String string) {
            List<DanceRoom> danceRoomElements = new ArrayList<>();
            for (String part : string.split(" and ")) {
                danceRoomElements.add(parseString(part));
            }
            return danceRoomElements;
        }

        public static DanceRoom parseString(String string) {
            switch (string) {
                case "Move!": return MOVE;
                case "Stand!": return STAND;
                case "Jump!": return JUMP;
                case "Don't jump!": return DONT_JUMP;
                case "Punch!": return PUNCH;
                default: return null;
            }
        }
    }
}
