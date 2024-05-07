package com.GunterPro7uDerKatzenLord.listener;

import com.GunterPro7uDerKatzenLord.gui.CustomIngameUI;
import com.GunterPro7uDerKatzenLord.gui.GunterAutoKickOverlay;
import com.GunterPro7uDerKatzenLord.gui.GunterCollectionOverlay;
import com.GunterPro7uDerKatzenLord.gui.GunterOverlay;
import com.GunterPro7uDerKatzenLord.Setting;
import com.GunterPro7uDerKatzenLord.utils.*;
import com.GunterPro7uDerKatzenLord.utils.Collections;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.GunterPro7uDerKatzenLord.Command.enableSearchChat;
import static com.GunterPro7uDerKatzenLord.Main.mc;

public class Listeners {
    public static final ExecutorService POOL = Executors.newCachedThreadPool();
    public static Long time = System.currentTimeMillis();
    public static final List<Long> cropTimeList = new ArrayList<>();
    public static final boolean compactChatEnabled = true;
    public static boolean setGunterOverlayNextTick;
    public static int farmingFortune;
    public static final Random RANDOM = new Random();
    private long totalCrops;
    public static String collectionJson;
    public static HashMap<String, Long> SERVER_COLLECTION_MAP = new HashMap<>();
    public static Collections CURRENT_COLLECTION = Collections.wheat;
    public static int CURRENT_LEVEL = 0;
    private static final String[] classList = new String[]{"Mage", "Berserk", "Healer", "Tank", "Archer"};

    @SubscribeEvent
    public void onFontRenderer(final RenderGameOverlayEvent.Post event) {
        if (event.type == RenderGameOverlayEvent.ElementType.TEXT) {
            if (Setting.MONEY_OVERLAY.isEnabled()) {
                FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;
                int scale = mc.gameSettings.guiScale;
                //fontRenderer.drawString("Hello this is !", Mouse.getEventX() / scale, (Display.getHeight()-Mouse.getEventY()) / scale, 0x100000);
                CustomIngameUI customIngameUI = new CustomIngameUI(0x80000000, 0xFF000000, "Mined Blocks: " + cropTimeList.size(), "Blocks / Second: " + Utils.getLengthPerSecond(cropTimeList), "Crops: " + totalCrops);
                customIngameUI.drawInfoBox(Setting.MONEY_OVERLAY.getOffsetX(), Setting.MONEY_OVERLAY.getOffsetY(), true);
            }
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
                customIngameUI.drawInfoBox(Setting.COLLECTION_OVERLAY.getOffsetX(), Setting.COLLECTION_OVERLAY.getOffsetY(), true);
            }
        }
    }

    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent event) {
        if (setGunterOverlayNextTick) {
            Minecraft.getMinecraft().displayGuiScreen(new GunterOverlay(null));
            setGunterOverlayNextTick = false;
        }
        if (Setting.COLLECTION_OVERLAY.isEnabled() && System.currentTimeMillis() - time > 180000) {
            // Create a Runnable that performs the HTTP request - OH YEAH I LIKE IT
            POOL.execute(() -> {
                try {
                    JsonHelper.collectionApiFetch();
                    collectionJson = JsonHelper.fetch("https://api.hypixel.net/resources/skyblock/collections");
                } catch (Exception e) {
                    System.out.println("Notwork disabled, trying again in 3 minutes!");
                }

                if (collectionJson != null) {
                    JsonObject jsonObject = new Gson().fromJson(collectionJson, JsonObject.class);
                    JsonHelper.addEntriesToEnum(jsonObject);
                }
            });
            time = System.currentTimeMillis();
        }
    }

    @SubscribeEvent
    public void onKeyPress(final InputEvent.KeyInputEvent event) {
        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            if (mc.currentScreen instanceof GunterCollectionOverlay) {
                mc.displayGuiScreen(((GunterCollectionOverlay) mc.currentScreen).getLastScreen());
            } else if (mc.currentScreen instanceof GunterOverlay) {
                mc.displayGuiScreen(((GunterOverlay) mc.currentScreen).getLastScreen());
            }
        }

    }


    @SubscribeEvent
    public void onBlockBreak(final ClientBlockListener.ClientBlockChangeEvent event) {
        cropTimeList.add(System.currentTimeMillis());
        if (cropTimeList.size() % 25 == 0) {
            try {
                for (final NetworkPlayerInfo info : Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap()) {
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
    public void blockBreakEvent(ClientBlockListener.ClientBlockChangeEvent event) {
        if (event.getMinecraftBlock().getItemStack() != null) {
            AdvancedChat.sendPrivateMessage(event.getMinecraftBlock().getName());
        } else {
            AdvancedChat.sendPrivateMessage(event.getMinecraftBlock().toString());
        }

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
                    if (GunterAutoKickOverlay.getIgnoredPlayers().contains(player)) {
                        Utils.executeCommand("/p kick " + player, 350);
                        return;
                    }
                    String gameClass = message.split(" Level")[0].split("\\(")[1];
                    for (int i = 0; i < GunterAutoKickOverlay.values.length; i++) {
                        boolean b = GunterAutoKickOverlay.values[i];
                        if (b) {
                            String curClass = classList[i];
                            if (Objects.equals(gameClass, curClass)) {
                                Utils.executeCommand("/p kick " + player, 350);
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    public static final SearchChatGui searchChat = new SearchChatGui(Minecraft.getMinecraft());

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (!Utils.commandTasks.isEmpty()) {
            for (long time : Utils.commandTasks.keySet()) {
                if (System.currentTimeMillis() > time) {
                    Runnable command = Utils.commandTasks.get(time);
                    command.run();
                    Utils.commandTasks.remove(time);
                }
            }
        }
    }

    @SubscribeEvent
    public void searchInChat(RenderGameOverlayEvent.Post event) {
        if (event.type == RenderGameOverlayEvent.ElementType.CHAT) {
            if (enableSearchChat) {
                if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
                    enableSearchChat = false;
                    mc.gameSettings.chatVisibility = EntityPlayer.EnumChatVisibility.FULL;
                    return;
                }
                searchChat.drawChat(0);
                if (!(Minecraft.getMinecraft().currentScreen instanceof GunterGuiChat)) {
                    mc.gameSettings.chatVisibility = EntityPlayer.EnumChatVisibility.HIDDEN;
                    searchChat.sortChatLines("");
                    mc.displayGuiScreen(new GunterGuiChat(searchChat));
                }
            }
        }
    }

    @SubscribeEvent
    public void listenOnKeyboardClick(GuiScreenEvent.KeyboardInputEvent event) {
        if (Minecraft.getMinecraft().ingameGUI.getChatGUI().getChatOpen()) {
            if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)) {
                if (Keyboard.isKeyDown(Keyboard.KEY_F)) {
                    enableSearchChat = true;
                }
            }
        }
    }

    private static int fishingRCDuration;

    @SubscribeEvent
    public void onFishEnter(FishingEvent.FishOnHookEvent event) {
        if (Setting.AUTO_FISHING.isEnabled()) {
            fishingRCDuration = new Random().nextInt(5) + 3;
            TimeUtils.addToQueue(new Random().nextInt(250) + 600, () -> fishingRCDuration = new Random().nextInt(5) + 3);
        }
    }

    @SubscribeEvent
    public void fishingTick(TickEvent.ClientTickEvent event) {
        if (fishingRCDuration > 0) {
            KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindUseItem.getKeyCode(), fishingRCDuration != 1);

            fishingRCDuration--;
        }
    }

    //public static String lastTitle;
//
    //@SubscribeEvent
    //public void tickEventForDanceRoom(TickEvent.ClientTickEvent event) throws IllegalAccessException {
    //    String title = (String) ReflectionHelper.findField(GuiIngame.class, "displayedTitle", "field_175201_x").get(Minecraft.getMinecraft().ingameGUI);
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
