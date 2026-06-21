package com.candle.delta_delight.content;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ZongziItem extends Item {
    public static final String SCORE_TAG = "ZongziScore";

    public ZongziItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        int score = getScore(stack);
        MutableComponent name = Component.translatable(getDescriptionId(stack) + "." + getTierKey(score));
        return score >= 10 ? name.withStyle(ChatFormatting.AQUA) : name;
    }

    @Override
    public @NotNull FoodProperties getFoodProperties(@NotNull ItemStack stack, @Nullable LivingEntity entity) {
        int score = getScore(stack);
        return new FoodProperties.Builder()
                .nutrition(score * 1 + 2)
                .saturationMod(1.0F)
                .build();
    }

    public static ItemStack create(int score) {
        ItemStack stack = new ItemStack(com.candle.delta_delight.registry.ModItems.ZONGZI.get());
        stack.getOrCreateTag().putInt(SCORE_TAG, clampScore(score));
        return stack;
    }

    public static int getScore(ItemStack stack) {
        if (stack.hasTag() && stack.getTag().contains(SCORE_TAG)) {
            return clampScore(stack.getTag().getInt(SCORE_TAG));
        }
        return 0;
    }

    public static String getTierKey(int score) {
        int clamped = clampScore(score);
        if (clamped <= 3) {
            return "crude";
        }
        if (clamped <= 6) {
            return "normal";
        }
        if (clamped <= 9) {
            return "tasty";
        }
        return "premium";
    }

    private static int clampScore(int score) {
        return Math.max(0, Math.min(12, score));
    }
}
