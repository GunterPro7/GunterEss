package com.GunterPro7.hypixel;

import com.GunterPro7.Setting;
import com.GunterPro7.listener.AdvancedChat;
import com.GunterPro7.listener.Listener;
import com.GunterPro7.utils.McUtils;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

import static com.GunterPro7.Main.mc;

public class BlockHoverListener implements Listener {

    private static final BlockHoverListener INSTANCE = new BlockHoverListener();
    private Vec3 lastPlayerPos;
    private Vec3 lastPlayerLook;
    private double lastDistance;
    private BlockPos hoveredBlock;

    private BlockHoverListener() {
    }

    public static BlockHoverListener getInstance() {
        return INSTANCE;
    }

    public BlockPos getHoveredBlock() {
        return hoveredBlock;
    }

    @SubscribeEvent
    public void onRenderGameOverlayEvent(RenderGameOverlayEvent.Text event) {
        int tuned_transmission = 0; // TODO
        double distance = 57.0 + tuned_transmission;

        Vec3 playerPos = mc.thePlayer.getPositionEyes(event.partialTicks);
        Vec3 playerLook = mc.thePlayer.getLook(event.partialTicks);

        if (!McUtils.equalsVec3(lastPlayerLook, playerLook) || !McUtils.equalsVec3(lastPlayerPos, playerPos) || lastDistance != distance) {
            this.lastPlayerPos = playerPos;
            this.lastPlayerLook = playerLook;
            this.lastDistance = distance;

            Vec3 lookingPos = playerPos.addVector(playerLook.xCoord * distance, playerLook.yCoord * distance, playerLook.zCoord * distance);

            MovingObjectPosition obj = mc.theWorld.rayTraceBlocks(playerPos, lookingPos, true, false, true);

            if (obj != null && obj.getBlockPos() != null && isValidEtherwarpPos(obj)) {
                BlockPos blockPos = obj.getBlockPos();
                this.hoveredBlock = blockPos;

                //AdvancedChat.sendPrivateMessage(blockPos.getX() + " " + blockPos.getY() + " " + blockPos.getZ());
            }
        }
    }

    private boolean isValidEtherwarpPos(MovingObjectPosition obj) {
        BlockPos pos = obj.getBlockPos();
        EnumFacing sideHit = obj.sideHit;

        IBlockState blockState = mc.theWorld.getBlockState(pos);
        Block block = blockState.getBlock();

        if (block.getMaterial().isSolid()) {
            for (int i = 1; i <= 2; i++) {
                BlockPos newPos = pos.up(i);
                IBlockState newBlockState = mc.theWorld.getBlockState(newPos);
                Block newBlock = newBlockState.getBlock();

                if (sideHit == EnumFacing.UP) {
                    if (newBlock == Blocks.fire || newBlock == Blocks.skull || newBlock instanceof BlockLiquid) {
                        return false;
                    }
                } else if (newBlock instanceof BlockSign) {
                    return false;
                }

                if (newBlock instanceof BlockLadder || newBlock instanceof BlockDoor) {
                    return false;
                }

                if (!newBlock.isPassable(mc.theWorld, newPos)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

}
