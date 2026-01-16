package com.candle.delta_delight.registry;

import net.minecraft.world.food.FoodProperties;

/**
 * 定义本 mod 的所有 FoodProperties。
 * 注意：
 *  - FoodProperties 通常是直接常量；
 *  - 饱食度（nutrition）和饱和度（saturationModifier）可按需求调整。
 */
public final class ModFoods {


    public static final FoodProperties CHAMPAGNE = new FoodProperties.Builder()
            .nutrition(2)            // 恢复饱食度（等于 1 鼓腿）
            .saturationMod(0.4F)
            .alwaysEat()              // 饱食状态也可饮用
            .build();

    private ModFoods() { /* no instance */ }
}
