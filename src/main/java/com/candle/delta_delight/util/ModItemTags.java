package com.candle.delta_delight.util;

import com.candle.delta_delight.DeltaDelight;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public final class ModItemTags {
    public static final TagKey<Item> APPLE_CIDER_INGREDIENTS = itemTag("apple_cider_ingredients");
    public static final TagKey<Item> WATERMELON_JUICE_INGREDIENTS = itemTag("watermelon_juice_ingredients");
    public static final TagKey<Item> MILK_BOTTLE_INGREDIENTS = ItemTags.create(ResourceLocation.fromNamespaceAndPath("forge", "milk/milk_bottle"));
    public static final TagKey<Item> RED_BERRIES = ItemTags.create(ResourceLocation.fromNamespaceAndPath("forge", "berries/red"));

    private static TagKey<Item> itemTag(String path) {
        return ItemTags.create(ResourceLocation.fromNamespaceAndPath(DeltaDelight.MODID, path));
    }
}
