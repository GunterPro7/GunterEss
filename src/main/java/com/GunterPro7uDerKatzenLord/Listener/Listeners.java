package com.GunterPro7uDerKatzenLord.Listener;

import com.GunterPro7uDerKatzenLord.Command;
import com.GunterPro7uDerKatzenLord.Gui.CustomIngameUI;
import com.GunterPro7uDerKatzenLord.Gui.GunterAutoKickOverlay;
import com.GunterPro7uDerKatzenLord.Gui.GunterCollectionOverlay;
import com.GunterPro7uDerKatzenLord.Gui.GunterOverlay;
import com.GunterPro7uDerKatzenLord.Main;
import com.GunterPro7uDerKatzenLord.Setting;
import com.GunterPro7uDerKatzenLord.Utils.Collections;
import com.GunterPro7uDerKatzenLord.Utils.JsonHelper;
import com.GunterPro7uDerKatzenLord.Utils.MessageInformation;
import com.GunterPro7uDerKatzenLord.Utils.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.intellij.lang.annotations.Subst;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    public void onFontRenderer(final RenderGameOverlayEvent event) {
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
            Minecraft.getMinecraft().displayGuiScreen(new GunterOverlay(Minecraft.getMinecraft().currentScreen));
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
    public void onBlockBreak(final ClientBlockListener.ClientBlockBreakEvent event) {
        cropTimeList.add(System.currentTimeMillis());
        if (cropTimeList.size() % 25 == 0) {
            try {
                for (final NetworkPlayerInfo info : Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap()) {
                    try {
                        String text = info.getDisplayName().getFormattedText();
                        if (text.contains("Farming Fortune:")) {
                            farmingFortune = Utils.parseInt(text.substring(17).replaceAll("§[0-9a-zA-Z]", ""));
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
    public void blockBreakEvent(ClientBlockListener.ClientBlockBreakEvent event) {
        //Utils.sendPrivateMessage(event.getMinecraftBlock().getName());
    }

    @SubscribeEvent
    public void onClientChatReceive(ClientChatReceivedEvent event) {
        if (Setting.AUTO_KICKER_ENABLED.isEnabled()) {
            String message = event.message.getUnformattedText().replaceAll("§[0-9a-zA-Z]", "");
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
}
