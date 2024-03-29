package com.GunterPro7uDerKatzenLord.Utils;

import com.GunterPro7uDerKatzenLord.Gui.CustomIngameUI;
import com.GunterPro7uDerKatzenLord.Setting;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.*;

public class GemstoneDisplay {
    public static final Map<Gemstone, Integer> gemstoneIntMap = new HashMap<>();

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Text event) {
        if (Setting.GEMSTONE_DISPLAY_POSITION.isEnabled()) {
            List<String> lines = new ArrayList<>();
            gemstoneIntMap.forEach((gemstone, count) -> {
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

            if (lines.size() > 0) {
                Setting.Position pos = Setting.GEMSTONE_DISPLAY_POSITION;
                CustomIngameUI ingameUI = new CustomIngameUI(0x00000000, 0xFF000000, lines);
                ingameUI.drawInfoBox(pos.getOffsetX(), pos.getOffsetY(), false);
            }
        }
    }

    @SubscribeEvent
    public void onChatMessage(ClientChatReceivedEvent event) {
        String text = event.message.getUnformattedText();
        if (text.startsWith("PRISTINE!")) {
            String[] parts = text.split("Flawed | Gemstone x|!");
            System.out.println(Arrays.deepToString(parts));
            Gemstone gemstone = Gemstone.valueOfShort(parts[2]);
            int count = Integer.parseInt(parts[3]);
            if (gemstoneIntMap.containsKey(gemstone)) {
                gemstoneIntMap.put(gemstone, gemstoneIntMap.get(gemstone) + count);
            } else {
                gemstoneIntMap.put(gemstone, count);
            }
        }
    }
}
