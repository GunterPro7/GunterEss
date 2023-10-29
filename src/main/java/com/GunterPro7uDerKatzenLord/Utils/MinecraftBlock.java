package com.GunterPro7uDerKatzenLord.Utils;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MinecraftBlock {
    private static Map<Integer, Integer> RE_MAP = new HashMap<>(); // TODO implement that carrots got like the planted form, it automatically gets the other form o-o
    private final int id;
    private final int dataValue;
    private String name;
    private final ItemStack itemStack;

    public MinecraftBlock(int id, int dataValue) {
        this.id = id;
        this.dataValue = dataValue;
        ItemStack itemStack;
        try {
            itemStack = new ItemStack(Item.getItemById(id), 1, dataValue);
            this.name = itemStack.getDisplayName();
        } catch (Exception e) {
            itemStack = null;
        }
        this.itemStack = itemStack;
    }

    public MinecraftBlock(int id) {
        this(id, 0);
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
}