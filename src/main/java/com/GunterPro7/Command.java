package com.GunterPro7;

import com.GunterPro7.overlay.AutoKickOverlay;
import com.GunterPro7.listener.AdvancedChat;
import com.GunterPro7.listener.BackendService;
import com.GunterPro7.listener.MiscListener;
import com.GunterPro7.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

public class Command extends CommandBase {
    private static final String NAME = "gunteress";

    @Override
    public String getCommandName() {
        return NAME;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return NAME + "";
    }

    public static boolean enableSearchChat = false;

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length == 0) {
            MiscListener.setGunterOverlayNextTick = true;
        }
        if (args.length > 1) {
            if (Objects.equals(args[0], "copy")) {
                Utils.copyToClipBoard(AdvancedChat.clearChatMessage(String.join(" ", Arrays.copyOfRange(args, 1, args.length))));
            }
            if (args[0].equalsIgnoreCase("mcchat")) {
                String message = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
                AdvancedChat.sendChatMessageAsPlayer(message);
            }
            if (Objects.equals(args[0], "ignore")) {
                AutoKickOverlay.addIgnoredPlayer(args[1]);
            }
            if (args[0].equalsIgnoreCase("msg")) {
                String playerFrom = Minecraft.getMinecraft().thePlayer.getGameProfile().getName();
                String playerTo = args[1];
                String message = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
                try {
                    BackendService.getInstance().send("msg;" + playerFrom + ";" + playerTo + ";" + message);
                } catch (IOException e) {
                    AdvancedChat.sendPrivateMessage("§cThere was an error Reaching the GunterEss Backend, try again later.");
                    e.printStackTrace();
                }
            }
            if (args[0].equalsIgnoreCase("party")) {
                try {
                    Party.processCommand(Arrays.copyOfRange(args, 1, args.length));
                } catch (Exception e) {
                    AdvancedChat.sendPrivateMessage("§cThere was an error Reaching the GunterEss Backend, try again later.");
                    e.printStackTrace();
                }
            }
        } else if (args.length > 0) {
            if (Objects.equals(args[0], "help")) {
                AdvancedChat.sendPrivateMessage("§e§lWelcome To GunterEss!§r Some Commands: " +
                        "\n§a§lcopy <text> - §rCopies the text to clipboard\n§a§lmcchat <text> - §rWrite text into chat" +
                        "\n§a§lmsg <Player> <text> - §rSend <Player> a message when there are using GunterEss" +
                        "\n§a§party <help, ...> - §rCreate Parties with other GunterEss - Users");
            }
        }
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }
}
