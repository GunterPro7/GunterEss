package com.GunterPro7uDerKatzenLord;

import com.GunterPro7uDerKatzenLord.Listener.AdvancedChat;
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

import java.io.IOException;

import static com.GunterPro7uDerKatzenLord.Listener.Listeners.collectionJson;

@Mod(modid = "GunterEss", useMetadata = true)
public class Main {
    public static final Minecraft mc = Minecraft.getMinecraft();
    public static boolean starting = true;
    public static final String VERSION = "1.1.1";
    public static final boolean DEV = true;


    @Mod.EventHandler
    public void serverStarting(final FMLInitializationEvent event) {
        System.out.println("INITIALIZING GunterEss :D");

        MinecraftForge.EVENT_BUS.register(new ClientBlockListener());
        MinecraftForge.EVENT_BUS.register(new Listeners());
        MinecraftForge.EVENT_BUS.register(new AdvancedChat());
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

        final String response;
        try {
            response = JsonHelper.fetch("http://49.12.101.156/GunterEss/backend.php?VERSION=" + VERSION + "&DEV=" + DEV);
        } catch (IOException e) {
            System.out.println("Backend Service not available");
            return;
        }

        boolean updateAvailable = Boolean.parseBoolean(response.substring("{\"update_available\":".length(), response.length() - 1));
        if (updateAvailable) {
            JsonHelper.downloadFile("http://49.12.101.156/GunterEss/latest.jar", "mods/GunterEss-1.1.0.jar");
            // TODO so was wie skytils mit autodeletion task
        }
    }
}

