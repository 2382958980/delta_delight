package com.candle.delta_delight.content;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.level.Level;

public interface IDDFoodItem {
    static InteractionResultHolder<ItemStack> use(Item item, Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        FoodProperties foodProperties = item.getFoodProperties(stack, player);
        if (foodProperties != null && player.canEat(foodProperties.canAlwaysEat())) {
            return ItemUtils.startUsingInstantly(level, player, hand);
        }
        return InteractionResultHolder.fail(stack);
    }

    static ItemStack finishUsingItem(Item returnItem, ItemStack result, LivingEntity entity) {

        if (returnItem != null && entity instanceof Player player && !player.isCreative()) {
            ItemStack container = new ItemStack(returnItem);
            if (result.isEmpty()) {
                return container;
            }
            if (!player.getInventory().add(container)) {
                player.drop(container, false);
            }
        }

        return result;
    }
}
