package com.GunterPro7uDerKatzenLord.Listener;

import com.GunterPro7uDerKatzenLord.Gui.CustomIngameUI;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.event.entity.player.PlayerOpenContainerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

public class QuiverEmptyChecker {
    private static final QuiverEmptyChecker INSTANCE = new QuiverEmptyChecker();

    private int arrows;
    private boolean loadedArrows;

    private QuiverEmptyChecker() {

    }

    @SubscribeEvent
    public void enteredSkyblock(EnteredSkyblockEvent event) {
        CustomIngameUI customIngameUI = new CustomIngameUI(0x80000000, 0xFF000000, "GunterEss > Visit your Quiver to", "activate the Quiver Checker!");
        customIngameUI.drawInfoBox(100, 100, true);
    }

    public static QuiverEmptyChecker getInstance() {
        return INSTANCE;
    }

    public void reload(Container container) {

    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (Minecraft.getMinecraft().thePlayer != null) {
            if (Minecraft.getMinecraft().thePlayer.openContainer != null) {
                Container container = Minecraft.getMinecraft().thePlayer.openContainer;

                IChatComponent displayName = ((ContainerChest) container).getLowerChestInventory().getDisplayName();
                AdvancedChat.sendPrivateMessage(displayName.getFormattedText());
            }
        }
    }

    @SubscribeEvent
    public void onMouseEvent(MouseEvent event) {
        // Check if the event is on the client side
        if (event.button == 0) {
            // Get the current player
            EntityPlayer player = Minecraft.getMinecraft().thePlayer;
            // Get the item in the player's hand
            ItemStack item = player.getHeldItem();
            // Check if the button was pressed or released
            if (event.buttonstate) {
                // Button was pressed, do something
                player.addChatMessage(new ChatComponentText("You pressed the left mouse button with " + item.getDisplayName()));
            } else {
                // Button was released, do something else
                player.addChatMessage(new ChatComponentText("You released the left mouse button with " + item.getDisplayName()));
            }
        }
    }
}
