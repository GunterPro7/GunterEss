package com.GunterPro7.utils;

import com.GunterPro7.listener.AdvancedChat;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.List;

import static com.GunterPro7.Main.mc;

// This Class if for Utils related to Minecraft
public class McUtils {
    private static final int SLOT_SIZE = 18;
    private static final List<String> ignoredMessages = new ArrayList<>();

    public static boolean isTextFieldHovered(GuiTextField textField, int mouseX, int mouseY) {
        return mouseX >= textField.xPosition && mouseX <= textField.xPosition + textField.width &&
                mouseY >= textField.yPosition && mouseY <= textField.yPosition + textField.height;
    }

    public static NBTTagList getItemLore(ItemStack itemStack) {
        if (itemStack != null && itemStack.hasTagCompound()) {
            NBTTagCompound nbt = itemStack.getTagCompound();

            if (nbt != null && nbt.hasKey("display", 10)) {
                NBTTagCompound display = nbt.getCompoundTag("display");

                if (display != null && display.hasKey("Lore", 9)) {
                    return display.getTagList("Lore", 8);
                }
            }
        }

        return null;
    }

    public static boolean mcLoaded() {
        return mc.thePlayer != null;
    }

    private static int getMaxGuiScale() {
        int scaleFactor = 1;

        while (mc.displayWidth / (scaleFactor + 1) >= 320 && mc.displayHeight / (scaleFactor + 1) >= 240) {
            scaleFactor++;
        }

        return scaleFactor;
    }

    public static int getGuiScale() {
        return mc.gameSettings.guiScale == 0 ? getMaxGuiScale() : mc.gameSettings.guiScale;
    }

    public static int getPing() {
        NetworkPlayerInfo networkPlayerInfo = mc.getNetHandler().getPlayerInfo(mc.thePlayer.getUniqueID());
        if (networkPlayerInfo != null) {
            return networkPlayerInfo.getResponseTime();
        } else {
            return -1;
        }
    }

    public static boolean isUselessMessage(String text) {
        return text.matches("▬+|-+") || ignoredMessages.contains(text);
    }

    public static String readRowFromTabList(String containingText, boolean clearChatComponent) {
        for (final NetworkPlayerInfo info : mc.getNetHandler().getPlayerInfoMap()) {
            if (info != null && info.getDisplayName() != null) {
                String text = clearChatComponent ? AdvancedChat.clearChatMessage(info.getDisplayName().getFormattedText()) : info.getDisplayName().getFormattedText();
                if (text.contains(containingText)) {
                    return text;
                }
            }

        }

        return "";
    }

    public static Slot getInventorySlotUnderMouse() {
        int x = Mouse.getX();
        int y = Mouse.getY();

        int scaling = getGuiScale();

        GuiContainer container = (GuiContainer) mc.currentScreen;

        for (Slot slot : container.inventorySlots.inventorySlots) {
            if (isMouseOverSlot(slot, x / scaling, (container.height * scaling - y) / scaling) && slot.canBeHovered()) {
                return slot;
            }
        }

        return null;
    }

    private static boolean isMouseOverSlot(Slot slotIn, int mouseX, int mouseY) {
        return isPointInRegion(slotIn.xDisplayPosition, slotIn.yDisplayPosition, 16, 16, mouseX, mouseY, slotIn);
    }

    protected static boolean isPointInRegion(int left, int top, int right, int bottom, int pointX, int pointY, Slot slot) {
        GuiContainer container = (GuiContainer) mc.currentScreen;

        int guiLeft = (container.width - 176) / 2;
        int guiTop = (container.height - 166) / 2;
        pointX -= guiLeft;
        pointY -= guiTop;

        return pointX >= left - 1 && pointX < left + right + 1 && pointY >= top - 1 && pointY < top + bottom + 1;
    }

    public static Entity findEntityByName(String name) {
        double radius = 6.0;
        BlockPos pos = Minecraft.getMinecraft().thePlayer.getPosition();

        List<Entity> entities = Minecraft.getMinecraft().theWorld.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(
                pos.getX() - radius, pos.getY() - radius, pos.getZ() - radius,
                pos.getX() + radius, pos.getY() + radius, pos.getZ() + radius
        ));

        for (Entity entity : entities) {
            System.out.println("Entity Name: " + entity.getCustomNameTag());
            if (entity.hasCustomName() && entity.getCustomNameTag().equals(name)) {
                return entity;
            }
        }
        return null;
    }

    public static BlockPos findChestNearEntity(Entity entity, int radius) {
        BlockPos entityPos = new BlockPos(entity);

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    BlockPos checkPos = entityPos.add(x, y, z);
                    Block block = Minecraft.getMinecraft().theWorld.getBlockState(checkPos).getBlock();

                    if (block instanceof BlockChest) {
                        return checkPos;
                    }
                }
            }
        }
        return null;
    }

    public static void hoverBlock(BlockPos blockPos) {
        // TODO
    }
}
