package com.GunterPro7uDerKatzenLord;

import com.GunterPro7uDerKatzenLord.Listener.ClientBlockListener;
import com.GunterPro7uDerKatzenLord.Listener.Listeners;
import com.GunterPro7uDerKatzenLord.Utils.JsonHelper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

import static com.GunterPro7uDerKatzenLord.Listener.Listeners.collectionJson;

@Mod(modid = "GunterEss", useMetadata = true)
public class Main {
    public static final Minecraft mc = Minecraft.getMinecraft();
    public static boolean starting = true;


    @Mod.EventHandler
    public void serverStarting(final FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new ClientBlockListener());
        MinecraftForge.EVENT_BUS.register(new Listeners());
        ClientCommandHandler.instance.registerCommand(new Command());
        if (Setting.COLLECTION_OVERLAY.isEnabled()) {
            try {
                JsonHelper.collectionApiFetch();
                collectionJson = JsonHelper.fetch("https://api.hypixel.net/resources/skyblock/collections");
            } catch (Exception e) {
                System.out.println("No network, retrying soon!");
            }

            if (collectionJson != null) {
                JsonObject jsonObject = new Gson().fromJson(collectionJson, JsonObject.class);
                JsonHelper.addEntriesToEnum(jsonObject);
            }
        }
    }
}

