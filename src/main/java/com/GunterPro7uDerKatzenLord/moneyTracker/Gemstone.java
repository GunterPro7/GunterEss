package com.GunterPro7uDerKatzenLord.moneyTracker;

import com.GunterPro7uDerKatzenLord.hypixel.Collections;
import net.minecraft.util.ResourceLocation;

public enum Gemstone implements SkyblockItem {
    RUBY("❤", 'c'),
    JADE("☘", 'a'),
    SAPPHIRE("✎", 'b'),
    AMETHYST("❈", '5'),
    AMBER("⸕", '6'),
    TOPAZ("✧", 'e'),
    JASPER("❁", 'd'),

    ;

    private final String sign;
    private final char colorIndex;

    Gemstone(String sign, char colorIndex) {
        this.sign = sign;
        this.colorIndex = colorIndex;
    }

    public static Gemstone valueOfShort(String shortName) {
        for (Gemstone gemstone : values()) {
            if (gemstone.shortName().equals(shortName)) {
                return gemstone;
            }
        }
        return null;
    }

    public String shortName() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }

    public String getSign() {
        return this.sign;
    }

    // Flawed, Flawless, Fine, ...
    public ResourceLocation getLocationBy(String level) {
        return null;
    }

    public String getColorString() {
        return "§" + colorIndex;
    }

    @Override
    public double getNpcPrice() {
        return 3 * 80;
    }
}
