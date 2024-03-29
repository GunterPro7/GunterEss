package com.GunterPro7uDerKatzenLord.Gui;

import com.GunterPro7uDerKatzenLord.Listener.AdvancedChat;
import com.GunterPro7uDerKatzenLord.Listener.ClientMouseEvent;
import com.GunterPro7uDerKatzenLord.Utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.*;

import static com.GunterPro7uDerKatzenLord.Main.mc;

public class ItemLoreScroller {
    private static final Map<ItemStack, Integer> ITEM_SCROLLS = new HashMap<>();

    public static boolean ITEM_LORE_SCROLL_ENABLED = true;
    public static int SHOWN_LORE_ROWS = 15;


    @SubscribeEvent
    public void onScroll(ClientMouseEvent.Scroll event) {
        if (Utils.mcLoaded()) {
            if (Minecraft.getMinecraft().currentScreen instanceof GuiContainer) {
                Slot slot = ((GuiContainer) Minecraft.getMinecraft().currentScreen).getSlotUnderMouse();
                if (slot != null) {
                    ItemStack item = slot.getStack();
                    if (item != null) {
                        if (!ITEM_SCROLLS.containsKey(item)) {
                            ITEM_SCROLLS.put(item, 0);
                        }

                        ITEM_SCROLLS.put(item, Math.max(ITEM_SCROLLS.get(item) - event.key / 120, 0));
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onRenderTooltip(ItemTooltipEvent event) {
        if (ITEM_SCROLLS.containsKey(event.itemStack)) {
            int scaling = mc.gameSettings.guiScale == 0 ? 4 : mc.gameSettings.guiScale;
            int fontHeight = mc.fontRendererObj.FONT_HEIGHT;

            int height = mc.displayHeight == -1 ? 2 : 200;

            int maxLines = height / fontHeight;

            AdvancedChat.sendPrivateMessage("height: " + height + ", maxLines: " + maxLines);


            int scroll = ITEM_SCROLLS.get(event.itemStack);

            String title = event.toolTip.remove(0);

            List<String> lore = new ArrayList<>(event.toolTip);


            if (lore.size() > maxLines) {
                int newIndex = 0;
                for (int i = scroll; i < scroll + maxLines; i++) {
                    event.toolTip.set(newIndex++, lore.get(i));
                }
//
                for (int i = newIndex; i < event.toolTip.size(); i++) {
                    event.toolTip.set(newIndex++, ""); // Dieser Code funktioniert, es wird erfolgreich auf "" umgeschrieben
                    // event.toolTip.remove(newIndex); // Dieser Code funktioniert NICHT! Warum? Bitte um hilfe
                }
//
                event.toolTip.add(0, title);
            }

        }

    }

}
