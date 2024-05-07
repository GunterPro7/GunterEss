package com.GunterPro7uDerKatzenLord;

import net.minecraft.network.play.server.S00PacketKeepAlive;

public class LagHandler {
    public static final LagHandler INSTANCE = new LagHandler();

    public int count = 1;
    public long startTime;
    public long lastTime;
    public long curTime;

    private LagHandler() {
    }

    public void calcPacket(S00PacketKeepAlive packet) {
        int i = packet.func_149134_c();

        lastTime = curTime;
        curTime = System.currentTimeMillis();

        count++;
    }

    public void reset() {
        startTime = System.currentTimeMillis();
        count = 1;
    }

    public double curLatency() {
        double curInterval = curInterval();

        return Math.abs((System.currentTimeMillis() - lastTime) - curInterval) / curInterval;
    }

    public double curInterval() {
        return (double) (curTime - startTime) / count;
    }
}
