package com.GunterPro7uDerKatzenLord;

import com.GunterPro7uDerKatzenLord.Gui.GunterAutoKickOverlay;
import com.GunterPro7uDerKatzenLord.Listener.AdvancedChat;
import com.GunterPro7uDerKatzenLord.Listener.BackendService;
import com.GunterPro7uDerKatzenLord.Listener.Listeners;
import com.GunterPro7uDerKatzenLord.Utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

public class Command extends CommandBase {
    public static boolean TESTSTOP = false;
    private static final String NAME = "gunterEss";

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
            Listeners.setGunterOverlayNextTick = true;
        }
        if (args.length > 1) {
            if (Objects.equals(args[0], "copy")) {
                Utils.copyToClipBoard(AdvancedChat.clearChatComponent(String.join(" ", Arrays.copyOfRange(args, 1, args.length))));
            }
            if (Objects.equals(args[0], "ignore")) {
                GunterAutoKickOverlay.addIgnoredPlayer(args[1]);
            }
            if (args[0].equalsIgnoreCase("msg")) {
                String playerFrom = Minecraft.getMinecraft().thePlayer.getGameProfile().getName();
                String playerTo = args[1];
                String message = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
                try {
                    BackendService.getInstance().send("msg;" + playerFrom + ";" + playerTo + ";" + message);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } else if (args.length > 0) {
            if (Objects.equals(args[0], "help")) {
                AdvancedChat.sendPrivateMessage("copy <text> - Copies the text to clipboard");
            } else if (Objects.equals(args[0], "test")) {
                Utils.execute(() -> {
                    try {
                        BackendService.getInstance().send("Test lol");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }, 500);
                //TESTSTOP = !TESTSTOP;

                //int x = Minecraft.getMinecraft().ingameGUI.getChatGUI().getChatWidth() + 10;
                //int y = Minecraft.getMinecraft().displayHeight - Mouse.getY();
//
                //AdvancedChat.sendPrivateMessage("X: " + x + ", Y: " + y);
            }
        }
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }
}
