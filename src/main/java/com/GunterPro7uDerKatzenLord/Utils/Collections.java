package com.GunterPro7uDerKatzenLord.Utils;

import com.GunterPro7uDerKatzenLord.Main;

import java.util.Objects;

public enum Collections {
    cocao_beans("INK_SACK:3", "Cocao Beans", 351, 3),
    water_lily("WATER_LILY", "Water Lily", 111),
    lapis_lazuli("INK_SACK:4", "Lapis Lazuli", 351, 4),
    rabbit_foot("RABBIT_FOOT", "Rabbit Foot", 414),
    carrot("CARROT_ITEM", "Carrot",  391),
    redstone("REDSTONE", "Redstone", 391),
    acacia_wood("LOG_2", "Acacia Wood", 162),
    enchanted_obsidian("ENCHANTED_OBSIDIAN", "Enchanted Obsidian"),
    enchanted_potato("ENCHANTED_POTATO", "Enchanted Potato"),
    spruce_wood("LOG:1", "Spruce Wood", 17, 1),
    coal("COAL", "Coal", 263),
    enchanted_quartz("ENCHANTED_QUARTZ", "Enchanted Quartz"),
    ender_pearl("ENDER_PEARL", "Ender Pearl", 368),
    jungle_wood("LOG:3", "Jungle Wood", 17, 3),
    birch_wood("LOG:2", "Birch Wood", 17, 2),
    gold_block("GOLD_BLOCK", "Gold Block", 41),
    enchanted_string("ENCHANTED_STRING", "Enchanted String"),
    redstone_block("REDSTONE_BLOCK", "Redstone Block", 152),
    slime_ball("SLIME_BALL", "Slime Ball", 341),
    mycelium("MYCEL", "Mycelium", 110),
    end_stone("ENDER_STONE", "End Stone", 121),
    snow_ball("SNOW_BALL", "Snow Ball", 332),
    nether_quartz("QUARTZ", "Nether Quartz", 406),
    raw_beef("RAW_BEEF", "Raw Beef", 363),
    sand("SAND", "Sand", 12),
    oak_wood("LOG", "Oak Wood", 17),
    raw_chicken("RAW_CHICKEN", "Raw Chicken", 365),
    cactus("CACTUS", "Cactus", 81),
    sugar_cane("SUGAR_CANE", "Sugar Cane", 338),
    magma_cream("MAGMA_CREAM", "Magma Cream", 378),
    ghast_tear("GHAST_TEAR", "Ghast Tear", 370),
    pumpkin("PUMPKIN", "Pumpkin", 86),
    enchanted_poisonous_potato("ENCHANTED_POISONOUS_POTATO", "Enchanted Poisonous Potato"),
    wheat("WHEAT", "Wheat", 296),
    seeds("SEEDS", "Seeds", 295),
    enchanted_gold_block("ENCHANTED_GOLD_BLOCK", "Enchanted Gold Block"),
    rabbit_hide("RABBIT_HIDE", "Rabbit Hide", 411),
    iron_ingot("IRON_INGOT", "Iron Ingot", 265),
    gemstone("GEMSTONE_COLLECTION", "Gemstone", 160, -2),
    ink_sack("INK_SACK", "Ink Sack", 351),
    prismarine_shard("PRISMARINE_SHARD", "Prismarine Shard", 409),
    mushroom("MUSHROOM_COLLECTION", "Mushroom"),
    enchanted_rotten_flesh("ENCHANTED_ROTTEN_FLESH", "Enchanted Rotten Flesh"),
    raw_rabbit("RABBIT", "Raw Rabbit"),
    gunpowder("SULPHUR", "Gunpowder"),
    nether_wart("NETHER_STALK", "Nether Wart"),
    enchanted_spider_eye("ENCHANTED_SPIDER_EYE", "Enchanted Spider Eye"),
    raw_mutton("MUTTON", "Raw Mutton"),
    enchanted_quartz_block("ENCHANTED_QUARTZ_BLOCK", "Enchanted Quartz Block"),
    enchanted_carrot("ENCHANTED_CARROT", "Enchanted Crarot"),
    rotten_flesh("ROTTEN_FLESH", "Rotten Flesh"),
    enchanted_clay_ball("ENCHANTED_CLAY_BALL", "Enchanted Clay Ball"),
    obsidian("OBSIDIAN", "Obsidian"),
    diamond("DIAMOND", "Diamond"),
    cobblestone("COBBLESTONE", "Cobblestone"),
    spider_eye("SPIDER_EYE", "Spider Eye"),
    raw_fish("RAW_FISH", "Raw Fish"),
    glowstone_dust("GLOWSTONE_DUST", "Glowstone Dust"),
    gold_ingot("GOLD_INGOT", "Gold Ingot"),
    gravel("GRAVEL", "Gravel"),
    melon("MELON", "Melon"),
    enchanted_packed_ice("ENCHANTED_PACKED_ICE", "Enchanted Packed Ice"),
    potato("POTATO_ITEM", "Potato"),
    pufferfish("RAW_FISH:3", "Pufferfish"),
    hard_stone("HARD_STONE", "Hard Stone", 1),
    iron_block("IRON_BLOCK", "Iron Block", 42),
    mithril("MITHRIL_ORE", "Mithril Ore", 410), // TODO same affact as on Prismarine crystal
    leather("LEATHER", "Leather", 334),
    enchanted_cobblestone("ENCHANTED_COBBLESTONE", "Enchanted Cobblestone"),
    bone("BONE", "Bone", 352),
    clownfish("RAW_FISH:2", "Clownfish", 349, 2),
    poisonous_potato("POISONOUS_POTATO", "Poisonius Potato", 394),
    raw_porkchop("PORK", "Raw Porkchop", 319),
    raw_salmon("RAW_FISH:1", "Raw Salmon", 349, 1),
    emerald("EMERALD", "Emerald", 388),
    red_sand("SAND:1", "Red Sand", 12, 1),
    magmafish("MAGMA_FISH", "Magmafish", 397, 3), // TODO das ist das selbe wie Player Head
    egg("EGG", "Egg", 383),
    prismarine_crystal("PRISMARINE_CRYSTALS", "Prismarine Crystal", 410),
    enchanted_ice("ENCHANTED_ICE", "Enchanted Ice"),
    ice("ICE", "Ice", 79),
    clay_ball("CLAY_BALL", "Clay Ball", 337),
    feather("FEATHER", "Feather", 288),
    sulphur("SULPHUR_ORE", "Sulphur", 348), // TODO das ist das selbe wie glostone dust
    netherrack("NETHERRACK", "Netherrack", 87),
    dark_oak_wood("LOG_2:1", "Dark Oak Wood", 162, 1),
    sponge("SPONGE", "Sponge", 19),
    enchanted_snow_block("ENCHANTED_SNOW_BLOCK", "Enchanted Snow Block"),
    blaze_rod("BLAZE_ROD", "Blaze Rod", 369),
    string("STRING", "String", 287),
    enchanted_diamond("ENCHANTED_DIAMOND", "Enchanted Diamond"),
    enchanted_gold("ENCHANTED_GOLD", "Enchanted Gold");

    private String apiName;
    private String simpleName;
    private JsonCollection collection;
    private int level;
    private CollectionDropsMeta collectionDropsMeta = new CollectionDropsMeta();
    private long crops;
    private final int minecraftId;
    private final int dataValue;

    Collections(String apiName, String simpleName, int minecraftId, int dataValue) {
        this.apiName = apiName;
        this.simpleName = simpleName;
        //collectionDropsMeta = new CollectionDropsMeta();
        this.minecraftId = minecraftId;
        this.dataValue = dataValue;
    }

    Collections(String apiName, String simpleName, int minecraftId) {
        this(apiName, simpleName, minecraftId, 0);
    }

    Collections(String apiName, String simpleName) {
        this(apiName, simpleName, -1);
    }

    public Collections getCollectionById(int minecraftId, int dataValue) {
        for (Collections collection : values()) {
            if (collection.minecraftId == minecraftId && collection.dataValue == dataValue) {
                return collection;
            }
        }
        return null;
    }

    public static boolean includesAPI(String key) {
        for (Collections collection : values()) {
            if (Objects.equals(collection.apiName, key)) {
                return true;
            }
        }
        return false;
    }

    public static Collections valueOfAPI(String key) {
        for (Collections collection : values()) {
            if (Objects.equals(collection.apiName, key)) {
                return collection;
            }
        }
        return null;
    }

    public String getApiName() {
        return apiName;
    }

    public String getSimpleName() {
        return simpleName;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    static boolean includes(String collectionString) {
        try {
            valueOf(collectionString);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    public void setCollection(JsonCollection collection) {
        this.collection = collection;
    }

    public JsonCollection getCollection() {
        return collection;
    }

    public long getCollectionDropsPerHour() {
        return collectionDropsMeta.getItemsPerHour();
    }

    public void addCollection(long collectionDrops) {
        collectionDropsMeta.addItems(collectionDrops);
    }

    public long getCrops() {
        return crops;
    }

    public void setCrops(long crops) {
        this.crops = crops;

        if (Main.starting) {
            collectionDropsMeta.init(crops);
        } else {
            collectionDropsMeta.addItems(crops);
        }
    }
}
