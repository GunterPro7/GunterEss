package com.GunterPro7uDerKatzenLord;

import com.GunterPro7uDerKatzenLord.event.ClientPacketEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TpsHandler {
    public static final TpsHandler INSTANCE = new TpsHandler();
    public static final int TPS_COUNT_TICKS = 60;

    private final LinkedList<Boolean> receivedTicks = new LinkedList<>();
    private boolean receivedSinceLastTick;


    private TpsHandler() {
    }

    @SubscribeEvent
    public void onClientPacketEvent(ClientPacketEvent event) {
        receivedSinceLastTick = true;
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            receivedTicks.addFirst(receivedSinceLastTick);

            receivedSinceLastTick = false;
            if (receivedTicks.size() >= TPS_COUNT_TICKS) {
                receivedTicks.removeLast();
            }
        }
    }

    public void reset() {
        receivedSinceLastTick = false;
    }

    public double curTps() {
        return Math.min(20d, (double) receivedTicks.stream().filter(tick -> tick).count() / 3);
    }
}
