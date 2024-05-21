package com.GunterPro7.event;

import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class ClientPacketEvent extends Event {

    private final Packet<?> packet;
    private final CallbackInfo callbackInfo;

    public ClientPacketEvent(Packet<?> packet, CallbackInfo callbackInfo) {
        this.packet = packet;
        this.callbackInfo = callbackInfo;
    }

    public Packet<?> getPacket() {
        return packet;
    }

    public CallbackInfo getCallbackInfo() {
        return callbackInfo;
    }
}
