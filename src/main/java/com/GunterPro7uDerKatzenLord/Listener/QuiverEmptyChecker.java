package com.GunterPro7uDerKatzenLord.Listener;

import com.GunterPro7uDerKatzenLord.Gui.CustomIngameUI;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.event.entity.player.PlayerOpenContainerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Random;

public class QuiverEmptyChecker {
    private static final QuiverEmptyChecker INSTANCE = new QuiverEmptyChecker();

    private int arrows;
    private boolean loadedArrows;
    private static final Random random = new Random();

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
        arrows = 0;
        loadedArrows = true;

        container.inventorySlots.forEach(slot -> arrows += slot.getStack().stackSize);
    }

    @SubscribeEvent
    public void onChatMessage(ClientChatReceivedEvent event) {
        // TODO if matches with something like this: You filled your quiver with 2,816 extra arrows!
        // TODO aktuellisieren des quiver coints
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (Minecraft.getMinecraft().thePlayer != null) {
            if (Minecraft.getMinecraft().thePlayer.openContainer != null) {
                Container container = Minecraft.getMinecraft().thePlayer.openContainer;

                if (container instanceof ContainerChest) {
                    IChatComponent displayName = ((ContainerChest) container).getLowerChestInventory().getDisplayName();
                    if (displayName.getFormattedText().equals("Quiver")) {
                        reload(container);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onMouseEvent(MouseEvent event) {
        if (event.button == 0) {
            EntityPlayer player = Minecraft.getMinecraft().thePlayer;
            ItemStack item = player.getHeldItem();

            if (item != null && item.getItem() instanceof ItemBow) {
                NBTTagCompound nbt = item.getTagCompound();
                int infiniteQuiverLvl = 0;

                if (nbt != null && nbt.hasKey("display", 10)) {
                    NBTTagCompound display = nbt.getCompoundTag("display");

                    if (display.hasKey("Lore", 9)) {
                        NBTTagList lore = display.getTagList("Lore", 8);

                        for (int i = 0; i < lore.tagCount(); i++) { // TODO nur wenn man einen pfeil weggschießt
                            String loreString = lore.getStringTagAt(i);

                            if (loreString.toLowerCase().contains("infinite quiver")) {
                                try {
                                    infiniteQuiverLvl = Integer.parseInt(loreString.toLowerCase().split("infinite quiver")[1].split(" ")[1]);
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }

                if (random.nextInt(100) > infiniteQuiverLvl * 3) {
                    arrowUse();
                }
            }
        }
    }

    private void arrowUse() {
        arrows--;

        AdvancedChat.sendPrivateMessage("Only " + arrows + " Arrows Left!");
    }
}
