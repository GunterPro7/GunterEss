package com.GunterPro7.moneyTracker;

import com.GunterPro7.utils.Utils;
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

    @Override
    public String shortName() {
        return Utils.toTitleCase2(name());
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
