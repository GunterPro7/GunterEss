package com.GunterPro7.hypixel;

import com.GunterPro7.listener.AdvancedChat;
import com.GunterPro7.listener.Listener;
import com.GunterPro7.utils.CollectionUtils;
import com.GunterPro7.utils.McUtils;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

public class ThreeWeirdosSolver implements Listener {

    private static final ThreeWeirdosSolver INSTANCE = new ThreeWeirdosSolver();
    private static final List<String> solutions = CollectionUtils.listOf("The reward is not in my chest!", "At least one of them is lying, and the reward is not in .*'s chest.", "My chest doesn't have the reward we are all telling the truth.", "My chest has the reward and I'm telling the truth!", "The reward isn't in any of our chests.", "Both of them are telling the truth. Also, .* has the reward in their chest.");

    public static ThreeWeirdosSolver getInstance() {
        return INSTANCE;
    }

    private ThreeWeirdosSolver() {}

    @SubscribeEvent
    public void onChatMessage(ClientChatReceivedEvent event) {
        String message = AdvancedChat.clearChatMessage(event.message.getUnformattedText()); // TODO idfk why this does not get called at some moments.

        if (message.startsWith("[NPC] ")) {
            handleThreeWeirdoMessage(message);
        }
    }

    private void handleThreeWeirdoMessage(String message) {
        boolean found = false;
        for (String solution : solutions) {
            if (message.matches(".*: " + solution)) {
                found = true;
                break;
            }
        }

        if (found) {
            System.out.println("GunterEssTest -> " + message.split("\\[NPC] |:")[1]);
            BlockPos pos = findChestByWeirdoName(message.split("\\[NPC] |:")[1]);
            if (pos != null) {
                McUtils.hoverBlock(pos);
            }
        }
    }

    private BlockPos findChestByWeirdoName(String name) {
        Entity entity = McUtils.findEntityByName("[NPC] " + name);
        return entity != null ? McUtils.findChestNearEntity(entity, 2) : null;
    }

}
