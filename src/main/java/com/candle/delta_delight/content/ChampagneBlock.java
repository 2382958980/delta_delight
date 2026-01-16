package com.candle.delta_delight.content;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

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
}
