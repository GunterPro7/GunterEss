package com.GunterPro7.moneyTracker;

public enum FarmingArmor {
    MELON34(0.0004, 25000),
    MELON44(0.0005, 25000),
    CROPIE34(0.0002, 75000),
    CRPOIE44(0.0003, 75000),
    SQUASH34(0.00006, 250000),
    SQUASH44(0.00007, 250000),
    FERMENTO34(1, 40),
    FERMENTO44(1, 52.5),
    NONE(0, 0),
    ;

    private final double farmingChance;
    private final double farmingReward;

    FarmingArmor(double farmingChance, double farmingReward) {
        this.farmingChance = farmingChance;
        this.farmingReward = farmingReward;
    }

    public double getFarmingChance() {
        return farmingChance;
    }

    public double getFarmingReward() {
        return farmingReward;
    }
}
