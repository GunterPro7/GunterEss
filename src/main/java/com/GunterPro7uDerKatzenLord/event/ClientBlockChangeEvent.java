package com.GunterPro7uDerKatzenLord.event;

import com.GunterPro7uDerKatzenLord.utils.MinecraftBlock;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.eventhandler.Event;

public class ClientBlockChangeEvent extends Event {
    private final BlockPos blockPos;
    private final MinecraftBlock mcBlock;

    public ClientBlockChangeEvent(BlockPos blockPos, MinecraftBlock mcBlock) {
        this.blockPos = blockPos;
        this.mcBlock = mcBlock;
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }

    public MinecraftBlock getMinecraftBlock() {
        return mcBlock;
    }
}