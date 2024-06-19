package com.GunterPro7.hypixel;

import com.GunterPro7.listener.Listener;
import com.GunterPro7.utils.CollectionUtils;
import com.GunterPro7.utils.McUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class ThreeWeirdosSolver implements Listener {

    private static final List<String> solutions = CollectionUtils.listOf("The reward is not in my chest!", "At least one of them is lying, and the reward is not in .*'s chest.", "My chest doesn't have the reward we are all telling the truth.", "My chest has the reward and I'm telling the truth!", "The reward isn't in any of our chests.", "Both of them are telling the truth. Also, .* has the reward in their chest.");

    @SubscribeEvent
    public void onChatMessage(ClientChatReceivedEvent event) {
        String message = event.message.getUnformattedText();

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
            McUtils.hoverBlock(findChestByWeirdoName(message.split("\\[NPC] |:")[1]));
        }
    }

    private BlockPos findChestByWeirdoName(String name) {
        Entity entity = McUtils.findEntityByName("[NPC] " + name);
        return entity != null ? McUtils.findChestNearEntity(entity, 2) : null;
    }

}
