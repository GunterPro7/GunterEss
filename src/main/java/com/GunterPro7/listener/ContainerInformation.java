package com.GunterPro7.listener;

import net.minecraft.client.Minecraft;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C0EPacketClickWindow;

import java.util.ArrayList;
import java.util.List;

import static com.GunterPro7.Main.mc;

public class ContainerInformation implements Listener {
    public List<ItemContainerInformation> getContainerInformation(Container container) {
        List<ItemContainerInformation> list = new ArrayList<>();
        for (Slot slot : container.inventorySlots) {
            if (slot.getHasStack()) {
                ItemStack item = slot.getStack();
                list.add(new ItemContainerInformation(item.getDisplayName(), item.getTooltip(null, false), slot.getSlotIndex()));
            }
        }
        return list;
    }

    public void sendClickPacket(int windowId, int slotIndex, int mouseButton, int actionNumber, Slot slot) {
        C0EPacketClickWindow packet = new C0EPacketClickWindow(windowId, slotIndex, mouseButton, actionNumber, slot.getStack(), (short) 0);
        // Sende das Paket an den Server
        mc.getNetHandler().getNetworkManager().sendPacket(packet);
    }

    public static class ItemContainerInformation {
        private final String displayName;
        private final List<String> lore;
        private final int slotIndex;

        protected ItemContainerInformation(String displayName, List<String> lore, int slotIndex) {
            this.displayName = displayName;
            this.lore = lore;
            this.slotIndex = slotIndex;
        }

        public String getDisplayName() {
            return displayName;
        }

        public List<String> getLore() {
            return lore;
        }

        public int getSlotIndex() {
            return slotIndex;
        }

        @Override
        public String toString() {
            return displayName + " " + lore;
        }
    }
}
