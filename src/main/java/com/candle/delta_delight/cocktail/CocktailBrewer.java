package com.candle.delta_delight.cocktail;

import com.candle.delta_delight.content.ShakerInventory;
import com.candle.delta_delight.registry.ModItems;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;

import java.util.*;

public final class CocktailBrewer {
    private static final int COMMON_NEGATIVE_DURATION = 20 * 120;
    private static final int UNCOMMON_NEGATIVE_DURATION = 20 * 30;
    private static final int COMMON_BUFF_DURATION = 20 * 30;
    private static final int RARE_BUFF_DURATION = 20 * 120;
    private static final float UNCOMMON_CHANCE = 0.6F;
    private static final float EPIC_PEAK_CHANCE = 0.8F;
    private static final float EPIC_FALLOFF_SECONDS = 5.0F;

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
            addOrMergeEffect(effects, new MobEffectInstance(base.getNegativeEffect(), negativeDuration, 0));
        }

        boolean sameIngredients = ingredients.size() == 2
                && ingredients.get(0).getKey().equals(ingredients.get(1).getKey());
        int amplifier = (quality == CocktailQuality.EPIC ? 1 : 0) + (sameIngredients ? 1 : 0);
        int duration = quality == CocktailQuality.COMMON ? COMMON_BUFF_DURATION : RARE_BUFF_DURATION;
        List<CocktailIngredient> appliedIngredients = sameIngredients
                ? List.of(ingredients.get(0))
                : ingredients;
        for (CocktailIngredient ingredient : appliedIngredients) {
            MobEffect effect = ingredient.getEffect();
            int actualAmplifier = Math.min(amplifier, CocktailEffectRules.getMaxAmplifier(effect));
            int actualDuration = effect.isInstantenous() ? 1 : duration;
            if (sameIngredients && actualAmplifier == 0 && !effect.isInstantenous()) {
                actualDuration *= 2;
            }
            addOrMergeEffect(effects, new MobEffectInstance(effect, actualDuration, actualAmplifier));
        }

        List<String> ingredientKeys = ingredients.stream()
                .map(CocktailIngredient::getKey)
                .toList();
        return CocktailItem.create(nameKey, quality, base.getKey(), effects, ingredientKeys, ModItems.MIXED_COCKTAIL.get());
    }

    private static void addOrMergeEffect(List<MobEffectInstance> effects, MobEffectInstance candidate) {
        for (int i = 0; i < effects.size(); i++) {
            MobEffectInstance existing = effects.get(i);
            if (existing.getEffect() == candidate.getEffect()) {
                effects.set(i, new MobEffectInstance(
                        candidate.getEffect(),
                        Math.max(existing.getDuration(), candidate.getDuration()),
                        Math.max(existing.getAmplifier(), candidate.getAmplifier())
                ));
                return;
            }
        }
        effects.add(candidate);
    }
}
