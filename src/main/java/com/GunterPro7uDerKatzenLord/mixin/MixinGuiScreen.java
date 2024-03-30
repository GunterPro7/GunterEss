package com.GunterPro7uDerKatzenLord.mixin;

import com.GunterPro7uDerKatzenLord.Listener.AdvancedChat;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

import java.util.List;

@Mixin(value = GuiScreen.class, priority = 2147483647)
public abstract class MixinGuiScreen {
     /**
     * @author GunterPro7
     * @reason Render tooltip differently when scrolling
     */
    @Overwrite
    protected void renderToolTip(ItemStack stack, int x, int y) {


    }
}
