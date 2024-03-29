package com.GunterPro7uDerKatzenLord.Listener;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Mouse;

import java.util.HashMap;
import java.util.Map;

public class ClientMouseEvent extends Event {
    public final int key;

    public ClientMouseEvent(int key) {
        this.key = key;
    }

    static class Press extends ClientMouseEvent {
        public Press(int key) {
            super(key);
        }
    }

    static class Release extends ClientMouseEvent {
        public Release(int key) {
            super(key);
        }
    }

    private static final Map<Integer, Boolean> buttonDown = new HashMap<>();

    static {
        buttonDown.put(0, false);
        buttonDown.put(1, false);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        for (int i = 0; i < 2; i++) {
            if (Mouse.isButtonDown(i)) {
                buttonDown.put(i, true);
                MinecraftForge.EVENT_BUS.post(new ClientMouseEvent.Press(i));
            } else {
                if (buttonDown.get(i)) {
                    buttonDown.put(i, false);
                    MinecraftForge.EVENT_BUS.post(new ClientMouseEvent.Release(i));
                }
            }
        }
    }
}
