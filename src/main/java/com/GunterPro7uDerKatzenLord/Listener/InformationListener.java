package com.GunterPro7uDerKatzenLord.Listener;

import com.GunterPro7uDerKatzenLord.Gui.CustomIngameUI;
import com.GunterPro7uDerKatzenLord.Setting;
import com.GunterPro7uDerKatzenLord.Utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.text.DecimalFormat;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.UnsupportedTemporalTypeException;
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

    private String lastDateFormat;
    private String lastTimeFormat;

    // Render Information
    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Post event) {
        if (event.type == RenderGameOverlayEvent.ElementType.TEXT) {
            Setting.INFO_SETTINGS.forEach((key, value) -> {
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
        Setting.Position position = Setting.INFO_POSITIONS.get(key);

        String v = "loading...";
        if (informationValues.containsKey(key)) {
            v = informationValues.get(key);
        }

        CustomIngameUI customIngameUI = new CustomIngameUI(0x00000000, 0x00000000,
                Setting.INFO_PREFIX_COLOR + key + Setting.INFO_SUFFIX_COLOR + ": " + Setting.INFO_VALUE_COLOR + v);
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

                // TODO rotation goes above 360 and under 360
                informationValues.put("Facing", df.format(player.rotationYaw) + " / " + df.format(player.rotationPitch));
            }

            LocalTime currentTime = LocalTime.now();
            if (lastTimeSec != currentTime.getSecond() || !lastTimeFormat.equals(Setting.INFO_TIME_FORMAT)) {
                String v;
                try {
                    v = currentTime.format(DateTimeFormatter.ofPattern(Setting.INFO_TIME_FORMAT));
                } catch (IllegalArgumentException | DateTimeException e) {
                    v = "<Invalid Format>";
                }

                informationValues.put("Time", v);
                lastTimeSec = currentTime.getSecond();
                lastTimeFormat = Setting.INFO_TIME_FORMAT;
            }

            // Lacy Tasks
            if (System.currentTimeMillis() - lacyLastTime >= 1000 || !lastDateFormat.equals(Setting.INFO_DATE_FORMAT)) {
                informationValues.put("Fps", String.valueOf(Minecraft.getDebugFPS()));

                LocalDate currentDate = LocalDate.now();
                if (currentDate.getDayOfYear() != lastDayOfYear || !lastDateFormat.equals(Setting.INFO_DATE_FORMAT)) {
                    String v;
                    try {
                        v = currentDate.format(DateTimeFormatter.ofPattern(Setting.INFO_DATE_FORMAT));
                    } catch (IllegalArgumentException | DateTimeException e) {
                        v = "<Invalid Format>";
                    }

                    informationValues.put("Day", v);
                    lastDayOfYear = currentDate.getDayOfYear();
                    lastDateFormat = Setting.INFO_DATE_FORMAT;
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

