package com.candle.delta_delight.content;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class DDPlaceableFoodBlockItem extends BlockItem {
    private final Item returnItem;
    private final int useDuration;
    private final UseAnim useAnim;

    public DDPlaceableFoodBlockItem(Block block,
                                    Properties properties,
                                    Item returnItem,
                                    int useDuration,
                                    UseAnim useAnim) {
        super(block, properties);
        this.returnItem = returnItem;
        this.useDuration = useDuration;
        this.useAnim = useAnim;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return useDuration;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return useAnim;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        super.use(level, player, hand); // 先调用父类的 use 方法以处理放置逻辑

        return IDDFoodItem.use(this, level, player, hand);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        ItemStack result = super.finishUsingItem(stack, level, entity);

        return IDDFoodItem.finishUsingItem(returnItem, result, entity);
    }
}
