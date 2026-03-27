package com.candle.delta_delight.registry;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import vectorwing.farmersdelight.common.registry.ModEffects;

public final class ModFoods {

    public static final FoodProperties CHAMPAGNE = new FoodProperties.Builder()
            .nutrition(0)
            .saturationMod(0.0F)
            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 3200, 0), 1.0F)
            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 3200, 0), 1.0F)
            .alwaysEat()
            .build();

    public static final FoodProperties CAVIAR = new FoodProperties.Builder()
            .nutrition(1)
            .saturationMod(8.0F)
            .effect(() -> new MobEffectInstance(ModEffects.NOURISHMENT.get(), 6000, 0), 1.0F)
            .effect(() -> new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 3000, 0), 1.0F)
            .alwaysEat()
            .build();

    public static final FoodProperties TEQUILA = new FoodProperties.Builder()
            .nutrition(0)
            .saturationMod(0.0F)
            .effect(() -> new MobEffectInstance(MobEffects.LUCK, 3200, 1), 1.0F)
            .alwaysEat()
            .build();

    public static final FoodProperties COFFEE = new FoodProperties.Builder()
            .nutrition(2)
            .saturationMod(2.0F)
            .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 1200, 0), 1.0F)
            .effect(() -> new MobEffectInstance(MobEffects.NIGHT_VISION, 1200, 0), 1.0F)
            .alwaysEat()
            .build();

    public static final FoodProperties SEAFOODCAN = new FoodProperties.Builder()
            .nutrition(7)
            .saturationMod(1F)
            .effect(() -> new MobEffectInstance(ModEffects.NOURISHMENT.get(), 6000, 0), 1.0F)
            .build();

    public static final FoodProperties NOURISHCAN = new FoodProperties.Builder()
            .nutrition(8)
            .saturationMod(0.75F)
            .effect(() -> new MobEffectInstance(ModEffects.NOURISHMENT.get(), 6000, 0), 1.0F)
            .build();

    public static final FoodProperties LEMONTEA = new FoodProperties.Builder()
            .nutrition(2)
            .saturationMod(2.0F)
            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 300, 1), 1.0F)
            .alwaysEat()
            .build();

    public static final FoodProperties GINGERBREADMAN = new FoodProperties.Builder()
            .nutrition(7)
            .saturationMod(1F)
            .effect(() -> new MobEffectInstance(ModEffects.NOURISHMENT.get(), 3600, 0), 1.0F)
            .build();

    public static final FoodProperties ORANGEL = new FoodProperties.Builder()
            .nutrition(1)
            .saturationMod(3.0F)
            .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 600, 0), 1.0F)
            .build();

    public static final FoodProperties ENGLISHTEA = new FoodProperties.Builder()
            .nutrition(3)
            .saturationMod(0F)
            .build();

    public static final FoodProperties COLA = new FoodProperties.Builder()
            .nutrition(2)
            .saturationMod(1.0F)
            .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 600, 0), 1.0F)
            .alwaysEat()
            .build();

    public static final FoodProperties ARMYCAN = new FoodProperties.Builder()
            .nutrition(12)
            .saturationMod(0.5F)
            .effect(() -> new MobEffectInstance(ModEffects.COMFORT.get(), 6000, 0), 1.0F)
            .effect(() -> new MobEffectInstance(ModEffects.NOURISHMENT.get(), 6000, 0), 1.0F)
            .build();

    public static final FoodProperties YUMMYNOODLES = new FoodProperties.Builder()
            .nutrition(9)
            .saturationMod(0.5F)
            .effect(() -> new MobEffectInstance(ModEffects.NOURISHMENT.get(), 3000, 0), 1.0F)
            .build();

    public static final FoodProperties VITABLETS = new FoodProperties.Builder()
            .nutrition(1)
            .saturationMod(1.0F)
            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 200, 0), 1.0F)
            .build();

    public static final FoodProperties YOGURT = new FoodProperties.Builder()
            .nutrition(4)
            .saturationMod(0.5F)
            .build();

    public static final FoodProperties FASTNOODLES = new FoodProperties.Builder()
            .nutrition(6)
            .saturationMod(0.5F)
            .build();

    public static final FoodProperties WILDBAR = new FoodProperties.Builder()
            .nutrition(3)
            .saturationMod(0.5F)
            .build();

    public static final FoodProperties NOSUGARBAR = new FoodProperties.Builder()
            .nutrition(2)
            .saturationMod(1F)
            .build();

    public static final FoodProperties SUGARTRIANGLE = new FoodProperties.Builder()
            .nutrition(4)
            .saturationMod(1F)
            .build();

    public static final FoodProperties PORRIDGE = new FoodProperties.Builder()
            .nutrition(8)
            .saturationMod(0.75F)
            .build();

    private ModFoods() {
    }
}
