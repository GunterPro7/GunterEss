package com.GunterPro7;

import com.GunterPro7.event.ClientMouseEvent;
import com.GunterPro7.event.ClientFishingEvent;
import com.GunterPro7.listener.ItemLoreScroller;
import com.GunterPro7.listener.*;
import com.GunterPro7.moneyTracker.FarmingTracker;
import com.GunterPro7.moneyTracker.GemstoneDisplay;
import com.GunterPro7.moneyTracker.MoneyTrackerDisplay;
import com.GunterPro7.utils.CollectionUtils;
import com.GunterPro7.utils.JsonUtils;
import com.GunterPro7.utils.TimeUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.gui.ScaledResolution;
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
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.eventhandler.ListenerList;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.Mixins;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.GunterPro7.listener.MiscListener.collectionJson;

@Mod(modid = Main.MODID, useMetadata = true, version = Main.VERSION, guiFactory = "com.GunterPro7.GunterEssGuiFactory")
public class Main {
    public static final String MODID = "GunterEss";
    public static final String VERSION = "1.4.1";
    public static final Minecraft mc = Minecraft.getMinecraft();
    public static final ScaledResolution scaledResolution = new ScaledResolution(mc);
    public static boolean starting = true;
    public static final boolean DEV = false;
    public static File configDirectory;
    public boolean updateAvailable;
    private ByteArrayOutputStream gunterEssByteData;

    @Mod.EventHandler
    public void preServerStarting(final FMLPreInitializationEvent event) {
        configDirectory = new File(event.getModConfigurationDirectory().getAbsolutePath() + "/gunterEss");
        MixinBootstrap.init();
        Mixins.addConfiguration("mixins.GunterEss.json");
    }


    @Mod.EventHandler
    public void serverStarting(final FMLInitializationEvent event) {
        System.out.println("INITIALIZING GunterEss :D");

        List<Listener> listenersToRegister = CollectionUtils.listOf(new ClientBlockListener(), new MiscListener(),
                new InformationListener(), new TimeUtils(), new GemstoneDisplay(), new MoneyTrackerDisplay(),
                TpsHandler.getInstance(), PreventLabymodUpdater.getInstance(), new ClientMouseEvent(0),
                new ClientFishingEvent(null), AdvancedChat.getInstance(), new ItemLoreScroller(),
                FarmingTracker.getInstance()
                /*, new ItemLock(), new HarpListener(), QuiverEmptyChecker.getInstance() */);

        for (Listener listener : listenersToRegister) {
            MinecraftForge.EVENT_BUS.register(listener);
        }

        ClientCommandHandler.instance.registerCommand(new Command());
        if (Setting.COLLECTION_OVERLAY.isEnabled()) {
            JsonUtils.fetch("https://api.hypixel.net/resources/skyblock/collections", response -> {
                collectionJson = response;

                if (collectionJson != null) {
                    JsonObject jsonObject = new Gson().fromJson(collectionJson, JsonObject.class);
                    JsonUtils.addEntriesToEnum(jsonObject);
                }
            });
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> { // TODO das muss noch getestet werden!
            if (Setting.AUTO_UPDATES.isEnabled() && updateAvailable) {
                File[] files = new File("mods").listFiles();
                boolean installed = false;

                if (files != null) {
                    for (File file : files) {
                        if (file.getName().toLowerCase().startsWith("gunteress")) {
                            installed = JsonUtils.saveToFile(new File(file.getAbsolutePath()), gunterEssByteData);
                            break;
                        }
                    }
                }

                System.out.println("Newest Version of GunterEss installed: " + installed);
            }
        }));

        JsonUtils.fetch("http://49.12.101.156/GunterEss/VersionCheck.php?VERSION=" + VERSION + "&DEV=" + DEV, response -> {
            if (Boolean.parseBoolean(response.split("\"update_available\":")[1].split(",")[0])) {
                if (!Setting.AUTO_UPDATES.isEnabled()) {
                    TimeUtils.addToQueue(1500, this::sendUpdateAvailable);
                } else {
                    updateAvailable = true;
                    Optional<ByteArrayOutputStream> optionalData = JsonUtils.downloadFile("http://49.12.101.156/GunterEss/latest.jar");
                    if (optionalData.isPresent()) {
                        gunterEssByteData = optionalData.get();
                        System.out.println("Newest Version of GunterEss downloaded!");
                    } else {
                        TimeUtils.addToQueue(1500, this::sendUpdateAvailable);
                    }
                }
            }
        });
    }

    private void sendUpdateAvailable() {
        String url = "https://github.com/GunterPro7/GunterEss/releases";
        IChatComponent chatComponent = new ChatComponentText("§7New Version of §l§3GunterEss §r§7is available!")
                .appendSibling(new ChatComponentText("§c§l[Download]").setChatStyle(new ChatStyle()
                        .setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url))
                        .setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText("§7Download the latest Version of §a§lGunterEss §r§7here!")))));
        TimeUtils.addToQueue(chatComponent);
    }
}

