package com.GunterPro7.moneyTracker;

import com.GunterPro7.utils.MinecraftBlock;

public enum Crop implements SkyblockItem {
    SUGAR_CANE(4, 2),
    NETHER_WART(4, 3),
    WHEAT(6, 1),
    MUSHROOM(10, 1),
    COCOA_BEAN(3, 3),
    PUMPKIN(10, 1),
    MELON(2, 5),
    POTATO(3, 3.5),
    CARROT(3, 3.5);

    private final double basePrice;
    private final double baseDrop;

    Crop(double basePrice, double baseDrop) {
        this.basePrice = basePrice;
        this.baseDrop = baseDrop;
    }

    public static Crop valueOf(MinecraftBlock minecraftBlock) {
        switch (minecraftBlock.getId()) {
            case 296:
                return WHEAT;
            case 391:
                return CARROT;
            case 351:
                return COCOA_BEAN;
            case 392:
                return POTATO;
            case 338:
                return SUGAR_CANE;
            case 372:
                return NETHER_WART;
            case 360:
                return MELON;
            case 86:
                return PUMPKIN;
            case 39:
            case 40:
                return MUSHROOM;
            default:
                return null;
        }
    }

    public double getBaseDrop() {
        return baseDrop;
    }

    @Override
    public String shortName() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }

    @Override
    public double getNpcPrice() {
        return basePrice;
    }
}
