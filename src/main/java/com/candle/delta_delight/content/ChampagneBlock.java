package com.candle.delta_delight.content;

import com.candle.delta_delight.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class ChampagneBlock extends Block {

    // 方块中心 0.5×0.5×0.8 转换成 16px 坐标
    private static final VoxelShape SHAPE = Block.box(
            6,     // minX
            0,     // minY
            6,     // minZ
            10,    // maxX
            10,  // maxY
            11     // maxZ
    );

    public ChampagneBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    // Shift + 右键取回
    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos,
                                 Player player, InteractionHand hand, BlockHitResult hit) {

        if (!player.isShiftKeyDown()) return InteractionResult.PASS;

        if (level.isClientSide) {
            player.swing(hand); // 修复空手潜行无动画 bug
            return InteractionResult.CONSUME;
        }

        // 服务端取回逻辑
        ItemStack stack = new ItemStack(ModItems.CHAMPAGNE.get());
        if (!player.getInventory().add(stack)) player.drop(stack, false);
        level.destroyBlock(pos, false);
        level.playSound(null, pos, SoundEvents.ITEM_FRAME_REMOVE_ITEM,
                SoundSource.BLOCKS, 1.0F, 1.0F);

        return InteractionResult.CONSUME_PARTIAL;
    }
}
