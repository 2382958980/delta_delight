package com.candle.delta_delight.content;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import vectorwing.farmersdelight.common.registry.ModEffects;

public class CaviarItem extends Item {

    private final Block placeBlock;

    public CaviarItem(Block block, Properties props) {
        super(props
                .stacksTo(64));
        this.placeBlock = block;
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        Level level = ctx.getLevel();
        BlockPos pos = ctx.getClickedPos().relative(ctx.getClickedFace());
        Player player = ctx.getPlayer();
        ItemStack stack = ctx.getItemInHand();

        if (!level.getBlockState(pos).canBeReplaced(new net.minecraft.world.item.context.BlockPlaceContext(ctx))) {
            return InteractionResult.PASS;
        }
        BlockState state = placeBlock.defaultBlockState();
        level.setBlock(pos, state, 3);
        if (!player.isCreative()) {
            stack.shrink(1);
        }

        level.playSound(player, pos, SoundEvents.GLASS_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);

        return InteractionResult.sidedSuccess(level.isClientSide());
    }
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (player.canEat(true)) {
            player.startUsingItem(hand); // 开始“使用”动画（喝的动作）
            return InteractionResultHolder.consume(stack);
        }

        return InteractionResultHolder.pass(stack);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (entity instanceof Player player) {
            if (!level.isClientSide) {
                player.addEffect(new MobEffectInstance(ModEffects.NOURISHMENT.get(), 9000, 0));
                level.playSound(null, player.blockPosition(), SoundEvents.GENERIC_EAT,
                        SoundSource.PLAYERS, 1.0F, 1.0F);
            }
        }
        return stack;
    }

    // 使用时间
    @Override
    public int getUseDuration(ItemStack stack) {
        return 16;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.EAT;
    }
}