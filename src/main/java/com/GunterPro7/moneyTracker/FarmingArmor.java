package com.GunterPro7.moneyTracker;

import com.GunterPro7.utils.CollectionUtils;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.ItemStack;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

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

    public static FarmingArmor fromPlayer(EntityPlayerSP player) {
        ItemStack[] armorInventory = player.inventory.armorInventory;

        Map<String, AtomicInteger> armorCounts = CollectionUtils.mapOf(
                CollectionUtils.listOf("melon", "cropie", "squash", "fermento"),
                CollectionUtils.listOf(() -> new AtomicInteger(0), 4));

        for (ItemStack itemStack : armorInventory) {
            String name = itemStack.getDisplayName().toLowerCase();
            armorCounts.forEach((armorName, times) -> {
                if (name.contains(armorName)) { // TODO check if contains lore "Required Farming Skill .."
                    times.addAndGet(1);
                }
            });
        }

        for (Map.Entry<String, AtomicInteger> entry : armorCounts.entrySet()) {
            int v = entry.getValue().intValue();
            if (v == 4) {
                return valueOf(entry.getKey().toUpperCase() + "44");
            } else if (v == 3) {
                return valueOf(entry.getKey().toUpperCase() + "34");
            }
        }

        return null;
    }

    public double getFarmingChance() {
        return farmingChance;
    }

    public double getFarmingReward() {
        return farmingReward;
    }
}
