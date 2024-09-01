package com.GunterPro7.minigames;

import com.GunterPro7.event.ClientChangeWorldEvent;
import com.GunterPro7.listener.AdvancedChat;
import com.GunterPro7.listener.Listener;
import com.GunterPro7.utils.CollectionUtils;
import com.GunterPro7.utils.CustomHitboxRenderer;
import com.GunterPro7.utils.McUtils;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.List;

import static com.GunterPro7.Main.mc;

public class MurderMystery implements Listener {
    private static final List<Item> murderItems = CollectionUtils.listOf(Items.iron_sword, Items.stone_sword, Items.iron_shovel,
            Items.stick, Items.wooden_axe, Items.wooden_sword, Item.getItemFromBlock(Blocks.deadbush), Items.stone_shovel, Items.blaze_rod,
            Items.diamond_shovel, Items.quartz, Items.pumpkin_pie, Items.golden_pickaxe, Items.apple, Items.name_tag,
            Item.getItemFromBlock(Blocks.sponge), Items.carrot_on_a_stick, Items.bone, Items.carrot, Items.golden_carrot, Items.cookie,
            Items.diamond_axe, Item.getItemFromBlock(Blocks.double_plant), Items.prismarine_shard, Items.cooked_beef, Items.netherbrick,
            Items.cooked_chicken, Items.golden_hoe, Items.golden_sword, Items.diamond_sword,
            Items.diamond_hoe, Items.shears, Items.fish, new ItemStack(Items.dye, 1, 1).getItem(), Items.boat, Items.speckled_melon,
            Items.book, new ItemStack(Blocks.sapling, 1, 3).getItem(),

            Items.record_13, Items.record_cat, Items.record_blocks, Items.record_chirp, Items.record_far,
            Items.record_mall, Items.record_mellohi, Items.record_stal, Items.record_strad, Items.record_ward,
            Items.record_11, Items.record_wait); // TODO in double mode there are 2 murdersw

    private String murderName;
    private boolean gameActive;

    @SubscribeEvent
    public void onWorldChange(ClientChangeWorldEvent event) {
        this.murderName = null;
        this.gameActive = false;
    }

    @SubscribeEvent
    public void onChatMessage(ClientChatReceivedEvent event) { // TODO show gold nuggets on the ground
        if (AdvancedChat.clearChatMessage(event.message.getUnformattedText()).equals("Teaming with the Murderer is not allowed!")) {
            this.gameActive = true;
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (this.gameActive && this.murderName == null) {
            String playerName = mc.thePlayer.getGameProfile().getName();

            mc.theWorld.getEntities(EntityPlayer.class, (e) -> !e.getGameProfile().getName().equals(playerName)).forEach((e) -> {
                if (e != null && e.getGameProfile() != null) {
                    if (e.getHeldItem() != null && murderItems.contains(e.getHeldItem().getItem())) {
                        this.murderName = e.getGameProfile().getName();
                        AdvancedChat.sendPrivateMessage(e.getDisplayName().getFormattedText() + "§7 is the §4Murderer!");
                    }
                }
            });
        }
    }

    @SubscribeEvent
    public void onWorldRenderer(RenderWorldLastEvent event) {
        if (this.gameActive) {
            String playerName = mc.thePlayer.getGameProfile().getName();

            mc.theWorld.getEntities(EntityPlayer.class, (e) -> !e.getGameProfile().getName().equals(playerName)).forEach((e) -> {
                if (e.getName().equals(this.murderName)) {
                    CustomHitboxRenderer.getInstance().renderHitboxAroundPlayer(e, event.partialTicks);
                }
            });

            mc.theWorld.getEntities(EntityItem.class, (e) -> e.getEntityItem().getItem() == Items.gold_ingot).forEach((e) -> {
                CustomHitboxRenderer.getInstance().renderHitboxAroundItem(e, event.partialTicks);
            });
        }
    }
}
