package com.candle.delta_delight.content;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class GeneralBlock extends Block {

    private final VoxelShape shape;

    /**
     * @param properties 方块属性
     * @param minX 碰撞箱最小 X (0–16)
     * @param minY 碰撞箱最小 Y (0–16)
     * @param minZ 碰撞箱最小 Z (0–16)
     * @param maxX 碰撞箱最大 X (0–16)
     * @param maxY 碰撞箱最大 Y (0–16)
     * @param maxZ 碰撞箱最大 Z (0–16)
     */
    public GeneralBlock(Properties properties,
                      double minX, double minY, double minZ,
                      double maxX, double maxY, double maxZ) {
        super(properties);
        this.shape = Block.box(minX, minY, minZ, maxX, maxY, maxZ);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return shape;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return shape;
    }
    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos below = pos.below();
        BlockState support = level.getBlockState(below);

        return support.isSolidRender(level, below);
    }
    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos,
                                Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {

        if (!this.canSurvive(state, level, pos)) {
            level.destroyBlock(pos, true);
        }

        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, movedByPiston);
    }
}

