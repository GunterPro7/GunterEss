package com.GunterPro7.utils;

import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.*;

public class MinecraftBlock {
    private static final Map<Integer, Integer> RE_MAP = CollectionUtils.mapOf(
            59, 296, // Wheat
            141, 391,        // Carrot
            127, 351,        // Cocoa
            142, 392,        // Potato
            83, 338,         // Sugar Cane
            115, 372         // Nether wart
    );

    private static final Item DEFAULT_ITEM = Items.name_tag;
    private final int id;
    private final int dataValue;
    private String name;
    private final ItemStack itemStack;

    public MinecraftBlock(Block block, int dataValue) {
        this.id = Block.getIdFromBlock(block);
        this.dataValue = dataValue;

        Item item = Item.getItemFromBlock(block);
        if (item == null) {
            if (RE_MAP.containsKey(id)) {
                item = Item.getItemById(RE_MAP.get(id));
            } else {
                item = DEFAULT_ITEM;
            }
        }

        this.itemStack = new ItemStack(item, 1, dataValue);
        this.name = this.itemStack.getDisplayName();
    }

    public int getId() {
        return id;
    }

    public int getDataValue() {
        return dataValue;
    }

    public String getName() {
        return name;
    }

    public String getBlockId() {
        return id + ":" + dataValue;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MinecraftBlock that = (MinecraftBlock) o;
        return id == that.id && dataValue == that.dataValue;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dataValue);
    }

    @Override
    public String toString() {
        return "id: " + id + ", dataValue: " + dataValue + ", name: " + name;
    }
}