package com.GunterPro7uDerKatzenLord;

import com.GunterPro7uDerKatzenLord.event.ClientMouseEvent;
import com.GunterPro7uDerKatzenLord.event.ClientFishingEvent;
import com.GunterPro7uDerKatzenLord.listener.ItemLoreScroller;
import com.GunterPro7uDerKatzenLord.listener.*;
import com.GunterPro7uDerKatzenLord.listener.GemstoneDisplay;
import com.GunterPro7uDerKatzenLord.utils.JsonHelper;
import com.GunterPro7uDerKatzenLord.utils.TimeUtils;
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

import static com.GunterPro7uDerKatzenLord.listener.MiscListener.collectionJson;

@Mod(modid = "GunterEss", useMetadata = true)
public class Main {
    public static final String MOD_ID = "GunterEss";
    public static final Minecraft mc = Minecraft.getMinecraft();
    public static boolean starting = true;
    public static final String VERSION = "1.3.0";
    public static final boolean DEV = false;
    public static File configDirectory;
    public boolean updateAvailable;

    @Mod.EventHandler
    public void preServerStarting(final FMLPreInitializationEvent event) {
        configDirectory = new File(event.getModConfigurationDirectory().getAbsolutePath() + "/gunterEss");
        MixinBootstrap.init();
        Mixins.addConfiguration("mixins.GunterEss.json");
    }


    @Mod.EventHandler
    public void serverStarting(final FMLInitializationEvent event) throws IllegalAccessException, IOException {
        System.out.println("INITIALIZING GunterEss :D");

        Setting.initSettings();

        MinecraftForge.EVENT_BUS.register(new ClientBlockListener());
        MinecraftForge.EVENT_BUS.register(new MiscListener());
        MinecraftForge.EVENT_BUS.register(new InformationListener());
        MinecraftForge.EVENT_BUS.register(new TimeUtils());
        MinecraftForge.EVENT_BUS.register(new GemstoneDisplay());
        //MinecraftForge.EVENT_BUS.register(new ItemLock());
        MinecraftForge.EVENT_BUS.register(new TextureLoader());
        MinecraftForge.EVENT_BUS.register(PreventLabymodUpdater.INSTANCE);
        MinecraftForge.EVENT_BUS.register(new ClientMouseEvent(0));
        MinecraftForge.EVENT_BUS.register(new ClientFishingEvent(null));
        MinecraftForge.EVENT_BUS.register(AdvancedChat.getInstance());
        MinecraftForge.EVENT_BUS.register(new ItemLoreScroller());
        //MinecraftForge.EVENT_BUS.register(new HarpListener());
        //MinecraftForge.EVENT_BUS.register(QuiverEmptyChecker.getInstance());
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

        Runtime.getRuntime().addShutdownHook(new Thread(() -> { // TODO das so umbauen das es die latest bereits am anfang runterlädt und erst jetzt installiert wird.
            if (Setting.AUTO_UPDATES.isEnabled() && updateAvailable) {
                File[] files = new File("mods").listFiles();
                boolean installed = false;

                if (files != null) {
                    for (File file : files) {
                        if (file.getName().toLowerCase().startsWith("gunteress")) {
                            installed = JsonHelper.downloadFile("http://49.12.101.156/GunterEss/latest.jar", new File(file.getAbsolutePath()));
                            break;
                        }
                    }
                }

                System.out.println("Newest Version of GunterEss Downloaded: " + installed);
            }
        }));

        JsonHelper.fetch("http://49.12.101.156/GunterEss/VersionCheck.php?VERSION=" + VERSION + "&DEV=" + DEV, response -> {
            if (Boolean.parseBoolean(response.split("\"update_available\":")[1].split(",")[0])) {
                if (!Setting.AUTO_UPDATES.isEnabled()) {
                    String url = "https://github.com/GunterPro7/GunterEss/releases";
                    IChatComponent chatComponent = new ChatComponentText("§7New Version of §l§3GunterEss §r§7is available!")
                            .appendSibling(new ChatComponentText("§c§l[Download]").setChatStyle(new ChatStyle()
                                    .setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url))
                                    .setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText("§7Download the latest Version of §a§lGunterEss §r§7here!")))));
                    TimeUtils.addToQueue(chatComponent);
                } else {
                    updateAvailable = true;
                }
            }
        });
    }
}

