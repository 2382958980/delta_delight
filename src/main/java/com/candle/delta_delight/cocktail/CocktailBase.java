package com.candle.delta_delight.cocktail;

import com.candle.delta_delight.registry.ModItems;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Supplier;

public enum CocktailBase {
    JUNIPER_SPIRIT("juniper_spirit", "cocktail.delta_delight.common.juniper_spirit", ModItems.JUNIPER_SPIRIT, () -> MobEffects.WEAKNESS),
    HERBAL_TEA("herbal_tea", "cocktail.delta_delight.common.qingyiyin", ModItems.HERBAL_TEA, () -> MobEffects.CONFUSION),
    AMBER_ESSENCE("amber_essence", "cocktail.delta_delight.common.hupolu", ModItems.AMBER_ESSENCE, () -> MobEffects.MOVEMENT_SLOWDOWN),
    MOLASSES("molasses", "cocktail.delta_delight.common.tangmizhi", ModItems.MOLASSES, () -> MobEffects.DIG_SLOWDOWN);

    private final String key;
    private final String commonNameKey;
    private final Supplier<Item> itemSupplier;
    private final Supplier<MobEffect> negativeEffect;

    CocktailBase(String key, String commonNameKey, Supplier<Item> itemSupplier, Supplier<MobEffect> negativeEffect) {
        this.key = key;
        this.commonNameKey = commonNameKey;
        this.itemSupplier = itemSupplier;
        this.negativeEffect = negativeEffect;
    }

    public String getKey() {
        return key;
    }

    public String getCommonNameKey() {
        return commonNameKey;
    }

    public MobEffect getNegativeEffect() {
        return negativeEffect.get();
    }

    public boolean matches(ItemStack stack) {
        return stack.is(itemSupplier.get());
    }

    public static Optional<CocktailBase> fromStack(ItemStack stack) {
        return Arrays.stream(values()).filter(base -> base.matches(stack)).findFirst();
    }

    public static Optional<CocktailBase> fromKey(String key) {
        String normalized = key.toLowerCase(Locale.ROOT);
        return Arrays.stream(values()).filter(base -> base.key.equals(normalized)).findFirst();
    }
}
