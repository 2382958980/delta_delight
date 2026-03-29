package com.candle.delta_delight.cocktail;

import com.candle.delta_delight.content.ShakerInventory;
import com.candle.delta_delight.registry.ModItems;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public final class CocktailBrewer {
    private static final int COMMON_NEGATIVE_DURATION = 20 * 120;
    private static final int UNCOMMON_NEGATIVE_DURATION = 20 * 30;
    private static final int COMMON_BUFF_DURATION = 20 * 30;
    private static final int RARE_BUFF_DURATION = 20 * 120;
    private static final float UNCOMMON_CHANCE = 0.6F;
    private static final float EPIC_PEAK_CHANCE = 0.8F;
    private static final float EPIC_FALLOFF_SECONDS = 5.0F;

    private CocktailBrewer() {
    }

    public static ItemStack brew(ShakerInventory inventory, float shakeSeconds, RandomSource random) {
        Optional<CocktailBase> baseOptional = CocktailBase.fromStack(inventory.getStackInSlot(ShakerInventory.BASE_SLOT));
        if (baseOptional.isEmpty()) {
            return ItemStack.EMPTY;
        }

        CocktailBase base = baseOptional.get();
        List<CocktailIngredient> ingredientList = inventory.getIngredientStacks().stream()
                .map(CocktailIngredient::fromStack)
                .flatMap(Optional::stream)
                .toList();

        if (ingredientList.isEmpty()) {
            return ItemStack.EMPTY;
        }

        Set<String> ingredientKeys = ingredientList.stream()
                .map(CocktailIngredient::getKey)
                .collect(LinkedHashSet::new, Set::add, Set::addAll);

        Optional<CocktailDefinition> epicDefinition = CocktailManager.findMatch(CocktailQuality.EPIC, base.getKey(), ingredientKeys);
        if (epicDefinition.isPresent() && random.nextFloat() < calculateEpicChance(epicDefinition.get(), shakeSeconds)) {
            return createDrink(epicDefinition.get().nameKey(), CocktailQuality.EPIC, base, ingredientList);
        }

        Optional<CocktailDefinition> uncommonDefinition = CocktailManager.findMatch(CocktailQuality.UNCOMMON, base.getKey(), ingredientKeys);
        if (uncommonDefinition.isPresent() && random.nextFloat() < UNCOMMON_CHANCE) {
            return createDrink(uncommonDefinition.get().nameKey(), CocktailQuality.UNCOMMON, base, ingredientList);
        }

        return createDrink(base.getCommonNameKey(), CocktailQuality.COMMON, base, ingredientList);
    }

    private static float calculateEpicChance(CocktailDefinition definition, float shakeSeconds) {
        if (shakeSeconds >= definition.minShakeSeconds() && shakeSeconds <= definition.maxShakeSeconds()) {
            return EPIC_PEAK_CHANCE;
        }

        float distance = shakeSeconds < definition.minShakeSeconds()
                ? definition.minShakeSeconds() - shakeSeconds
                : shakeSeconds - definition.maxShakeSeconds();
        float multiplier = Math.max(0.0F, 1.0F - distance / EPIC_FALLOFF_SECONDS);
        return EPIC_PEAK_CHANCE * multiplier;
    }

    private static ItemStack createDrink(String nameKey, CocktailQuality quality, CocktailBase base, List<CocktailIngredient> ingredients) {
        List<MobEffectInstance> effects = new ArrayList<>();
        if (quality != CocktailQuality.EPIC) {
            int negativeDuration = quality == CocktailQuality.COMMON ? COMMON_NEGATIVE_DURATION : UNCOMMON_NEGATIVE_DURATION;
            effects.add(new MobEffectInstance(base.getNegativeEffect(), negativeDuration, 0));
        }

        int amplifier = quality == CocktailQuality.EPIC ? 1 : 0;
        int duration = quality == CocktailQuality.COMMON ? COMMON_BUFF_DURATION : RARE_BUFF_DURATION;
        for (CocktailIngredient ingredient : ingredients) {
            MobEffect effect = ingredient.getEffect();
            int actualDuration = effect.isInstantenous() ? 1 : duration;
            effects.add(new MobEffectInstance(effect, actualDuration, amplifier));
        }

        return CocktailItem.create(nameKey, quality, effects, ModItems.MIXED_COCKTAIL.get());
    }
}
