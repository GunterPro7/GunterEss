package com.GunterPro7uDerKatzenLord.Listener;

import com.GunterPro7uDerKatzenLord.Setting;
import com.GunterPro7uDerKatzenLord.Utils.HarpSong;
import com.GunterPro7uDerKatzenLord.Utils.TimeUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

public class HarpListener {
    private static boolean inHarpGui = false;

    /** Effective when 'inHarpGui' is true */
    private static HarpSong harpSong;

    @SubscribeEvent
    public void onInventoryClick(GuiScreenEvent.InitGuiEvent.Post event) {
        if (event.gui instanceof GuiChest && Setting.AUTO_HARP.isEnabled()) {
            GuiChest chest = (GuiChest) event.gui;
            IInventory chestInventory = ((ContainerChest) chest.inventorySlots).getLowerChestInventory();
            if (chestInventory.hasCustomName()) {
                String name = chestInventory.getName();

                if (name.startsWith("Harp - ")) {
                    inHarpGui = true;
                    harpSong = HarpSong.valueOfName(name.substring("Harp - ".length()));
                }
            }
        }
    }

    @SubscribeEvent
    public void onInventoryClose(GuiScreenEvent.KeyboardInputEvent.Pre event) {
        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) && inHarpGui && Setting.AUTO_HARP.isEnabled()) {
            if (event.gui instanceof GuiChest) {
                GuiChest chest = (GuiChest) event.gui;
                IInventory chestInventory = ((ContainerChest) chest.inventorySlots).getLowerChestInventory();
                if (chestInventory.hasCustomName()) {
                    String name = chestInventory.getName();

                    if (name.startsWith("Harp - ")) {
                        inHarpGui = false;
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (inHarpGui && Setting.AUTO_HARP.isEnabled()) {
            if (Minecraft.getMinecraft().currentScreen instanceof GuiChest) {
                GuiChest chest = (GuiChest) Minecraft.getMinecraft().currentScreen;

                // area to check for the blocks
                for (int i = 28; i <= 34; i++) {
                    Slot slot = chest.inventorySlots.getSlot(i);
                    if (slot.getHasStack() && slot.getStack().getItem().getRegistryName().contains("Wool")) {
                        int timeUntilExecute = harpSong.getInterval() - Setting.AUTO_HARP.getValue();

                        final int finalI = i;
                        Runnable runnable = () -> {
                            if (inHarpGui) {
                                chest.inventorySlots.slotClick(finalI, 0, 0, Minecraft.getMinecraft().thePlayer);
                            }
                        };

                        if (timeUntilExecute < 0) {
                            runnable.run();
                        } else {
                            TimeUtils.addToQueue(timeUntilExecute, runnable);
                        }
                    }
                }
            }
        }
    }
}
