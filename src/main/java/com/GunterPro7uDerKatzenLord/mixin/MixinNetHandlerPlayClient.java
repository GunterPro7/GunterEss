package com.GunterPro7uDerKatzenLord.mixin;

import com.GunterPro7uDerKatzenLord.LagHandler;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.server.S00PacketKeepAlive;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = NetHandlerPlayClient.class, priority = 2147483647)
public abstract class MixinNetHandlerPlayClient {
    @Shadow public abstract void addToSendQueue(Packet p_147297_1_);

    /**
     * @author GunterPro7
     * @reason To Catch ChatMessages sent by other mods
     */
    @Overwrite
    public void handleKeepAlive(S00PacketKeepAlive packetIn) {
        LagHandler.INSTANCE.calcPacket(packetIn);
        addToSendQueue(new C00PacketKeepAlive(packetIn.func_149134_c()));
    }
}
