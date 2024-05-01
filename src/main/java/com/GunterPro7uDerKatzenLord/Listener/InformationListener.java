package com.GunterPro7uDerKatzenLord.Listener;

import com.GunterPro7uDerKatzenLord.Gui.CustomIngameUI;
import com.GunterPro7uDerKatzenLord.Setting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.HashMap;
import java.util.Map;

public class InformationListener {

    private final Map<String, String> informationValues = new HashMap<>();

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        Setting.infoSettings.forEach((key, value) -> {
            if (value.isEnabled()) {
                Setting.Position position = Setting.infoPositions.get(key);

                String v = "loading...";
                if (informationValues.containsKey(key)) {
                    v = informationValues.get(key);
                }

                CustomIngameUI customIngameUI = new CustomIngameUI(0x00000000, 0x00000000, key + ": " + v);
                customIngameUI.drawInfoBox(position.getOffsetX(), position.getOffsetY(), true);
            }
        });
    }
}
