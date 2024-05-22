package com.GunterPro7;

import com.GunterPro7.event.ClientPacketEvent;
import com.GunterPro7.listener.Listener;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.LinkedList;

public class TpsHandler implements Listener {
    private static final TpsHandler INSTANCE = new TpsHandler();
    private static final int TPS_COUNT_TICKS = 60;

    private final LinkedList<Boolean> receivedTicks = new LinkedList<>();
    private boolean receivedSinceLastTick;


    private TpsHandler() {
    }

    public static TpsHandler getInstance() {
        return INSTANCE;
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
            if (receivedTicks.size() > TPS_COUNT_TICKS) {
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
