package com.GunterPro7.moneyTracker;

import com.GunterPro7.gui.CustomIngameUI;
import com.GunterPro7.Setting;
import com.GunterPro7.listener.Listener;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.*;

public class GemstoneDisplay implements Listener {
    public static final Map<Gemstone, MoneyItem> gemstoneIntMap = new HashMap<>();

    static {
        MoneyHandler.getInstance().register(gemstoneIntMap);
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Text event) {
        if (Setting.GEMSTONE_DISPLAY.isEnabled()) {
            List<String> lines = new ArrayList<>();
            gemstoneIntMap.forEach((gemstone, moneyItem) -> {
                int count = (int) moneyItem.getCount();
                String line = "";
                if (count >= 32_000) {
                    line += "§b" + (count / 32_000) + " §6Perfect§f, ";
                    count %= 32_000;
                }

                if (count >= 6400) {
                    line += "§b" + (count / 6400) + " §5Flawless§f, ";
                    count %= 6400;
                }

                if (count >= 80) {
                    line += "§b" + (count / 80) + " §1Fine§f, ";
                    count %= 80;
                }
                line += "§b" + count + " §aFlawed " + gemstone.getColorString() + gemstone.getSign() + " " + gemstone.shortName() + " Gemstone";
                lines.add(line);
            });

            if (!lines.isEmpty()) {
                CustomIngameUI ingameUI = new CustomIngameUI(0x00000000, 0xFF000000, lines);
                ingameUI.drawInfoBox(Setting.GEMSTONE_DISPLAY, false);
            }
        }
    }

    @SubscribeEvent
    public void onChatMessage(ClientChatReceivedEvent event) {
        String text = event.message.getUnformattedText();
        if (text.startsWith("PRISTINE!")) {
            Gemstone gemstone;
            int count;
            try {
                String[] parts = text.split("Flawed | Gemstone x|!");
                System.out.println(Arrays.deepToString(parts));
                gemstone = Gemstone.valueOfShort(parts[2]);
                count = Integer.parseInt(parts[3]);
            } catch (Exception e) {
                System.err.println("GunterEss > Error while reading flawed gemstone information.");
                e.printStackTrace();
                return;
            }

            if (gemstoneIntMap.containsKey(gemstone)) {
                gemstoneIntMap.get(gemstone).addCount(count);
            } else {
                gemstoneIntMap.put(gemstone, new MoneyItem(gemstone, count));
            }
        }
    }

    public static void resetGemstone(Gemstone gemstone) {
        gemstoneIntMap.remove(gemstone);
    }

    public static void resetAll() {
        gemstoneIntMap.clear();
    }
}
