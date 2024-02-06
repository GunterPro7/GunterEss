package com.GunterPro7uDerKatzenLord;

import com.GunterPro7uDerKatzenLord.Gui.TextureLoader;
import com.GunterPro7uDerKatzenLord.Listener.AdvancedChat;
import com.GunterPro7uDerKatzenLord.Listener.ClientBlockListener;
import com.GunterPro7uDerKatzenLord.Listener.FishingEvent;
import com.GunterPro7uDerKatzenLord.Listener.Listeners;
import com.GunterPro7uDerKatzenLord.Utils.JsonHelper;
import com.GunterPro7uDerKatzenLord.Utils.TimeUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.Mixins;

import java.io.File;
import java.io.IOException;

import static com.GunterPro7uDerKatzenLord.Listener.Listeners.collectionJson;

@Mod(modid = "GunterEss", useMetadata = true)
public class Main {
    public static final Minecraft mc = Minecraft.getMinecraft();
    public static boolean starting = true;
    public static final String VERSION = "1.2.2";
    public static final boolean DEV = false;
    public static File configDirectory;
    public static File gunterEssDelFile;

    @Mod.EventHandler
    public void preServerStarting(final FMLPreInitializationEvent event) {
        MixinBootstrap.init();
        Mixins.addConfiguration("mixins.GunterEss.json");
    }


    @Mod.EventHandler
    public void serverStarting(final FMLInitializationEvent event) {
        System.out.println("INITIALIZING GunterEss :D");

        MinecraftForge.EVENT_BUS.register(new ClientBlockListener());
        MinecraftForge.EVENT_BUS.register(new Listeners());
        MinecraftForge.EVENT_BUS.register(new TimeUtils());
        MinecraftForge.EVENT_BUS.register(new TextureLoader());
        MinecraftForge.EVENT_BUS.register(new FishingEvent(null));
        MinecraftForge.EVENT_BUS.register(AdvancedChat.getInstance());
        ClientCommandHandler.instance.registerCommand(new Command());
        if (Setting.COLLECTION_OVERLAY.isEnabled()) {
            JsonHelper.fetch("https://api.hypixel.net/resources/skyblock/collections", response -> {
                collectionJson = response;

                if (collectionJson != null) {
                    JsonObject jsonObject = new Gson().fromJson(collectionJson, JsonObject.class);
                    JsonHelper.addEntriesToEnum(jsonObject);
                }
            });
        }

        JsonHelper.fetch("http://49.12.101.156/GunterEss/VersionCheck.php?VERSION=" + VERSION + "&DEV=" + DEV, response -> {
            if (Boolean.parseBoolean(response.split("\"update_available\":")[1].split(",")[0])) {
                String url = "https://github.com/GunterPro7/GunterEss/releases";
                IChatComponent chatComponent = new ChatComponentText("§7New Version of §l§3GunterEss §r§7is available!")
                        .appendSibling(new ChatComponentText("§c§l[Download]").setChatStyle(new ChatStyle()
                                        .setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url))
                                        .setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText("§7Download the latest Version of §a§lGunterEss §r§7here!")))));
                TimeUtils.addToQueue(chatComponent);
            }
        });
    }
}

