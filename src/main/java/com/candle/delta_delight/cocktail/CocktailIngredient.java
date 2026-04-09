package com.candle.delta_delight.cocktail;

import com.candle.delta_delight.util.ModItemTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.IExtensibleEnum;
import vectorwing.farmersdelight.common.registry.ModEffects;

import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Supplier;

public enum CocktailIngredient implements IExtensibleEnum {
    COKE("coke", stack -> stack.is(ModItemTags.COKE_INGREDIENTS), () -> MobEffects.MOVEMENT_SPEED),
    LEMON_TEA("lemon_tea", stack -> stack.is(ModItemTags.LEMON_TEA_INGREDIENTS), () -> MobEffects.REGENERATION),
    APPLE_CIDER("apple_cider", stack -> stack.is(ModItemTags.APPLE_CIDER_INGREDIENTS), () -> MobEffects.ABSORPTION),
    WATERMELON_JUICE("watermelon_juice", stack -> stack.is(ModItemTags.WATERMELON_JUICE_INGREDIENTS), () -> MobEffects.HEAL),
    HONEY_BOTTLE("honey_bottle", stack -> stack.is(ModItemTags.HONEY_BOTTLE_INGREDIENTS), ModEffects.NOURISHMENT),
    MILK_BOTTLE("milk_bottle", stack -> stack.is(ModItemTags.MILK_BOTTLE_INGREDIENTS), ModEffects.COMFORT),
    DRAGON_BREATH("dragon_breath", stack -> stack.is(ModItemTags.DRAGON_BREATH_INGREDIENTS), () -> MobEffects.INVISIBILITY),
    GLOW_BERRIES("glow_berries", stack -> stack.is(ModItemTags.GLOW_BERRIES_INGREDIENTS), () -> MobEffects.LUCK),
    GOLDEN_CARROT("golden_carrot", stack -> stack.is(ModItemTags.GOLDEN_CARROT_INGREDIENTS), () -> MobEffects.JUMP),
    SEA_PICKLE("sea_pickle", stack -> stack.is(ModItemTags.SEA_PICKLE_INGREDIENTS), () -> MobEffects.WATER_BREATHING),
    ENDER_PEARL("ender_pearl", stack -> stack.is(ModItemTags.ENDER_PEARL_INGREDIENTS), () -> MobEffects.NIGHT_VISION),
    CHORUS_FRUIT("chorus_fruit", stack -> stack.is(ModItemTags.CHORUS_FRUIT_INGREDIENTS), () -> MobEffects.SLOW_FALLING),
    GUNPOWDER("gunpowder", stack -> stack.is(ModItemTags.GUNPOWDER_INGREDIENTS), () -> MobEffects.DAMAGE_BOOST),
    REDSTONE("redstone", stack -> stack.is(ModItemTags.REDSTONE_INGREDIENTS), () -> MobEffects.DIG_SPEED),
    GLOWSTONE_DUST("glowstone_dust", stack -> stack.is(ModItemTags.GLOWSTONE_DUST_INGREDIENTS), () -> MobEffects.GLOWING),
    BLAZE_POWDER("blaze_powder", stack -> stack.is(ModItemTags.BLAZE_POWDER_INGREDIENTS), () -> MobEffects.FIRE_RESISTANCE);

    private final String key;
    private final ItemMatcher matcher;
    private final Supplier<MobEffect> effectSupplier;

    CocktailIngredient(String key, ItemMatcher matcher, Supplier<MobEffect> effectSupplier) {
        this.key = key;
        this.matcher = matcher;
        this.effectSupplier = effectSupplier;
    }

    @SuppressWarnings("unused")
    public static CocktailIngredient create(String key, ItemMatcher matcher, Supplier<MobEffect> effectSupplier) {
        throw new IllegalStateException("Enum constants must be defined at compile time");
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

    @SuppressWarnings("unused")
    public static Optional<CocktailIngredient> fromKey(String key) {
        String normalized = key.toLowerCase(Locale.ROOT);
        return Arrays.stream(values()).filter(ingredient -> ingredient.key.equals(normalized)).findFirst();
    }

    @FunctionalInterface
    public interface ItemMatcher {
        boolean matches(ItemStack stack);
    }
}
