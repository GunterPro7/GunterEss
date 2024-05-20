package com.GunterPro7uDerKatzenLord.moneyTracker;

import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MoneyHandler {
    private static final MoneyHandler INSTANCE = new MoneyHandler();
    /* Milliseconds */
    private static final long TIME_BETWEEN_ACTIONS = 1000 * 30;

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
        activeTime += Math.min(System.currentTimeMillis() - lastUpdate, TIME_BETWEEN_ACTIONS);

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
        return activeTime + (started ? Math.min(System.currentTimeMillis() - lastUpdate, TIME_BETWEEN_ACTIONS) : 0);
    }

    public long getCurrentMoney() {
        return (long) currentMoney;
    }

    public double getMoneyPerHour() {
        return currentMoney / ((double) getActiveTime() / (1000 * 60 * 60));
    }
}