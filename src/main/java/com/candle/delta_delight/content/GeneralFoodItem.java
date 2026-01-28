package com.candle.delta_delight.content;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class GeneralFoodItem extends Item {

    private final Block linkedBlock;
    private final Item returnItem;
    private final int useDuration;
    private final UseAnim useAnim;

    public GeneralFoodItem(Block linkedBlock,
                           Properties properties,
                           Item returnItem,
                           int useDuration,
                           UseAnim useAnim) {
        super(properties);
        this.linkedBlock = linkedBlock;
        this.returnItem = returnItem;
        this.useDuration = useDuration;
        this.useAnim = useAnim;
    }

    // 自定义食用时间
    @Override
    public int getUseDuration(ItemStack stack) {
        return useDuration;
    }

    // 自定义动画（吃 / 喝）
    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return useAnim;
    }

    // 右键开始使用
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(player.getItemInHand(hand));
    }

    // 食用完成逻辑
    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        ItemStack result = super.finishUsingItem(stack, level, entity);

        if (entity instanceof Player player && !player.isCreative()) {
            ItemStack container = new ItemStack(returnItem);

            if (!player.getInventory().add(container)) {
                player.drop(container, false);
            }
        }

        return result;
    }

    // 可选：右键方块时放置对应方块（例如酒瓶放桌面）
    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (linkedBlock == null) return InteractionResult.PASS;

        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos().relative(context.getClickedFace());
        Player player = context.getPlayer();

        if (!level.isClientSide && level.isEmptyBlock(pos)) {
            BlockState state = linkedBlock.defaultBlockState();

            level.setBlock(pos, state, 3);

            // 播放放置声音（关键）
            SoundType sound = state.getSoundType();
            level.playSound(
                    null,
                    pos,
                    sound.getPlaceSound(),
                    SoundSource.BLOCKS,
                    (sound.getVolume() + 1.0F) / 2.0F,
                    sound.getPitch() * 0.8F
            );

            // 触发游戏事件（兼容振动/监听系统）
            level.gameEvent(player, GameEvent.BLOCK_PLACE, pos);

            if (player != null && !player.isCreative()) {
                context.getItemInHand().shrink(1);
            }

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

}
