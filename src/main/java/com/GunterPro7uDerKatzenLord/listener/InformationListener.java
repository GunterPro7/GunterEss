package com.GunterPro7uDerKatzenLord.listener;

import com.GunterPro7uDerKatzenLord.Main;
import com.GunterPro7uDerKatzenLord.event.ClientBlockChangeEvent;
import com.GunterPro7uDerKatzenLord.event.ClientChangeWorldEvent;
import com.GunterPro7uDerKatzenLord.event.ClientMouseEvent;
import com.GunterPro7uDerKatzenLord.gui.CustomIngameUI;
import com.GunterPro7uDerKatzenLord.LagHandler;
import com.GunterPro7uDerKatzenLord.Setting;
import com.GunterPro7uDerKatzenLord.utils.McUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.text.DecimalFormat;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class InformationListener {

    private static final DecimalFormat DECIMAL_FORMAT_1 = new DecimalFormat("#.#");
    private static final DecimalFormat DECIMAL_FORMAT_2 = new DecimalFormat("0.00");

    public static final Map<String, String> informationValues = new HashMap<>();
    private BlockPos lastPos;
    private float lastFacingX = 361f;
    private float lastFacingY = 361f;
    private long lastTimeSec = -1;
    private int lastDayOfYear = -1;
    private int lastPing = -2;
    private long lacyLastTime = System.currentTimeMillis();
    private long lagLastTime = System.currentTimeMillis();

    private String lastDateFormat;
    private String lastTimeFormat;

    private LinkedList<Long> leftClickTimes = new LinkedList<>();
    private LinkedList<Long> rightClickTimes = new LinkedList<>();

    private final LinkedList<Long> frameTimes = new LinkedList<>();
    private final LinkedList<Long> blocksBrokenTimes = new LinkedList<>();

    // Render Information
    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Post event) {
        if (event.type == RenderGameOverlayEvent.ElementType.TEXT) {

            // Calculating Fps
            long curTime = System.currentTimeMillis();
            frameTimes.addLast(curTime);

            while (!frameTimes.isEmpty() && curTime - frameTimes.getFirst() > 1000) {
                frameTimes.removeFirst();
            }

            informationValues.put("Fps", String.valueOf(frameTimes.size()));

            // Calculating CPs
            while (!leftClickTimes.isEmpty() && curTime - leftClickTimes.getFirst() > 1000) {
                leftClickTimes.removeFirst();
            }
            while (!rightClickTimes.isEmpty() && curTime - rightClickTimes.getFirst() > 1000) {
                rightClickTimes.removeFirst();
            }

            informationValues.put("Cps", leftClickTimes.size() + " | " + rightClickTimes.size());

            // Calculating Walking Speed
            double destX = Main.mc.thePlayer.posX - Main.mc.thePlayer.prevPosX;
            double destY = Main.mc.thePlayer.posZ - Main.mc.thePlayer.prevPosZ;

            informationValues.put("Speed", DECIMAL_FORMAT_2.format(MathHelper.sqrt_double(destX * destX + destY * destY) * 20));

            // Showing Information
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
        customIngameUI.align(position.getAlign());
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
                informationValues.put("Facing", DECIMAL_FORMAT_1.format(player.rotationYaw) + " / " + DECIMAL_FORMAT_1.format(player.rotationPitch));
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

                int curPing = McUtils.getPing();
                if (lastPing != curPing) {
                    informationValues.put("Ping", String.valueOf(curPing));
                    lastPing = curPing;
                }

                lacyLastTime = System.currentTimeMillis();
            }

            if (System.currentTimeMillis() - lagLastTime >= 500) {
                InformationListener.informationValues.put("Lag", DECIMAL_FORMAT_1.format(LagHandler.INSTANCE.curLatency() * 10) + "%");

                lagLastTime = System.currentTimeMillis();
            }

            long curTime = System.currentTimeMillis();
            while (!blocksBrokenTimes.isEmpty() && curTime - blocksBrokenTimes.getFirst() > 1000) {
                blocksBrokenTimes.removeFirst();
            }

            informationValues.put("Blocks/s", String.valueOf(blocksBrokenTimes.size()));
        }
    }

    @SubscribeEvent
    public void onBlockBreak(final ClientBlockChangeEvent event) {
        blocksBrokenTimes.add(System.currentTimeMillis());
    }

    @SubscribeEvent
    public void onMouse(MouseEvent e) {
        if (e.buttonstate) {
            if (e.button == 0) {
                leftClickTimes.addLast(System.currentTimeMillis());
            } else if (e.button == 1) {
                rightClickTimes.addLast(System.currentTimeMillis());
            }
        }
    }

    @SubscribeEvent
    public void onWorldChange(final ClientChangeWorldEvent event) {
        LagHandler.INSTANCE.reset();
    }
}

