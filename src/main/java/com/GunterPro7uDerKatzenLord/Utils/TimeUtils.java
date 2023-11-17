package com.GunterPro7uDerKatzenLord.Utils;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.HashMap;
import java.util.Map;

public class TimeUtils {
    private static final Map<Long, Runnable> map = new HashMap<>();

    public TimeUtils() {

    }

    public static void addToQueue(long millisecondsLeft, Runnable f) {
        map.put(System.currentTimeMillis() + millisecondsLeft, f);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) { // TODO same key at same tick task..
        for (Map.Entry<Long, Runnable> entry : map.entrySet()) {
            final long key = entry.getKey();

            if (System.currentTimeMillis() >= key) {
                entry.getValue().run();
                map.remove(key);
            }
        }
    }
}
