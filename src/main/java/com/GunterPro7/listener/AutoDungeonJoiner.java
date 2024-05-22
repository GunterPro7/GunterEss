package com.GunterPro7.listener;

import com.GunterPro7.hypixel.DungeonUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.inventory.Container;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.GunterPro7.Main.mc;

public class AutoDungeonJoiner extends ContainerInformation implements Listener {
    public AutoDungeonJoiner() {
        Container container = mc.thePlayer.openContainer;
        List<DungeonInformation> list = getDungeonPageInformation(container);

        System.out.println(list.size());
        for (DungeonInformation dungeonInformation : list) {
            if (!dungeonInformation.available && dungeonInformation.note.contains("s+")) {
                int indexSlot = dungeonInformation.getSlotIndex();

                sendClickPacket(container.windowId, indexSlot, 0, 0, container.getSlot(indexSlot));
                break;
            }
        }
    }

    public List<DungeonInformation> getDungeonPageInformation(Container container) {
        List<ItemContainerInformation> information = getContainerInformation(container);
        List<DungeonInformation> dungeonInformation = new ArrayList<>();
        //container.slotClick(2, 0, 0, Minecraft.getMinecraft().thePlayer);

        GuiScreen currentScreen = Minecraft.getMinecraft().currentScreen;
        int slot = 2;
        sendClickPacket(container.windowId, slot, 0, 0, container.getSlot(slot));


        for (ItemContainerInformation item : information) {
            DungeonInformation curInfo;
            try {
                curInfo = new DungeonInformation(item);
            } catch (IllegalArgumentException e) {
                continue;
            }
            dungeonInformation.add(curInfo);
        }
        return dungeonInformation;
    }

    static class DungeonInformation extends ItemContainerInformation {
        private final List<Player> players = new ArrayList<>();
        private String note = "";
        private DungeonUtils.Mode mode;
        private int floor;
        private int classLevelRequired;
        private int catacombsLevelRequired;
        private final boolean available;

        public DungeonInformation(String displayName, List<String> lore, int slotIndex) {
            super(displayName, lore, slotIndex);

            if (!AdvancedChat.clearChatMessage(lore.get(0)).endsWith("'s Party")) {
                throw new IllegalArgumentException("Not a valid dungeonInformationField ItemStack");
            }


            boolean membersNow = false;

            for (String l : lore) {
                String line = AdvancedChat.clearChatMessage(l);
                String[] parts = line.split(":");

                if (parts.length != 2) {
                    continue;
                }

                if (line.contains("Members:")) {
                    membersNow = true;
                } else if (membersNow) {
                    Player player;
                    try {
                        player = new Player(line);
                    } catch (IllegalArgumentException e) {
                        System.out.println("THIS IS AN ERROR OF THE TEST OF THE NEWEST VERSION:");
                        e.printStackTrace();
                        continue;
                    }
                    players.add(player);
                } else {
                    String key = parts[0].trim();
                    System.out.println(key);
                    String value = parts[1].trim();
                    System.out.println(value);
                    switch (key) {
                        case "Dungeon":
                            this.mode = DungeonUtils.Mode.parse(value);
                            break;
                        case "Floor":
                            this.floor = DungeonUtils.parseFloor(value);
                            break;
                        case "Note":
                            this.note = value;
                            break;
                        case "Dungeon Level Required":
                            this.catacombsLevelRequired = Integer.parseInt(value);
                            break;
                        case "Class Level Required":
                            this.classLevelRequired = Integer.parseInt(value);
                            break;
                    }
                }
            }

            available = AdvancedChat.clearChatMessage(lore.get(lore.size() - 1)).equals("Click to join!");

        }

        protected DungeonInformation(ItemContainerInformation information) {
            this(information.getDisplayName(), information.getLore(), information.getSlotIndex());
        }

        public void addPlayer(Player player) {
            players.add(player);
        }

        public void addPlayers(List<Player> players) {
            this.players.addAll(players);
        }

        public List<Player> getPlayers() {
            return players;
        }

        public String getNote() {
            return note;
        }

        public DungeonUtils.Mode getMode() {
            return mode;
        }

        public int getFloor() {
            return floor;
        }

        public int getClassLevelRequired() {
            return classLevelRequired;
        }

        public int getCatacombsLevelRequired() {
            return catacombsLevelRequired;
        }

        public boolean isAvailable() {
            return available;
        }

        @Override
        public String toString() {
            return "DungeonInformation{" +
                    "players=" + players +
                    ", note='" + note + '\'' +
                    ", mode=" + mode +
                    ", floor=" + floor +
                    ", classLevelRequired=" + classLevelRequired +
                    ", catacombsLevelRequired=" + catacombsLevelRequired +
                    '}';
        }
    }

    static class Player {
        private final String player;
        private final int level;
        private final DungeonUtils.Class dungeonClass;

        public Player(String player, int level, DungeonUtils.Class dungeonClass) {
            this.player = player;
            this.level = level;
            this.dungeonClass = dungeonClass;
        }

        protected Player(String string) {
            Pattern pattern = Pattern.compile("(.*): (.*?) \\((\\d+)\\)");
            Matcher matcher = pattern.matcher(string);

            if (matcher.matches()) {
                player = matcher.group(1);
                dungeonClass = DungeonUtils.Class.valueOf(matcher.group(2).toUpperCase());
                level = Integer.parseInt(matcher.group(3));
            } else {
                throw new IllegalArgumentException("Invalid Player");
            }
        }

        public String getPlayer() {
            return player;
        }

        public int getLevel() {
            return level;
        }

        public DungeonUtils.Class getDungeonClass() {
            return dungeonClass;
        }

        @Override
        public String toString() {
            return "Player{" +
                    "player='" + player + '\'' +
                    ", level=" + level +
                    ", dungeonClass=" + dungeonClass +
                    '}';
        }
    }
}
