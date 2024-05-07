package com.GunterPro7uDerKatzenLord.listener;

import com.GunterPro7uDerKatzenLord.event.ClientMouseEvent;
import com.GunterPro7uDerKatzenLord.utils.McUtils;
import com.GunterPro7uDerKatzenLord.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static com.GunterPro7uDerKatzenLord.Main.mc;

public class ItemLoreScroller {
    private static final Map<ItemStack, Integer> TOOLTIP_SCROLLS = new HashMap<>();
    private static final Map<ItemStack, Integer> ITEM_TOOLTIP_LENGTH = new HashMap<>();
    private static int lastGuiScale = mc.gameSettings.guiScale;
    private static int lastWidth = mc.displayWidth;
    private static int lastHeight = mc.displayHeight;

    @SubscribeEvent
    public void onScroll(ClientMouseEvent.Scroll event) {
        if (McUtils.mcLoaded()) {
            if (Minecraft.getMinecraft().currentScreen instanceof GuiContainer) {
                Slot slot = ((GuiContainer) Minecraft.getMinecraft().currentScreen).getSlotUnderMouse();
                if (slot != null) {
                    ItemStack item = slot.getStack();
                    if (item != null && TOOLTIP_SCROLLS.containsKey(item) && ITEM_TOOLTIP_LENGTH.containsKey(item)) {
                        int maxLines = getMaxLines();
                        int tooltipLength = ITEM_TOOLTIP_LENGTH.get(item);
                        int scrollOffset = TOOLTIP_SCROLLS.get(item) - event.key / 120;

                        TOOLTIP_SCROLLS.put(item, Math.min(Math.max(scrollOffset, 1), tooltipLength - maxLines - 1));
                    }
                }
            }
        }
    }

    public static void onScalingChange() {
        TOOLTIP_SCROLLS.clear();
        ITEM_TOOLTIP_LENGTH.clear();
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onRenderTooltip(ItemTooltipEvent event) {
        int curScale = mc.gameSettings.guiScale;
        int curHeight = mc.displayHeight;
        int curWidth = mc.displayWidth;

        if (lastGuiScale != curScale || lastHeight != curHeight || lastWidth != curWidth) {
            onScalingChange();
            lastGuiScale = curScale;
            lastHeight = curHeight;
            lastWidth = curWidth;
        }

        ITEM_TOOLTIP_LENGTH.put(event.itemStack, event.toolTip.size());
        int maxLines = getMaxLines();

        if (maxLines < event.toolTip.size()) {
            int scrollOffset;
            if (TOOLTIP_SCROLLS.containsKey(event.itemStack)) {
                scrollOffset = TOOLTIP_SCROLLS.get(event.itemStack);
            } else {
                scrollOffset = event.toolTip.size() - maxLines - 1;
                TOOLTIP_SCROLLS.put(event.itemStack, scrollOffset);
            }

            AtomicInteger atomic = new AtomicInteger(-scrollOffset);

            int tooltipSize = event.toolTip.size();
            String title = event.toolTip.remove(0);

            event.toolTip.removeIf(line -> {
                int curValue = atomic.addAndGet(1);
                return curValue < 0 || curValue > maxLines;
            });

            event.toolTip.add(0, title);
        }

    }

    private static int getMaxLines() {
        int scaling = mc.gameSettings.guiScale == 0 ? McUtils.getMaxGuiScale() : mc.gameSettings.guiScale;
        int fontHeight = mc.fontRendererObj.FONT_HEIGHT + 1;

        return mc.displayHeight / scaling / fontHeight - 3;
    }

}
