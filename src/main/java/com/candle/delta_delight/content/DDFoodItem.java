package com.candle.delta_delight.content;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

public class DDFoodItem extends Item {
    private final Block linkedBlock;
    private final Item returnItem;
    private final int useDuration;
    private final UseAnim useAnim;

    public DDFoodItem(Block linkedBlock,
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
        ItemStack stack = player.getItemInHand(hand);
        FoodProperties foodProperties = this.getFoodProperties();
        if (foodProperties != null && player.canEat(foodProperties.canAlwaysEat())) {
            return ItemUtils.startUsingInstantly(level, player, hand);
        }
        return InteractionResultHolder.fail(stack);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        ItemStack result = super.finishUsingItem(stack, level, entity);

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

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (linkedBlock == null) {
            return InteractionResult.PASS;
        }

        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos().relative(context.getClickedFace());
        Player player = context.getPlayer();
        BlockState state = linkedBlock.defaultBlockState();
        BlockPlaceContext placeContext = new BlockPlaceContext(context);

        if (!level.isClientSide
                && level.getBlockState(pos).canBeReplaced(placeContext)
                && state.canSurvive(level, pos)) {

            level.setBlock(pos, state, 3);

            SoundType sound = state.getSoundType();
            level.playSound(
                    null,
                    pos,
                    sound.getPlaceSound(),
                    SoundSource.BLOCKS,
                    (sound.getVolume() + 1.0F) / 2.0F,
                    sound.getPitch() * 0.8F
            );

            level.gameEvent(player, GameEvent.BLOCK_PLACE, pos);

            if (player != null && !player.isCreative()) {
                context.getItemInHand().shrink(1);
            }

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }
}
