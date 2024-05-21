package com.GunterPro7.event;

import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Event;

public class ClientChangeWorldEvent extends Event {
    private final World world;

    public ClientChangeWorldEvent(World world) {
        this.world = world;
    }

    public World getWorld() {
        return world;
    }
}
