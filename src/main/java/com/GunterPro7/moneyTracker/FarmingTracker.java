package com.GunterPro7.moneyTracker;

import com.GunterPro7.Setting;
import com.GunterPro7.event.ClientBlockChangeEvent;
import com.GunterPro7.gui.CustomIngameUI;
import com.GunterPro7.listener.AdvancedChat;
import com.GunterPro7.listener.Listener;
import com.GunterPro7.utils.McUtils;
import com.GunterPro7.utils.Utils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.GunterPro7.Main.mc;

public class FarmingTracker implements Listener {
    private static final FarmingTracker INSTANCE = new FarmingTracker();
    private static final Map<Crop, MoneyItem> moneyItems = new HashMap<>();

    static {
        MoneyHandler.getInstance().register(moneyItems);
    }

    private FarmingTracker() {
    }

    public static FarmingTracker getInstance() {
        return INSTANCE;
    }

    public static double calculateProfits(@NotNull Crop crop, int fortune, int extraFortune, boolean bountifulReforge, @Nullable Rarity farmingToolRarity, @Nullable FarmingArmor armor, double multiplier) {
        double basePrice = crop.getNpcPrice();
        double baseDrop = crop.getBaseDrop();

        double bountifulReforgeCount = bountifulReforge ? 0.2 : 0;
        int bountifulRarityFactor = bountifulReforge ? (farmingToolRarity != null ? farmingToolRarity.getBountifulRarityFactor() : 0) : 0;

        double dropAmount = baseDrop * (1 + (((fortune + extraFortune) + bountifulRarityFactor) * 0.01));

        return ((dropAmount * basePrice + (dropAmount * bountifulReforgeCount) + (armor != null ? armor.getFarmingChance() * armor.getFarmingReward() : 0)) / basePrice) * multiplier;
    }

    // TODO calcDrops

    @SubscribeEvent
    public void onBlockBreak(ClientBlockChangeEvent event) {
        Crop crop = Crop.valueOf(event.getMinecraftBlock());
        if (crop == null) return;

        int multiplier = 1;

        if (crop == Crop.SUGAR_CANE) {
            int curOffset = 1;
            while (mc.theWorld.getBlockState(new BlockPos(event.getBlockPos().getX(), event.getBlockPos().getY() + curOffset++, event.getBlockPos().getZ())).getBlock().getLocalizedName().equals("Sugar cane")) {
                multiplier++;
            }
        }

        EntityPlayerSP player = mc.thePlayer;

        String row = McUtils.readRowFromTabList("Farming Fortune: ", true);
        int fortune = Utils.safeToInteger(row);

        String specificRow = McUtils.readRowFromTabList(crop.shortName() + " Fortune: ", true);
        int extraFortune = Utils.safeToInteger(specificRow);

        boolean bountifulReforge = false;
        Rarity farmingToolRarity = Rarity.COMMON;

        NBTTagList lore = McUtils.getItemLore(mc.thePlayer.getHeldItem());
        if (lore != null) {
            for (int i = 0; i < lore.tagCount(); i++) {
                String line = lore.getStringTagAt(i);
                if (line.contains("Bountiful Bonus")) {
                    bountifulReforge = true;
                } if (lore.tagCount() - 1 == i) {
                    farmingToolRarity = Rarity.valueOfContaining(line);
                    if (farmingToolRarity == null) {
                        farmingToolRarity = Rarity.COMMON;
                    }
                }
            }
        }

        FarmingArmor armor = FarmingArmor.fromPlayer(player);

        //AdvancedChat.sendPrivateMessage("fortune: " + fortune + " extraFortune: " + extraFortune + " bountifulReforge" + bountifulReforge + " farmingToolRarity: " + farmingToolRarity + " farmingArmor: " + (armor != null ? armor.name() : "null"));

        double profits = calculateProfits(crop, fortune, extraFortune, bountifulReforge, farmingToolRarity, armor, multiplier);
        if (moneyItems.containsKey(crop)) {
            moneyItems.get(crop).addCount(profits);
        } else {
            moneyItems.put(crop, new MoneyItem(crop, profits));
        } // TODO make Boxes e.g with background and border configureable

    }

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Post event) {
        if (event.type == RenderGameOverlayEvent.ElementType.TEXT) {
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

    public static void resetAll() {
        moneyItems.clear();
    }
}
