package com.GunterPro7uDerKatzenLord.Utils;

public class CollectionDropsMeta {
    private long amountCollected;
    private int minutesPast;
    private long lastAmountOfItems;
    private long sessionAmount;
    private long brokenBlocks;
    private long amountAtStart;
    private long totalBrokenBlocks;

    public CollectionDropsMeta(long amountCollected, int minutesPast, long lastAmountOfItems) {
        this.amountCollected = amountCollected;
        this.minutesPast = minutesPast;
        this.lastAmountOfItems = lastAmountOfItems;
        amountAtStart = amountCollected;
    }

    public CollectionDropsMeta() {

    }

    public long getAmountCollected() {
        return amountCollected;
    }

    public void setAmountCollected(long amountCollected) {
        this.amountCollected = amountCollected;
    }

    public int getMinutesPast() {
        return minutesPast;
    }

    public void setMinutesPast(int minutesPast) {
        this.minutesPast = minutesPast;
    }

    public long getLastAmountOfItems() {
        return lastAmountOfItems;
    }

    public void setLastAmountOfItems(long lastAmountOfItems) {
        this.lastAmountOfItems = lastAmountOfItems;
    }

    public void addItems(long newAmountOfItems) {
        if (lastAmountOfItems != newAmountOfItems) {
            sessionAmount += newAmountOfItems - lastAmountOfItems;
            amountCollected = newAmountOfItems;
            lastAmountOfItems = amountCollected;
            minutesPast += 3;
            brokenBlocks = 0;
        }
    }

    public void blockBroken() {
        brokenBlocks++;
        totalBrokenBlocks++;
    }

    public long getBrokenBlocks() {
        return brokenBlocks;
    }

    public double getEstimatedCollection() {
        return lastAmountOfItems + brokenBlocks * getAverageCollectionPerBlock();
    }

    public double getAverageCollectionPerBlock() {
        return ((double) lastAmountOfItems - amountAtStart) / totalBrokenBlocks;
    }

    public long getItemsPerHour() {
        if (minutesPast == 0) {
            return 0;
        }
        return sessionAmount / minutesPast * 60;
    }

    public void init(long crops) {
        amountCollected = crops;
        lastAmountOfItems = crops;
    }
}
