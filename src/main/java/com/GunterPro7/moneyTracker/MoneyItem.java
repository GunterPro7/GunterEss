package com.GunterPro7.moneyTracker;

public class MoneyItem {
    private double count;
    private final SkyblockItem skyblockItem;

    public MoneyItem(SkyblockItem skyblockItem, double count) {
        this.skyblockItem = skyblockItem;
        this.count = count;
        if (count != 0) {
            execEvent(count);
        }
    }

    private void execEvent(double c) {
        MoneyHandler.getInstance().update(this, c * getNpcPrice());
    }

    public void setCount(double count) {
        double oldCount = this.count;
        this.count = count;
        execEvent(count - oldCount);
    }

    public void addCount(double count) {
        this.count += count;
        execEvent(count);
    }

    public double getCount() {
        return count;
    }

    public double getNpcPrice() {
        return skyblockItem.getNpcPrice();
    }

    public SkyblockItem getItem() {
        return skyblockItem;
    }
}
