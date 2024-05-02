package com.GunterPro7uDerKatzenLord.Listener;

import com.GunterPro7uDerKatzenLord.Gui.CustomIngameUI;
import com.GunterPro7uDerKatzenLord.Setting;
import com.GunterPro7uDerKatzenLord.Utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class InformationListener {

    private static final DecimalFormat df = new DecimalFormat("#.#");

    private final Map<String, String> informationValues = new HashMap<>();
    private BlockPos lastPos;
    private float lastFacingX = 361f;
    private float lastFacingY = 361f;
    private long lastTimeSec = -1;
    private int lastDayOfYear = -1;
    private int lastPing = -2;
    private long lacyLastTime = System.currentTimeMillis();

    // Render Information
    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Post event) {
        if (event.type == RenderGameOverlayEvent.ElementType.TEXT) {
            Setting.infoSettings.forEach((key, value) -> {
                if (value.isEnabled()) {
                    if (key.equals("Position")) {
                        for (String c : new String[]{"X", "Y", "Z"}) {
                            renderInformationOverlay(c);
                        }
                    } else {
                        renderInformationOverlay(key);
                    }
                }
            });
        }
    }

    private void renderInformationOverlay(String key) {
        Setting.Position position = Setting.infoPositions.get(key);

        String v = "loading...";
        if (informationValues.containsKey(key)) {
            v = informationValues.get(key);
        }

        CustomIngameUI customIngameUI = new CustomIngameUI(0x00000000, 0x00000000,
                Setting.infoPrefixColor + key + Setting.infoSuffixColor + ": " + Setting.infoValueColor + v);
        customIngameUI.drawInfoBox(position.getOffsetX(), position.getOffsetY(), true);
    }


    // Set Information
    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (Minecraft.getMinecraft().thePlayer != null) {
            EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;

            BlockPos curPos = player.getPosition();

            if (lastPos == null || !lastPos.equals(curPos)) {
                lastPos = curPos;

                informationValues.put("X", String.valueOf(curPos.getX()));
                informationValues.put("Y", String.valueOf(curPos.getY()));
                informationValues.put("Z", String.valueOf(curPos.getZ()));
            }
            if (lastFacingX != player.rotationYaw || lastFacingY != player.rotationPitch) {
                lastFacingX = player.rotationYaw;
                lastFacingY = player.rotationPitch;

                informationValues.put("Facing", df.format(player.rotationYaw) + " " + df.format(player.rotationPitch));
            }

            // Lacy Tasks
            if (System.currentTimeMillis() - lacyLastTime >= 1000) {
                informationValues.put("Fps", String.valueOf(Minecraft.getDebugFPS()));
                LocalTime currentTime = LocalTime.now();

                if (lastTimeSec != currentTime.getSecond()) {
                    informationValues.put("Time", currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"))); // TODO format in settings
                    lastTimeSec = currentTime.getSecond();

                    LocalDate currentDate = LocalDate.now();
                    if (currentDate.getDayOfYear() != lastDayOfYear) {
                        informationValues.put("Day", currentDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))); // TODO format in settings
                        lastDayOfYear = currentDate.getDayOfYear();
                    }
                }

                int curPing = Utils.getPing();
                if (lastPing != curPing) {
                    informationValues.put("Ping", String.valueOf(curPing));
                    lastPing = curPing;
                }

                lacyLastTime = System.currentTimeMillis();
            }

        }
    }

}

