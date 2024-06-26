package com.GunterPro7.listener;

import com.GunterPro7.event.ClientBlockChangeEvent;
import com.GunterPro7.event.ClientChangeWorldEvent;
import com.GunterPro7.event.ClientFishingEvent;
import com.GunterPro7.gui.CustomIngameUI;
import com.GunterPro7.gui.SearchChatGui;
import com.GunterPro7.overlay.AutoKickOverlay;
import com.GunterPro7.overlay.CollectionOverlay;
import com.GunterPro7.overlay.GunterEssOverlay;
import com.GunterPro7.Setting;
import com.GunterPro7.utils.*;
import com.GunterPro7.hypixel.Collections;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.GunterPro7.Command.enableSearchChat;
import static com.GunterPro7.Main.mc;

public class MiscListener implements Listener {
    public static final ExecutorService POOL = Executors.newCachedThreadPool();
    public static Long time = System.currentTimeMillis();
    public static final List<Long> cropTimeList = new ArrayList<>();
    public static final boolean compactChatEnabled = true;
    public static boolean setGunterOverlayNextTick;
    public static int farmingFortune;
    public static final Random RANDOM = new Random();
    private long totalCrops;
    public static String collectionJson;
    private WorldClient lastWorldClient;
    public static HashMap<String, Long> SERVER_COLLECTION_MAP = new HashMap<>();
    public static Collections CURRENT_COLLECTION = Collections.wheat;
    public static int CURRENT_LEVEL = 0;
    private static final String[] classList = new String[]{"Mage", "Berserk", "Healer", "Tank", "Archer"};

    @SubscribeEvent
    public void onFontRenderer(final RenderGameOverlayEvent.Post event) {
        if (event.type == RenderGameOverlayEvent.ElementType.TEXT) {
            if (Setting.COLLECTION_OVERLAY.isEnabled()) {
                Collections collection = CURRENT_COLLECTION;
                long collectionDropsPerHour = CURRENT_COLLECTION.getCollectionDropsPerHour();

                List<String> args = new ArrayList<>(Arrays.asList("§C§l" + collection.getSimpleName().toUpperCase(), "§b[LVL] " + collection.getLevel() + " ➤ " + CURRENT_LEVEL + "   59% 2h 30m",
                        "§e[Collected] " + Utils.commaAfterThird(String.valueOf(collection.getCrops())),
                        "§a[REWARDS]"));

                for (String part : CURRENT_COLLECTION.getCollection().getRewardsAsString(0, CURRENT_LEVEL)) {
                    args.add("§d  - " + part);
                }

                args.add("§f" + collection.getSimpleName() + "/ h: " + collectionDropsPerHour);

                CustomIngameUI customIngameUI = new CustomIngameUI(0x501E1E1E, 0x501E1E1E, args);
                customIngameUI.drawInfoBox(Setting.COLLECTION_OVERLAY, true);
            }
        }
    }

    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent event) {
        if (setGunterOverlayNextTick) {
            mc.displayGuiScreen(new GunterEssOverlay(null));
            setGunterOverlayNextTick = false;
        }
        if (Setting.COLLECTION_OVERLAY.isEnabled() && System.currentTimeMillis() - time > 180000) {
            // Create a Runnable that performs the HTTP request - OH YEAH I LIKE IT
            POOL.execute(() -> {
                try {
                    JsonUtils.collectionApiFetch();
                    collectionJson = JsonUtils.fetch("https://api.hypixel.net/resources/skyblock/collections");
                } catch (Exception e) {
                    System.out.println("Notwork disabled, trying again in 3 minutes!");
                }

                if (collectionJson != null) {
                    JsonObject jsonObject = new Gson().fromJson(collectionJson, JsonObject.class);
                    JsonUtils.addEntriesToEnum(jsonObject);
                }
            });
            time = System.currentTimeMillis();
        }

        if ((lastWorldClient == null || lastWorldClient != FMLClientHandler.instance().getWorldClient()) && FMLClientHandler.instance().getWorldClient() != null) {
            lastWorldClient = FMLClientHandler.instance().getWorldClient();

            MinecraftForge.EVENT_BUS.post(new ClientChangeWorldEvent(lastWorldClient));
        }
    }

    @SubscribeEvent
    public void onKeyPress(final InputEvent.KeyInputEvent event) {
        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            if (mc.currentScreen instanceof CollectionOverlay) {
                mc.displayGuiScreen(((CollectionOverlay) mc.currentScreen).getLastScreen());
            } else if (mc.currentScreen instanceof GunterEssOverlay) {
                mc.displayGuiScreen(((GunterEssOverlay) mc.currentScreen).getLastScreen());
            }
        }

    }


    @SubscribeEvent
    public void onBlockBreak(final ClientBlockChangeEvent event) {
        cropTimeList.add(System.currentTimeMillis());
        if (cropTimeList.size() % 25 == 0) {
            try {
                for (final NetworkPlayerInfo info : mc.getNetHandler().getPlayerInfoMap()) {
                    try {
                        String text = info.getDisplayName().getFormattedText();
                        if (text.contains("Farming Fortune:")) {
                            farmingFortune = Utils.parseInt(AdvancedChat.clearChatMessage(text.substring(17)));
                        }
                    } catch (NullPointerException ignored) {
                    }
                }
            } catch (Exception e) {
                System.out.println(e);
            }

        }
        double chance = 1 + farmingFortune * 0.01;
        int crops = 0;

        if (event.getMinecraftBlock().getId() == 141) {
            double carrotChance = 2.615186615 * chance;
            double d = carrotChance - (int) carrotChance;

            if (RANDOM.nextDouble() > d) {
                crops = (int) carrotChance;
            } else {
                crops = (int) carrotChance + 1;
            }
        }

        totalCrops += crops;
    }

    @SubscribeEvent
    public void onClientChatReceive(ClientChatReceivedEvent event) {
        if (Setting.AUTO_KICKER.isEnabled()) {
            String message = AdvancedChat.clearChatMessage(event.message.getUnformattedText());
            if (message.matches("Party Finder > .* joined the dungeon group! \\(.* Level \\d+\\)")) {
                System.out.println(message.indexOf("Level"));
                int index = message.indexOf("Level");
                if (index != -1) {
                    String level = message.substring(index + 6, message.length() - 1);
                    String player = message.split("Party Finder > ")[1].split(" ")[0];
                    if (AutoKickOverlay.getIgnoredPlayers().contains(player)) {
                        TimeUtils.addToQueue(350, () -> mc.thePlayer.sendChatMessage("/p kick " + player));
                        return;
                    }
                    String gameClass = message.split(" Level")[0].split("\\(")[1];
                    for (int i = 0; i < AutoKickOverlay.values.length; i++) {
                        boolean b = AutoKickOverlay.values[i];
                        if (b) {
                            String curClass = classList[i];
                            if (Objects.equals(gameClass, curClass)) {
                                TimeUtils.addToQueue(350, () -> mc.thePlayer.sendChatMessage("/p kick " + player));
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    public static final SearchChatGui searchChat = new SearchChatGui(mc);

    @SubscribeEvent
    public void searchInChat(RenderGameOverlayEvent.Chat event) {
        if (event.type == RenderGameOverlayEvent.ElementType.CHAT) {
            if (enableSearchChat) {
                if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
                    enableSearchChat = false;
                    mc.gameSettings.chatVisibility = EntityPlayer.EnumChatVisibility.FULL;
                    return;
                }

                GlStateManager.pushMatrix();
                GlStateManager.translate((float)event.posX, (float)event.posY, 0.0F);
                searchChat.drawChat(0);
                GlStateManager.popMatrix();

                if (!(mc.currentScreen instanceof GunterGuiChat)) {
                    mc.gameSettings.chatVisibility = EntityPlayer.EnumChatVisibility.HIDDEN;
                    searchChat.sortChatLines("");
                    mc.displayGuiScreen(new GunterGuiChat(searchChat));
                }
            }
        }
    }

    @SubscribeEvent
    public void listenOnKeyboardClick(GuiScreenEvent.KeyboardInputEvent event) {
        if (mc.ingameGUI.getChatGUI().getChatOpen()) {
            if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)) {
                if (Keyboard.isKeyDown(Keyboard.KEY_F)) {
                    enableSearchChat = true;
                }
            }
        }
    }

    private static int fishingRCDuration;

    @SubscribeEvent
    public void onFishEnter(ClientFishingEvent.FishOnHookEvent event) {
        if (Setting.AUTO_FISHING.isEnabled()) {
            fishingRCDuration = new Random().nextInt(5) + 3;
            TimeUtils.addToQueue(new Random().nextInt(250) + 600, () -> fishingRCDuration = new Random().nextInt(5) + 3);
        }
    }

    @SubscribeEvent
    public void fishingTick(TickEvent.ClientTickEvent event) {
        if (fishingRCDuration > 0) {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), fishingRCDuration != 1);

            fishingRCDuration--;
        }
    }

    //public static String lastTitle;
//
    //@SubscribeEvent
    //public void tickEventForDanceRoom(TickEvent.ClientTickEvent event) throws IllegalAccessException {
    //    String title = (String) ReflectionHelper.findField(GuiIngame.class, "displayedTitle", "field_175201_x").get(mc.ingameGUI);
    //    if (title != null && !title.isEmpty()) {
    //        lastTitle = title;
    //    }
//
//
    //    if (Keyboard.isKeyDown(Keyboard.KEY_G)) {
    //        for (DanceRoom danceRoom : MirrorverseUtils.DanceRoom.parse(lastTitle)) {
    //            new EntityPlayerSP().
    //        }
    //    }
    //}
}
