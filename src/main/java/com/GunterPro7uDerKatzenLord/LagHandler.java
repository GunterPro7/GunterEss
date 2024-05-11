package com.GunterPro7uDerKatzenLord;

import com.GunterPro7uDerKatzenLord.event.ClientPacketEvent;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S00PacketKeepAlive;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class LagHandler {
    public static final LagHandler INSTANCE = new LagHandler();
    public long lastTime = System.currentTimeMillis();

    private LagHandler() {
    }

    @SubscribeEvent
    public void onClientPacketEvent(ClientPacketEvent event) {
        lastTime = System.currentTimeMillis();
    }

    public void reset() {

    }

    public double curLatency() {
        return (double) (System.currentTimeMillis() - lastTime) / 100;
    }
}
