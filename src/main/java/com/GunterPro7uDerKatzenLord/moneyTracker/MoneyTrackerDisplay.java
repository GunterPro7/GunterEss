package com.GunterPro7uDerKatzenLord.moneyTracker;

import com.GunterPro7uDerKatzenLord.Setting;
import com.GunterPro7uDerKatzenLord.gui.CustomIngameUI;
import com.GunterPro7uDerKatzenLord.utils.Utils;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MoneyTrackerDisplay {

    private final MoneyHandler moneyHandler = MoneyHandler.getInstance();
    private final CustomIngameUI customIngameUI = new CustomIngameUI(0x501E1E1E, 0x501E1E1E, "");

    @SubscribeEvent
    public void onRenderGameOverlayEvent(RenderGameOverlayEvent.Post event) {
        if (event.type == RenderGameOverlayEvent.ElementType.TEXT && Setting.MONEY_OVERLAY.isEnabled()) {
            customIngameUI.lines = new String[]{"Time: " + Utils.toIngameTimeFormat(moneyHandler.getActiveTime()),
                    "Money: " + Utils.toIngameMoneyFormat(moneyHandler.getCurrentMoney()), "Money/h: " +
                    Utils.toIngameMoneyFormat((long) moneyHandler.getMoneyPerHour())};

            customIngameUI.drawInfoBox(Setting.MONEY_OVERLAY.getOffsetX(), Setting.MONEY_OVERLAY.getOffsetY(), true);
        }
    }
}
