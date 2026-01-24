package com.candle.delta_delight.content;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import vectorwing.farmersdelight.common.registry.ModEffects;

import javax.annotation.Nullable;
import java.util.List;

public class ChampagneItem extends Item {

    private final Block placeBlock;

    public ChampagneItem(Block block, Properties props) {
        super(props
                .stacksTo(16));
        this.placeBlock = block;
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        Level level = ctx.getLevel();
        BlockPos pos = ctx.getClickedPos().relative(ctx.getClickedFace());
        Player player = ctx.getPlayer();
        ItemStack stack = ctx.getItemInHand();

        // 尝试放置
        if (!level.getBlockState(pos).canBeReplaced(new net.minecraft.world.item.context.BlockPlaceContext(ctx))) {
            return InteractionResult.PASS;//尝试其他交互（例如下面的屯屯屯
        }

        BlockState state = placeBlock.defaultBlockState();
        level.setBlock(pos, state, 3);

        if (!player.isCreative()) {
            stack.shrink(1);
        }

        level.playSound(player, pos, SoundEvents.AMETHYST_BLOCK_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);

        return InteractionResult.sidedSuccess(level.isClientSide());
    }
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        // 如果能吃（ModFoods定义了都能吃）
        if (player.canEat(true)) {
            player.startUsingItem(hand); // 开始“使用”动画（喝的动作）
            return InteractionResultHolder.consume(stack);
        }

        return InteractionResultHolder.pass(stack);
    }

    // 喝完那一刻触发
    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (entity instanceof Player player) {
            if (!level.isClientSide) {
                player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 3000, 0));
                player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 3000, 0));
                player.addEffect(new MobEffectInstance(ModEffects.COMFORT.get(), 3000, 0));
                // 播放喝饮料的声音
                level.playSound(null, player.blockPosition(), SoundEvents.GENERIC_DRINK,
                        SoundSource.PLAYERS, 1.0F, 1.0F);
            }

            // 生存模式下消耗一个，返还玻璃瓶
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
                if (stack.isEmpty()) {
                    return new ItemStack(Items.GLASS_BOTTLE); // 喝完剩玻璃瓶
                } else {
                    player.getInventory().add(new ItemStack(Items.GLASS_BOTTLE));
                }
            }
        }

        return stack;
    }
    //增加文本
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {

        // 普通提示（总是显示）
        tooltip.add(Component.translatable("item.delta_delight.champagne.tooltip").withStyle(ChatFormatting.GRAY));

        // 按住 Shift 显示更多
        if (Screen.hasShiftDown()) {
            tooltip.add(Component.translatable("item.delta_delight.champagne.tooltip.shift").withStyle(ChatFormatting.RED));
        }
        super.appendHoverText(stack, level, tooltip, flag);
    }

    // 使用时间
    @Override
    public int getUseDuration(ItemStack stack) {
        return 32;
    }

    // 使用动画（喝的动作）
    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }
}