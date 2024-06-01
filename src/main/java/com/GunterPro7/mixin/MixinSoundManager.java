package com.GunterPro7.mixin;

import com.GunterPro7.listener.ClientBlockListener;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SoundManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = SoundManager.class, priority = 2147483647)
public abstract class MixinSoundManager {

    @Inject(method = "playSound", at = @At(value = "HEAD", shift = At.Shift.AFTER))
    public void playSound(ISound p_sound, CallbackInfo callbackInfo) {
        ClientBlockListener.onSound(p_sound);
    }
}