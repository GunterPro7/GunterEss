package com.GunterPro7.listener;

import com.GunterPro7.event.ClientBlockChangeEvent;
import com.GunterPro7.utils.MinecraftBlock;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.client.event.sound.SoundEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.List;

public class ClientBlockListener implements Listener {
    // Minecraft Ingame Sound needs to be turned on for this to work!
    private static final List<BlockCheckTask> tasksNextTick = new ArrayList<>();

    @SubscribeEvent
    public void onSound(final SoundEvent.SoundSourceEvent event) {
        if (event.name.startsWith("dig.")) {
            BlockPos blockPos = new BlockPos(event.sound.getXPosF(), event.sound.getYPosF(), event.sound.getZPosF());

            MovingObjectPosition lookingAt = Minecraft.getMinecraft().objectMouseOver;
            if (lookingAt != null && lookingAt.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                BlockPos pos = lookingAt.getBlockPos();
                if (!pos.equals(blockPos)) {
                    return;
                }
            }

            if (!Minecraft.getMinecraft().thePlayer.isSwingInProgress) {
                return;
            }

            int id = Block.getIdFromBlock(Minecraft.getMinecraft().theWorld.getBlockState(blockPos).getBlock());
            int dataValue = Minecraft.getMinecraft().theWorld.getBlockState(blockPos).getBlock().getMetaFromState(Minecraft.getMinecraft().theWorld.getBlockState(blockPos));

            tasksNextTick.add(new BlockCheckTask(blockPos, Minecraft.getMinecraft().theWorld.getBlockState(blockPos).getBlock().getLocalizedName(),
                    new MinecraftBlock(Minecraft.getMinecraft().theWorld.getBlockState(blockPos).getBlock(), dataValue)));
        }
    }

    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent event) {
        while (!tasksNextTick.isEmpty()) {
            BlockCheckTask blockCheckTask = tasksNextTick.get(0);
            tasksNextTick.remove(0);
            String newBlock = Minecraft.getMinecraft().theWorld.getBlockState(blockCheckTask.getBlockPos()).getBlock().getLocalizedName();
            if (!newBlock.equals(blockCheckTask.getOldBlock())) {
                MinecraftForge.EVENT_BUS.post(new ClientBlockChangeEvent(blockCheckTask.getBlockPos(), blockCheckTask.getMinecraftBlock()));
            }
        }
    }

    private static class BlockCheckTask {
        private final BlockPos blockPos;
        private final String oldBlock;
        private final MinecraftBlock minecraftBlock;

        public BlockCheckTask(BlockPos blockPos, String oldBlock, MinecraftBlock minecraftBlock) {
            this.blockPos = blockPos;
            this.oldBlock = oldBlock;
            this.minecraftBlock = minecraftBlock;
        }

        public BlockPos getBlockPos() {
            return blockPos;
        }

        public String getOldBlock() {
            return oldBlock;
        }

        public MinecraftBlock getMinecraftBlock() {
            return minecraftBlock;
        }
    }
}
