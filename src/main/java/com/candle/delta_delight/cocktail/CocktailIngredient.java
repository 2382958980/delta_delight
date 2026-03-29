package com.candle.delta_delight.cocktail;

import com.candle.delta_delight.registry.ModItems;
import com.candle.delta_delight.util.ModItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import vectorwing.farmersdelight.common.registry.ModEffects;

import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Supplier;

public enum CocktailIngredient {
    COLA("cola", stack -> stack.is(ModItems.COLA.get()), () -> MobEffects.MOVEMENT_SPEED),
    LEMONTEA("lemontea", stack -> stack.is(ModItems.LEMONTEA.get()), () -> MobEffects.REGENERATION),
    APPLE_CIDER("apple_cider", stack -> stack.is(ModItemTags.APPLE_CIDER_INGREDIENTS), () -> MobEffects.ABSORPTION),
    WATERMELON_JUICE("watermelon_juice", stack -> stack.is(ModItemTags.WATERMELON_JUICE_INGREDIENTS), () -> MobEffects.HEAL),
    HONEY_BOTTLE("honey_bottle", stack -> stack.is(Items.HONEY_BOTTLE), ModEffects.NOURISHMENT::get),
    MILK_BOTTLE("milk_bottle", stack -> stack.is(ModItemTags.MILK_BOTTLE_INGREDIENTS), ModEffects.COMFORT::get),
    DRAGON_BREATH("dragon_breath", stack -> stack.is(Items.DRAGON_BREATH), () -> MobEffects.INVISIBILITY),
    GLOW_BERRIES("glow_berries", stack -> stack.is(Items.GLOW_BERRIES), () -> MobEffects.LUCK),
    GOLDEN_CARROT("golden_carrot", stack -> stack.is(Items.GOLDEN_CARROT), () -> MobEffects.JUMP),
    SEA_PICKLE("sea_pickle", stack -> stack.is(Items.SEA_PICKLE), () -> MobEffects.WATER_BREATHING),
    ENDER_PEARL("ender_pearl", stack -> stack.is(Items.ENDER_PEARL), () -> MobEffects.NIGHT_VISION),
    CHORUS_FRUIT("chorus_fruit", stack -> stack.is(Items.CHORUS_FRUIT), () -> MobEffects.SLOW_FALLING),
    GUNPOWDER("gunpowder", stack -> stack.is(Items.GUNPOWDER), () -> MobEffects.DAMAGE_BOOST),
    REDSTONE("redstone", stack -> stack.is(Items.REDSTONE), () -> MobEffects.DIG_SPEED),
    GLOWSTONE_DUST("glowstone_dust", stack -> stack.is(Items.GLOWSTONE_DUST), () -> MobEffects.GLOWING),
    BLAZE_POWDER("blaze_powder", stack -> stack.is(Items.BLAZE_POWDER), () -> MobEffects.FIRE_RESISTANCE);

    private final String key;
    private final ItemMatcher matcher;
    private final Supplier<MobEffect> effectSupplier;

    CocktailIngredient(String key, ItemMatcher matcher, Supplier<MobEffect> effectSupplier) {
        this.key = key;
        this.matcher = matcher;
        this.effectSupplier = effectSupplier;
    }

    public String getKey() {
        return key;
    }

    public MobEffect getEffect() {
        return effectSupplier.get();
    }

    public boolean matches(ItemStack stack) {
        return matcher.matches(stack);
    }

    public static Optional<CocktailIngredient> fromStack(ItemStack stack) {
        return Arrays.stream(values()).filter(ingredient -> ingredient.matches(stack)).findFirst();
    }

    public static Optional<CocktailIngredient> fromKey(String key) {
        String normalized = key.toLowerCase(Locale.ROOT);
        return Arrays.stream(values()).filter(ingredient -> ingredient.key.equals(normalized)).findFirst();
    }

    @FunctionalInterface
    private interface ItemMatcher {
        boolean matches(ItemStack stack);
    }
}
