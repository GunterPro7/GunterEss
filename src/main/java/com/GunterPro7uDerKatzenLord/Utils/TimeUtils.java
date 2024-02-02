package com.GunterPro7uDerKatzenLord.Utils;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TimeUtils {
    private static final Map<Long, List<Runnable>> map = new HashMap<>();

    public TimeUtils() {

    }

    public static void addToQueue(long millisecondsLeft, Runnable f) {
        long m = System.currentTimeMillis() + millisecondsLeft;

        List<Runnable> runnables = new ArrayList<>();

        if (map.containsKey(m)) {
            runnables = map.get(m);
        } else {
            map.put(m, runnables);
        }

        runnables.add(f);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        List<Long> keysToRemove = new ArrayList<>();

        for (Map.Entry<Long, List<Runnable>> entry : map.entrySet()) {
            long key = entry.getKey();

            if (System.currentTimeMillis() >= key) {
                entry.getValue().forEach(Runnable::run);
                keysToRemove.add(key);
            }
        }

        for (Long key : keysToRemove) {
            map.remove(key);
        }
    }
}
