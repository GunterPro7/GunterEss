package com.GunterPro7;// Importe

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

public class TextureLoader {
    private boolean resourceLoaded = false;

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (Keyboard.isKeyDown(Keyboard.KEY_X)) {
            if (!resourceLoaded) {
                loadCustomResource();
                resourceLoaded = true;
            }
        } else {
            resourceLoaded = false;
        }
    }

    private void loadCustomResource() {
        ResourceLocation emeraldTexture = new ResourceLocation("minecraft", "textures/items/emerald.png");
        ResourceLocation diamondTexture = new ResourceLocation("minecraft", "textures/items/diamond.png");
        TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
        ITextureObject texture = textureManager.getTexture(emeraldTexture);
        if (texture != null) {
            ITextureObject newTexture = new SimpleTexture(diamondTexture);
            textureManager.loadTexture(emeraldTexture, newTexture);
        }
    }
}