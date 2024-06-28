package com.GunterPro7.hypixel;

import com.GunterPro7.Setting;
import com.GunterPro7.listener.Listener;
import com.GunterPro7.utils.McUtils;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

import static com.GunterPro7.Main.mc;

public class ItemFeatures implements Listener {
    @SubscribeEvent
    public void onRenderGameOverlay(RenderBlockOverlayEvent event) {
        if(event.overlayType != RenderBlockOverlayEvent.OverlayType.BLOCK) return; // TODO not working
        if (Setting.AOTV_HOVER.isEnabled() && Keyboard.isKeyDown(mc.gameSettings.keyBindSneak.getKeyCode())) {
            McUtils.hoverBlock(BlockHoverListener.getInstance().getHoveredBlock());
        }
    }
}
