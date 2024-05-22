package com.GunterPro7.moneyTracker;

import com.GunterPro7.Setting;
import com.GunterPro7.event.ClientBlockChangeEvent;
import com.GunterPro7.gui.CustomIngameUI;
import com.GunterPro7.listener.Listener;
import com.GunterPro7.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FarmingTracker implements Listener {
    private static final FarmingTracker INSTANCE = new FarmingTracker();
    private static final Map<Crop, MoneyItem> moneyItems = new HashMap<>();

    static {
        MoneyHandler.getInstance().register(moneyItems);
    }

    private FarmingTracker() {}

    public static FarmingTracker getInstance() {
        return INSTANCE;
    }

    public static double calculateProfits(@NotNull Crop crop, int fortune, int extraFortune, boolean bountifulReforge, @Nullable Rarity farmingToolRarity, @Nullable FarmingArmor armor) {
        double basePrice = crop.getNpcPrice();
        double baseDrop = crop.getBaseDrop();

        double bountifulReforgeCount = bountifulReforge ? 0.2 : 0;
        int bountifulRarityFactor = bountifulReforge ? (farmingToolRarity != null ? farmingToolRarity.getBountifulRarityFactor() : 0) : 0;

        double dropAmount = baseDrop * (1 + (((fortune + extraFortune) + bountifulRarityFactor) * 0.01));

        return dropAmount * basePrice + (dropAmount * bountifulReforgeCount) + (armor != null ? armor.getFarmingChance() * armor.getFarmingReward() : 0);
    }

    @SubscribeEvent
    public void onBlockBreak(ClientBlockChangeEvent event) {
        Crop crop = Crop.valueOf(event.getMinecraftBlock());

        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;

        int fortune = 0;
        int extraFortune = 0; // TODO not ready
        boolean bountifulReforge = true;
        Rarity farmingToolRarity = Rarity.MYTHIC;
        FarmingArmor armor = FarmingArmor.fromPlayer(player);

        if (crop != null) {
            double profits = calculateProfits(crop, fortune, extraFortune, bountifulReforge, farmingToolRarity, armor);
            if (moneyItems.containsKey(crop)) {
                moneyItems.get(crop).addCount(profits);
            } else {
                moneyItems.put(crop, new MoneyItem(crop, profits));
            }

        }
    }

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Post event) {
        if (Setting.FARMING_OVERLAY.isEnabled()) {
            List<String> lines = new ArrayList<>();
            for (MoneyItem moneyItem : moneyItems.values()) {
                Crop crop = (Crop) moneyItem.getItem();
                lines.add(Utils.convertToColorString(crop.shortName() + ": " + Utils.toIngameNumberFormat((long) (crop.getNpcPrice() * moneyItem.getCount()))));
            }

            CustomIngameUI ingameUI = new CustomIngameUI(0x00000000, 0xFF000000, lines);
            ingameUI.drawInfoBox(Setting.FARMING_OVERLAY, false);
        }
    }
}
