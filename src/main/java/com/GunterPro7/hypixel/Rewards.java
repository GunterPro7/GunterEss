package com.GunterPro7.hypixel;

public class Rewards {
    public final int tier;
    public final int amountRequired;
    public final String[] unlocks;

    public Rewards(int tier, int amountRequired, String[] unlocks) {
        this.tier = tier;
        this.amountRequired = amountRequired;
        this.unlocks = unlocks;
    }

    public int getTier() {
        return tier;
    }

    public int getAmountRequired() {
        return amountRequired;
    }

    public String[] getUnlocks() {
        return unlocks;
    }
}
