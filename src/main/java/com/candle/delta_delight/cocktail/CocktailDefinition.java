package com.candle.delta_delight.cocktail;

import net.minecraft.resources.ResourceLocation;

import java.util.Set;

public record CocktailDefinition(
        ResourceLocation id,
        String nameKey,
        CocktailQuality quality,
        String baseKey,
        Set<String> ingredientKeys,
        float minShakeSeconds,
        float maxShakeSeconds
) {
    public boolean matches(String base, Set<String> ingredients) {
        return baseKey.equals(base) && ingredientKeys.equals(ingredients);
    }
}
