package com.GunterPro7;

import com.GunterPro7.listener.AdvancedChat;
import net.minecraft.client.Minecraft;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.GunterPro7.Main.mc;

public class Party {
    public static final List<Party> parties = new ArrayList<>();
    private static Party lastParty;
    private static Party defaultParty;
    private static Party toggledParty;
    private final String name;
    private String owner;
    private final List<String> players = new ArrayList<>();

    private Party(String name, String owner) {
        this.name = name;
        this.owner = owner;
        if (lastParty == null) lastParty = this;
        if (defaultParty == null) defaultParty = this;
        parties.add(this);
    }

    public static Party create(String name, String owner) throws IOException {
        Party party = new Party(name, owner);
        party.addPlayer(owner);
        BackendService.getInstance().send("party;" + name + ";create;" + owner);
        AdvancedChat.sendPrivateMessage("Successfully created the Party " + name);
        return party;
    }

    public static Party join(String name, String owner) throws IOException {
        Party party = new Party(name, owner);
        BackendService.getInstance().send("party;" + name + ";join");
        AdvancedChat.sendPrivateMessage("You joined the party " + name);
        AdvancedChat.sendPrivateMessage("Do /GunterEss party " + name + " list to view all Players");
        return party;
    }

    public boolean invitePlayer(String playerName) throws IOException {
        if (isOwner()) {
            BackendService.getInstance().send("party;" + getName() + ";invite;" + playerName);
            return true;
        }
        return false;
    }

    public boolean kickPlayer(String playerName) throws IOException {
        if (isOwner()) {
            players.remove(playerName);
            BackendService.getInstance().send("party;" + getName() + ";kick;" + playerName);
            return true;
        }
        return false;
    }

    public boolean disband() throws IOException {
        if (isOwner()) {
            parties.remove(this);
            BackendService.getInstance().send("party;" + getName() + ";disband");
            return true;
        }
        return false;
    }

    private void addPlayer(String playerName) {
        this.players.add(playerName);
    }

    private void removePlayer(String playerName) {
        this.players.remove(playerName);
    }

    public String getName() {
        return name;
    }

    public String getOwner() {
        return owner;
    }

    public void transfer(String newOwner) {
        this.owner = newOwner;
    }

    public List<String> getPlayers() {
        return players;
    }

    public boolean isOwner() {
        return mc.thePlayer.getGameProfile().getName().equals(owner);
    }

    public void sendMessage(String message) throws IOException {
        BackendService.getInstance().send("party;" + getName() + ";msg;" + message);
        AdvancedChat.sendPrivateMessage(getName() + " > " + mc.thePlayer.getGameProfile().getName() + ": " + message);
    }

    public void leave() throws IOException {
        parties.remove(this);
        BackendService.getInstance().send("party;" + getName() + ";leave");
        AdvancedChat.sendPrivateMessage("You left the party " + getName());
    }

    public void toggle() {
        if (toggledParty == this) {
            toggledParty = null;
        } else {
            toggledParty = this;
        }
    }

    public static boolean isAPartyToggled() {
        return toggledParty != null;
    }

    public static Party getToggledParty() {
        return toggledParty;
    }

    public static void processServerMessage(String message) {
        // so basically I sort by the start of the string, splittet by ;
        String[] parts = message.split(";");
        String partyName = parts[0];
        Party party = getPartyByName(partyName);
        if (party == null) {
            if (!parts[1].equals("invited")) {
                System.out.println("GunterEss -> Invalid Server Packet: Party not found + " + parts[0]);
                return;
            }
        }

        switch (parts[1]) {
            case "message":
                String playerName = parts[3];
                String messageSent = parts[4];
                AdvancedChat.sendPrivateMessage(partyName + " > " + playerName + ": " + messageSent);
                break;
            case "log":
                AdvancedChat.sendPrivateMessage(partyName + " > " + parts[2]);
                break;
            case "remove":
                party.removePlayer(parts[2]);
                break;
            case "accepted":
                party.addPlayer(parts[2]);
                break;
            case "kick":
                parties.remove(party);
                AdvancedChat.sendPrivateMessage("You got kicked from the Party " + partyName + "!");
                break;
            case "playerjoin":
                String newPlayer = parts[2]; // TODO check if in the new version Client & Backend work perfect together
                party.addPlayer(newPlayer);
                AdvancedChat.sendPrivateMessage(newPlayer + " has joined the party " + party.getName());
                break;
            case "playerkick":
            case "playerleave":
                String playerToKick = parts[2];
                party.removePlayer(playerToKick);
                AdvancedChat.sendPrivateMessage(party.getName() + " > " + playerToKick + " has " + (parts[1].equals("playerkick") ? "been kicked from" : "left") + " the party!");
                break;
            case "partydisband":
                String playerThatDisbanded = parts[2];
                parties.remove(party);
                AdvancedChat.sendPrivateMessage("The Party " + partyName + " has been disbanded by " + playerThatDisbanded);
                break;
            case "invited":
                String owner = parts[2];
                IChatComponent iChatComponent = new ChatComponentText(owner + " has invited you to party " + partyName + ".");
                IChatComponent part = new ChatComponentText("§e§l[CLICK HERE]§r");
                ChatStyle partChatStyle = new ChatStyle();
                partChatStyle.setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/GunterEss party join " + partyName + " " + owner));
                partChatStyle.setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText("This command will add you to the party " + partyName + "!")));
                part.setChatStyle(partChatStyle);
                AdvancedChat.sendPrivateMessage(iChatComponent.appendSibling(part).appendText(" to accept!"), true);
                break;
            case "joinedInit":
                for (String curPlayerName : Arrays.copyOfRange(parts, 2, parts.length)) {
                    party.addPlayer(curPlayerName);
                }
                break;
            case "transfer":
                String playerToTransfer = parts[2];
                party.transfer(playerToTransfer);
                AdvancedChat.sendPrivateMessage("The Party " + partyName + " has been transferred to " + playerToTransfer);
                break;
        }
    }

    public static void processCommand(String[] command) throws IOException {
        String com = command[0];
        if (com.equalsIgnoreCase("create")) {
            Party.create(command[1], mc.thePlayer.getGameProfile().getName());
            // TODO BACKEND-SIDE: a party can be created with the same name again. that results in messages sent to that party via a "solo" party. Always check this on backend side, and send an error as result to the client if it exists
        } else if (com.equalsIgnoreCase("help")) {
            AdvancedChat.sendPrivateMessage("§e§lGunterEss party System!§r Available Commands: \n-> §a§lchat§r <party-name> <message>\n" +
                    "-> §a§linvite§r <party-name> <player-name>\n-> §a§ltoggle§r <party-name>\n-> §a§lleave§r <party-name>\n-> §a§ldisband§r <party-name>\n" +
                    "-> §a§lkick§r <party-name> <player-name>\n-> §a§llist§r\n-> §a§llist§r <party-name>\n\n§eYou can be in multiple parties at once!");
        } else if (com.equalsIgnoreCase("invite")) {
            Party party = getPartyByName(command[1]);
            if (party != null) {
                if (!party.invitePlayer(command[2])) {
                    AdvancedChat.sendPrivateMessage("You are not the owner from the Party " + command[1]);
                }
            } else {
                AdvancedChat.sendPrivateMessage("Chat §3" + command[1] + " §rnot found!");
            }

        } else if (com.equalsIgnoreCase("toggle")) {
            Party chat = getPartyByName(command[1]);
            if (chat != null) {
                chat.toggle(); // TODO give feedback and send message imediatly to party chat without the minecraft ingame chat
            } else {
                AdvancedChat.sendPrivateMessage("Chat §3" + command[1] + " §rnot found!");
            }
        } else if (com.equalsIgnoreCase("join")) {
            String newPartyName = command[1];
            if (parties.stream().anyMatch(party -> party.name.equals(newPartyName))) {
                AdvancedChat.sendPrivateMessage("You are already in the Party " + newPartyName);
                return;
            }
            Party.join(command[1], command[2]); // part 1 = name, part 2 = owner
            // TODO backend fetch joined the party
        }
        else if (com.equalsIgnoreCase("chat") || com.matches("[dDrR]")) {
            Party party = null;
            if (com.equalsIgnoreCase("chat")) {
                party = getPartyByName(command[1]);
            } else if (com.equalsIgnoreCase("d")) {
                party = defaultParty;
            } else if (com.equalsIgnoreCase("r")) {
                party = lastParty;
            }

            if (party != null) {
                String message = String.join(" ", Arrays.copyOfRange(command, 2, command.length));
                party.sendMessage(message);
            } else {
                AdvancedChat.sendPrivateMessage("Chat §3" + command[1] + " §rnot found!");
            }
        } else if (com.equalsIgnoreCase("leave")) {
            Party party = getPartyByName(command[1]);
            if (party != null) {
                party.leave();
            } else {
                AdvancedChat.sendPrivateMessage("Party " + command[1] + " not found!");
            }
        } else if (com.equalsIgnoreCase("disband")) {
            Party party = getPartyByName(command[1]);
            if (party != null) {
                if (!party.disband()) {
                    AdvancedChat.sendPrivateMessage("You are not the owner of the party " + command[1]);
                } else {
                    AdvancedChat.sendPrivateMessage("You disbanded the party " + command[1]);
                }
            } else {
                AdvancedChat.sendPrivateMessage("Party " + command[1] + " not found!");
            }
        } else if (com.equalsIgnoreCase("kick")) {
            Party party = getPartyByName(command[1]);
            String playerName = command[2];
            if (party != null) {
                if (!party.kickPlayer(playerName)) {
                    AdvancedChat.sendPrivateMessage("You are not the owner of the party " + command[1]);
                }
            }
        } else if (com.equalsIgnoreCase("list")) {
            StringBuilder string;
            if (command.length > 1) {
                Party party = getPartyByName(command[1]);
                if (party == null) {
                    AdvancedChat.sendPrivateMessage("The Party " + command[1] + " has not been found!");
                    return;
                }
                string = new StringBuilder("§eFollowing Members are in the Party " + party.getName() + ": §r\n");
                for (String member : party.getPlayers()) {
                    string.append("- ").append(member).append("\n");
                }
            } else {
                string = new StringBuilder("§eYou are in the following parties: §r\n");
                for (Party party : parties) {
                    string.append("- ").append(party.getName()).append("\n");
                }
            }
            AdvancedChat.sendPrivateMessage(string.toString());
        }
    }

    public static Party getPartyByName(String partyName) {
        for (Party party : parties) {
            if (party.getName().equals(partyName)) {
                return party;
            }
        }
        return null;
    }
}
