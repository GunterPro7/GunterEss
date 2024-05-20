package com.GunterPro7uDerKatzenLord.moneyTracker;

import com.GunterPro7uDerKatzenLord.Setting;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MoneyHandler {
    private static final MoneyHandler INSTANCE = new MoneyHandler();
    /* Milliseconds */

    private final List<MoneyItem> moneyItems = new ArrayList<>();
    private double currentMoney;
    /* Milliseconds */
    private long activeTime;
    private boolean started;

    private long lastUpdate;

    private MoneyHandler() {}

    public static MoneyHandler getInstance() {
        return INSTANCE;
    }

    public void update(MoneyItem moneyItem, int c) {
        currentMoney += c;
        if (started) {
            activeTime += Math.min(System.currentTimeMillis() - lastUpdate, Setting.MONEY_PAUSE_AFTER.getValue() * 1000L);
        }

        started = true;
        lastUpdate = System.currentTimeMillis();
    }

    public void register(Map<? extends SkyblockItem, MoneyItem> map) {
        moneyItems.addAll(map.values());
    }

    public void register(List<MoneyItem> list) {
        moneyItems.addAll(list);
    }

    public void register(MoneyItem moneyItem) {
        moneyItems.add(moneyItem);
    }

    public List<MoneyItem> getMoneyItems() {
        return moneyItems;
    }

    public long getActiveTime() {
        return activeTime + (started ? Math.min(System.currentTimeMillis() - lastUpdate, Setting.MONEY_PAUSE_AFTER.getValue() * 1000L) : 0);
    }

    public long getCurrentMoney() {
        return (long) currentMoney;
    }

    public double getMoneyPerHour() {
        return currentMoney / ((double) getActiveTime() / (1000 * 60 * 60));
    }
}
