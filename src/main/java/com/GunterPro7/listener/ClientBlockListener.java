package com.GunterPro7.listener;

import com.GunterPro7.event.ClientBlockChangeEvent;
import com.GunterPro7.utils.MinecraftBlock;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.client.event.sound.SoundEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.List;

import static com.GunterPro7.Main.mc;

public class ClientBlockListener implements Listener {
    // Minecraft Ingame Sound needs to be turned on for this to work!
    private static final List<BlockCheckTask> tasksNextTick = new ArrayList<>();

    public static void onSound(final ISound sound) {
        if (sound.getSoundLocation().getResourcePath().startsWith("dig.")) {
            BlockPos blockPos = new BlockPos(sound.getXPosF(), sound.getYPosF(), sound.getZPosF());

            MovingObjectPosition lookingAt = mc.objectMouseOver;
            if (lookingAt != null && lookingAt.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                BlockPos pos = lookingAt.getBlockPos();
                if (!pos.equals(blockPos)) {
                    return;
                }
            }

            if (!mc.thePlayer.isSwingInProgress) {
                return;
            }

            int id = Block.getIdFromBlock(mc.theWorld.getBlockState(blockPos).getBlock());
            int dataValue = mc.theWorld.getBlockState(blockPos).getBlock().getMetaFromState(mc.theWorld.getBlockState(blockPos));

            tasksNextTick.add(new BlockCheckTask(blockPos, mc.theWorld.getBlockState(blockPos).getBlock().getLocalizedName(),
                    new MinecraftBlock(mc.theWorld.getBlockState(blockPos).getBlock(), dataValue)));
        }
    }

    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent event) {
        while (!tasksNextTick.isEmpty()) {
            BlockCheckTask blockCheckTask = tasksNextTick.get(0);
            tasksNextTick.remove(0);
            String newBlock = mc.theWorld.getBlockState(blockCheckTask.getBlockPos()).getBlock().getLocalizedName();
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
