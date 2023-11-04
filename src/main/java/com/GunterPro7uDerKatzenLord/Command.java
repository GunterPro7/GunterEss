package com.GunterPro7uDerKatzenLord;

import com.GunterPro7uDerKatzenLord.Gui.GunterAutoKickOverlay;
import com.GunterPro7uDerKatzenLord.Listener.Listeners;
import com.GunterPro7uDerKatzenLord.Utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import org.lwjgl.input.Mouse;

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

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length == 0) {
            Listeners.setGunterOverlayNextTick = true;
        }
        if (args.length > 1) {
            if (Objects.equals(args[0], "copy")) {
                Utils.copyToClipBoard(Utils.clearChatComponent(String.join(" ", Arrays.copyOfRange(args, 1, args.length))));
            }
            if (Objects.equals(args[0], "ignore")) {
                GunterAutoKickOverlay.addIgnoredPlayer(args[1]);
            }
        } else if (args.length > 0) {
            if (Objects.equals(args[0], "help")) {
                Utils.sendPrivateMessage("copy <text> - Copies the text to clipboard");
            } else if (Objects.equals(args[0], "test")) {
                //Utils.execute(() -> {
                //    new AutoDungeonJoiner();
                //}, 2500);
                //TESTSTOP = !TESTSTOP;

                int x = Minecraft.getMinecraft().ingameGUI.getChatGUI().getChatWidth() + 10;
                int y = Minecraft.getMinecraft().displayHeight - Mouse.getY();

                Utils.sendPrivateMessage("X: " + x + ", Y: " + y);
            }
        }
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }
}
