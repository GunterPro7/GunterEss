package com.GunterPro7.listener;

import com.GunterPro7.event.ClientMouseEvent;
import com.GunterPro7.event.EnteredSkyblockEvent;
import com.GunterPro7.gui.CustomIngameUI;
import com.GunterPro7.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Random;

import static com.GunterPro7.Main.mc;

public class QuiverEmptyChecker implements Listener {
    private static final QuiverEmptyChecker INSTANCE = new QuiverEmptyChecker();

    private int arrows;
    private static final int maxArrows = 64 * 45;
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

        container.inventorySlots.forEach(slot -> {
            if (slot != null && slot.getStack() != null) {
                arrows += slot.getStack().stackSize;
            }

        });
    }

    @SubscribeEvent
    public void onChatMessage(ClientChatReceivedEvent event) {
        String message = AdvancedChat.clearChatMessage(event.message.getUnformattedText());
        if (message.matches("You filled your quiver with .* extra arrows!")) {
            arrows += Utils.parseInt(message.substring("You filled your quiver with ".length()).split(" ")[0]);
            if (arrows > maxArrows) {
                arrows = maxArrows;
            }
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (mc.thePlayer != null) {
            if (mc.thePlayer.openContainer != null) {
                Container container = mc.thePlayer.openContainer;

                if (container instanceof ContainerChest) {
                    IChatComponent displayName = ((ContainerChest) container).getLowerChestInventory().getDisplayName();
                    if (AdvancedChat.clearChatMessage(displayName.getFormattedText()).equals("Quiver")) {
                        reload(container);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onEntitySpawn(EntityEvent.EntityConstructing event) {
        System.out.println(event.entity.getClass());
        if (event.entity instanceof EntityArrow) {
            if (((EntityArrow) event.entity).shootingEntity != null)
                AdvancedChat.sendPrivateMessage(((EntityArrow) event.entity).shootingEntity.getDisplayName().getFormattedText());
        }
    }

    @SubscribeEvent
    public void onMouseEvent(ClientMouseEvent.Press event) {
        bowUse(); // TODO only call if bow is shooting (1: check time via bonus attack speed, 2: check if arrow is "shooting" away...
    }

    private void bowUse() {
        EntityPlayer player = mc.thePlayer;
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
                                infiniteQuiverLvl = Utils.convertToNumberFromRomNumber(AdvancedChat.clearChatMessage(loreString.toLowerCase().split("infinite quiver")[1].split(" ")[1].replaceAll(",", ""))); // This is temporary
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

    private void arrowUse() {
        arrows--;

        //AdvancedChat.sendPrivateMessage("Only " + arrows + " Arrows Left!");
    }
}
