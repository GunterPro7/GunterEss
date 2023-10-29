package com.GunterPro7uDerKatzenLord;

import com.GunterPro7uDerKatzenLord.Gui.GunterAutoKickOverlay;
import com.GunterPro7uDerKatzenLord.Listener.AutoDungeonJoiner;
import com.GunterPro7uDerKatzenLord.Listener.Listeners;
import com.GunterPro7uDerKatzenLord.Utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

import java.util.Arrays;
import java.util.Objects;

public class Command extends CommandBase {
    private static final String NAME = "gunterEss";

    @Override
    public String getCommandName() {
        return NAME;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return NAME + "";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length == 0) {
            Listeners.setGunterOverlayNextTick = true;
        }
        if (args.length > 1) {
            if (Objects.equals(args[0], "copy")) {
                Utils.copyToClipBoard(String.join(" ", Arrays.copyOfRange(args, 1, args.length)).replaceAll("§[0-9a-zA-Z]", ""));
            }
            if (Objects.equals(args[0], "ignore")) {
                GunterAutoKickOverlay.addIgnoredPlayer(args[1]);
            }
        } else if (args.length > 0) {
            if (Objects.equals(args[0], "help")) {
                Utils.sendPrivateMessage("copy <text> - Copies the text to clipboard");
            } else if (Objects.equals(args[0], "test")) {
                Utils.execute(() -> {
                    new AutoDungeonJoiner();
                }, 2500);
            }
        }
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }
}
