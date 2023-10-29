package com.GunterPro7uDerKatzenLord.Utils;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.List;

public class AdvancedTimer {
    private final long milliseconds;
    private final List<Long> timeList = new ArrayList<>();


    public AdvancedTimer(long milliseconds) {
        this.milliseconds = milliseconds;
    }

    public void entry() {
        timeList.add(System.currentTimeMillis());
    }

    @SubscribeEvent
    private void onTick(TickEvent.ClientTickEvent event) {
        if (System.currentTimeMillis() - timeList.get(0) > milliseconds) {
            timeList.remove(0);
        }
    }
}
