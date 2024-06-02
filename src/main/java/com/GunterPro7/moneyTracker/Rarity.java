package com.GunterPro7.moneyTracker;

public enum Rarity {
    COMMON(1),
    UNCOMMON(2),
    RARE(3),
    EPIC(5),
    LEGENDARY(7),
    MYTHIC(10),
    ;

    private final int bountifulRarityFactor;

    Rarity(int bountifulRarityFactor) {
        this.bountifulRarityFactor = bountifulRarityFactor;
    }

    public static Rarity valueOfContaining(String line) {
        line = line.toUpperCase();
        for (Rarity rarity : Rarity.values()) {
            if (line.contains(rarity.name())) {
                return rarity;
            }
        }

        return null;
    }

    public int getBountifulRarityFactor() {
        return bountifulRarityFactor;
    }
}
