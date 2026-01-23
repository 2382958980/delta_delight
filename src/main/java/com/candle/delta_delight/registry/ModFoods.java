package com.candle.delta_delight.registry;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import vectorwing.farmersdelight.common.registry.ModEffects;

/**
 * 定义本 mod 的所有 FoodProperties。
 * 注意：
 *  - FoodProperties 通常是直接常量；
 *  - 饱食度（nutrition）和饱和度（saturationModifier）可按需求调整。
 */
public final class ModFoods {


    public static final FoodProperties CHAMPAGNE = new FoodProperties.Builder()
            .alwaysEat()
            .build();
    public static final FoodProperties TEQUILA = new FoodProperties.Builder()
            .alwaysEat()
            .build();
    public static final FoodProperties CAVIAR = new FoodProperties.Builder()
            .nutrition(1)
            .saturationMod(2F)
            .build();
    public static final FoodProperties COFFEE = new FoodProperties.Builder()
            .nutrition(1)
            .saturationMod(1F)
            .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 1800, 0), 1.0F)
            .alwaysEat()
            .build();
    public static final FoodProperties SEAFOODCAN = new FoodProperties.Builder()
            .nutrition(8)
            .saturationMod(0.5F)
            .effect(() -> new MobEffectInstance(ModEffects.NOURISHMENT.get(), 3000, 0), 1.0F)
            .alwaysEat()
            .build();
    public static final FoodProperties NOURISHCAN = new FoodProperties.Builder()
            .nutrition(8)
            .saturationMod(0.5F)
            .effect(() -> new MobEffectInstance(ModEffects.NOURISHMENT.get(), 3000, 0), 1.0F)
            .alwaysEat()
            .build();


    private ModFoods() { /* no instance */ }
}
