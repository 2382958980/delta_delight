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
    COKE(stack -> stack.is(ModItemTags.COKE_INGREDIENTS), () -> MobEffects.MOVEMENT_SPEED),
    LEMON_TEA(stack -> stack.is(ModItemTags.LEMON_TEA_INGREDIENTS), () -> MobEffects.REGENERATION),
    APPLE_CIDER(stack -> stack.is(ModItemTags.APPLE_CIDER_INGREDIENTS), () -> MobEffects.ABSORPTION),
    WATERMELON_JUICE(stack -> stack.is(ModItemTags.WATERMELON_JUICE_INGREDIENTS), () -> MobEffects.HEAL),
    HONEY_BOTTLE(stack -> stack.is(ModItemTags.HONEY_BOTTLE_INGREDIENTS), ModEffects.NOURISHMENT),
    MILK_BOTTLE(stack -> stack.is(ModItemTags.MILK_BOTTLE_INGREDIENTS), ModEffects.COMFORT),
    DRAGON_BREATH(stack -> stack.is(ModItemTags.DRAGON_BREATH_INGREDIENTS), () -> MobEffects.INVISIBILITY),
    GLOW_BERRIES(stack -> stack.is(ModItemTags.GLOW_BERRIES_INGREDIENTS), () -> MobEffects.LUCK),
    GOLDEN_CARROT(stack -> stack.is(ModItemTags.GOLDEN_CARROT_INGREDIENTS), () -> MobEffects.JUMP),
    SEA_PICKLE(stack -> stack.is(ModItemTags.SEA_PICKLE_INGREDIENTS), () -> MobEffects.WATER_BREATHING),
    ENDER_PEARL(stack -> stack.is(ModItemTags.ENDER_PEARL_INGREDIENTS), () -> MobEffects.NIGHT_VISION),
    CHORUS_FRUIT(stack -> stack.is(ModItemTags.CHORUS_FRUIT_INGREDIENTS), () -> MobEffects.SLOW_FALLING),
    GUNPOWDER(stack -> stack.is(ModItemTags.GUNPOWDER_INGREDIENTS), () -> MobEffects.DAMAGE_BOOST),
    REDSTONE(stack -> stack.is(ModItemTags.REDSTONE_INGREDIENTS), () -> MobEffects.DIG_SPEED),
    GLOWSTONE_DUST(stack -> stack.is(ModItemTags.GLOWSTONE_DUST_INGREDIENTS), () -> MobEffects.GLOWING),
    BLAZE_POWDER(stack -> stack.is(ModItemTags.BLAZE_POWDER_INGREDIENTS), () -> MobEffects.FIRE_RESISTANCE);

    private final ItemMatcher matcher;
    private final Supplier<MobEffect> effectSupplier;

    CocktailIngredient(ItemMatcher matcher, Supplier<MobEffect> effectSupplier) {
        this.matcher = matcher;
        this.effectSupplier = effectSupplier;
    }

    @SuppressWarnings("unused")
    public static CocktailIngredient create(String name, ItemMatcher matcher, Supplier<MobEffect> effectSupplier) {
        throw new IllegalStateException("Enum constants must be defined at compile time");
    }

    public String getKey() {
        return normalizeKey(name());
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
        String normalized = normalizeKey(key);
        return Arrays.stream(values()).filter(ingredient -> ingredient.getKey().equals(normalized)).findFirst();
    }

    private static String normalizeKey(String value) {
        return value.toLowerCase(Locale.ROOT);
    }

    @FunctionalInterface
    public interface ItemMatcher {
        boolean matches(ItemStack stack);
    }
}
