package com.GunterPro7.moneyTracker;

public class MoneyItem {
    private int count;
    private final SkyblockItem skyblockItem;

    public MoneyItem(SkyblockItem skyblockItem, int count) {
        this.skyblockItem = skyblockItem;
        this.count = count;
        if (count != 0) {
            execEvent(count);
        }
    }

    private void execEvent(int c) {
        MoneyHandler.getInstance().update(this, (int) (c * getNpcPrice()));
    }

    public void setCount(int count) {
        int oldCount = this.count;
        this.count = count;
        execEvent(count - oldCount);
    }

    public void addCount(int count) {
        this.count += count;
        execEvent(count);
    }

    public int getCount() {
        return count;
    }

    public double getNpcPrice() {
        return skyblockItem.getNpcPrice();
    }
}
