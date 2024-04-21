package com.GunterPro7uDerKatzenLord.Utils;

import com.GunterPro7uDerKatzenLord.Listener.AdvancedChat;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TimeUtils {
    private static final Map<Long, List<Runnable>> map = new HashMap<>();
    private static final List<IChatComponent> messagesToChat = new ArrayList<>();

    public TimeUtils() {

    }

    public static void addToQueue(IChatComponent message) {
        messagesToChat.add(message);
    }

    public static void addToQueue(int ms, Runnable f) {
        addToQueue(System.currentTimeMillis() + ms, f);
    }

    public static void addToQueue(long time, Runnable f) {
        List<Runnable> runnables = new ArrayList<>();

        if (map.containsKey(time)) {
            runnables = map.get(time);
        } else {
            map.put(time, runnables);
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

        if (messagesToChat.size() > 0 && Minecraft.getMinecraft().thePlayer != null) {
            for (IChatComponent iChatComponent : messagesToChat) {
                AdvancedChat.sendPrivateMessage(iChatComponent, true);
            }
            messagesToChat.clear();
        }
    }
}
