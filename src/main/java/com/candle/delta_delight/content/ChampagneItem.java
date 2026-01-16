package com.candle.delta_delight.content;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
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

public class ChampagneItem extends Item {

    private final Block placeBlock;

    public ChampagneItem(Block block, Properties props) {
        super(props);
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
            return InteractionResult.FAIL;
        }

        BlockState state = placeBlock.defaultBlockState();
        level.setBlock(pos, state, 3);

        if (!player.isCreative()) {
            stack.shrink(1);
        }

        level.playSound(player, pos, SoundEvents.AMETHYST_BLOCK_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);

        return InteractionResult.sidedSuccess(level.isClientSide());
    }
}