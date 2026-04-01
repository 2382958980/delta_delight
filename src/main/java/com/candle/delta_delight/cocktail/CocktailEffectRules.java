package com.candle.delta_delight.cocktail;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.ForgeRegistries;

public final class CocktailEffectRules {
    private CocktailEffectRules() {
    }

    public static int getMaxAmplifier(MobEffect effect) {
        String key = getEffectKey(effect);
        return switch (key) {
            case "minecraft:speed",
                    "minecraft:regeneration",
                    "minecraft:absorption",
                    "minecraft:instant_health",
                    "minecraft:luck",
                    "minecraft:jump_boost",
                    "minecraft:strength",
                    "minecraft:haste",
                    "minecraft:weakness",
                    "minecraft:slowness",
                    "minecraft:mining_fatigue" -> 2;
            default -> 0;
        };
    }

    public static boolean showsAmplifier(MobEffect effect) {
        return getMaxAmplifier(effect) > 0;
    }

    private static String getEffectKey(MobEffect effect) {
        ResourceLocation key = ForgeRegistries.MOB_EFFECTS.getKey(effect);
        return key == null ? "" : key.toString();
    }
}
