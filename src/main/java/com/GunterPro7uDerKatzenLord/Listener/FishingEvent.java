package com.GunterPro7uDerKatzenLord.Listener;

import com.GunterPro7uDerKatzenLord.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.List;

import static com.GunterPro7uDerKatzenLord.Main.mc;

public class FishingEvent extends Event {
    private final Entity entity;

    private static boolean onlineOnHypixelSkyblock = true;
    private static final List<Entity> entities = new ArrayList<>();

    public FishingEvent(Entity entity) {
        this.entity = entity;
    }

    public static class FishOnHookEvent extends FishingEvent {
        public FishOnHookEvent(Entity entity) {
            super(entity);
        }
    }

    public static class FishMovingToHookEvent extends FishingEvent {
        public FishMovingToHookEvent(Entity entity) {
            super(entity);
        }
    }


    @SubscribeEvent
    public void listenForFishingEvent(TickEvent.ClientTickEvent event) {
        if (Setting.AUTO_FISHING.isEnabled() && mc.thePlayer != null) {
            EntityFishHook hook = mc.thePlayer.fishEntity;
            if (hook != null) {
                entities.removeIf(entity -> {
                    if (hook.getDistance(entity.posX, entity.posY, entity.posZ) < 12.5) {
                        if (entity instanceof EntityArmorStand) {
                            if (entity.getDisplayName().getFormattedText().equals("§e§l?§r")) {
                                MinecraftForge.EVENT_BUS.post(new FishMovingToHookEvent(entity));
                            }
                            if (entity.getDisplayName().getFormattedText().equals("§c§l!!!§r")) {
                                MinecraftForge.EVENT_BUS.post(new FishOnHookEvent(entity));
                                return true;
                            }
                            return false;
                        }
                    }
                    return true;
                });
            }
        }
    }

    @SubscribeEvent
    public void onEntitySpawn(EntityEvent.EntityConstructing event) {
        if (Setting.AUTO_FISHING.isEnabled() && mc.thePlayer != null && mc.thePlayer.fishEntity != null) {
            Entity entity = event.entity;
            entities.add(entity);
        }
    }
}
