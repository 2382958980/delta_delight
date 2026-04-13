package com.candle.delta_delight.util;

import com.candle.delta_delight.DeltaDelight;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public final class ModItemTags {
    public static final TagKey<Item> COKE_INGREDIENTS = cocktailIngredient("coke");
    public static final TagKey<Item> LEMON_TEA_INGREDIENTS = cocktailIngredient("lemon_tea");
    public static final TagKey<Item> APPLE_CIDER_INGREDIENTS = cocktailIngredient("apple_cider");
    public static final TagKey<Item> WATERMELON_JUICE_INGREDIENTS = cocktailIngredient("watermelon_juice");
    public static final TagKey<Item> HONEY_BOTTLE_INGREDIENTS = cocktailIngredient("honey_bottle");
    public static final TagKey<Item> MILK_BOTTLE_INGREDIENTS = cocktailIngredient("milk_bottle");
    public static final TagKey<Item> DRAGON_BREATH_INGREDIENTS = cocktailIngredient("dragon_breath");
    public static final TagKey<Item> GLOW_BERRIES_INGREDIENTS = cocktailIngredient("glow_berries");
    public static final TagKey<Item> GOLDEN_CARROT_INGREDIENTS = cocktailIngredient("golden_carrot");
    public static final TagKey<Item> SEA_PICKLE_INGREDIENTS = cocktailIngredient("sea_pickle");
    public static final TagKey<Item> ENDER_PEARL_INGREDIENTS = cocktailIngredient("ender_pearl");
    public static final TagKey<Item> CHORUS_FRUIT_INGREDIENTS = cocktailIngredient("chorus_fruit");
    public static final TagKey<Item> GUNPOWDER_INGREDIENTS = cocktailIngredient("gunpowder");
    public static final TagKey<Item> REDSTONE_INGREDIENTS = cocktailIngredient("redstone");
    public static final TagKey<Item> GLOWSTONE_DUST_INGREDIENTS = cocktailIngredient("glowstone_dust");
    public static final TagKey<Item> BLAZE_POWDER_INGREDIENTS = cocktailIngredient("blaze_powder");

    private static TagKey<Item> itemTag(String path) {
        return ItemTags.create(new ResourceLocation(DeltaDelight.MODID, path));
    }

    private static TagKey<Item> cocktailIngredient(String name) {
        return itemTag("cocktail/ingredient/" + name);
    }
}
