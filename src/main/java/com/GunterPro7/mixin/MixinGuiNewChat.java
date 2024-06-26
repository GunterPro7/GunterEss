package com.GunterPro7.mixin;

import com.GunterPro7.listener.AdvancedChat;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = GuiNewChat.class, priority = 2147483647)
public abstract class MixinGuiNewChat {
    /**
     * @author GunterPro7
     * @reason To Catch ChatMessages sent by other mods
     */
    @Overwrite
    public void printChatMessage(IChatComponent chatComponent) {
        AdvancedChat.getInstance().onChatMessage(new ClientChatReceivedEvent((byte) 3, chatComponent));
    }
}
