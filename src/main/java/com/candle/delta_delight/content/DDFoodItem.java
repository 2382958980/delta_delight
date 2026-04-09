package com.candle.delta_delight.content;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class DDFoodItem extends Item {
    private final Item returnItem;
    private final int useDuration;
    private final UseAnim useAnim;

    public DDFoodItem(Properties properties,
                      Item returnItem,
                      int useDuration,
                      UseAnim useAnim) {
        super(properties);
        this.returnItem = returnItem;
        this.useDuration = useDuration;
        this.useAnim = useAnim;
    }

    @Override
    public int getUseDuration(@NotNull ItemStack stack) {
        return useDuration;
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack stack) {
        return useAnim;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        return IDDFoodItem.use(this, level, player, hand);
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity entity) {
        ItemStack result = super.finishUsingItem(stack, level, entity);

        return IDDFoodItem.finishUsingItem(returnItem, result, entity);
    }
}
