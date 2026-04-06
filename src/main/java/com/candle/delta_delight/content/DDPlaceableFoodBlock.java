package com.candle.delta_delight.content;

import com.candle.delta_delight.util.VoxelShapeHelper;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;

public class DDPlaceableFoodBlock extends HorizontalDirectionalBlock {

    private final Map<Direction, VoxelShape> SHAPES = new EnumMap<>(Direction.class);

    /**
     * @param properties 方块属性
     * @param minX       碰撞箱最小 X (0–16)
     * @param minY       碰撞箱最小 Y (0–16)
     * @param minZ       碰撞箱最小 Z (0–16)
     * @param maxX       碰撞箱最大 X (0–16)
     * @param maxY       碰撞箱最大 Y (0–16)
     * @param maxZ       碰撞箱最大 Z (0–16)
     */
    public DDPlaceableFoodBlock(Properties properties,
                                double minX, double minY, double minZ,
                                double maxX, double maxY, double maxZ) {
        super(properties);
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            SHAPES.put(direction, VoxelShapeHelper.rotateShape(direction, minX, minY, minZ, maxX, maxY, maxZ));
        }
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return SHAPES.get(state.getValue(FACING));
    }

    @Override
    protected ImmutableMap<BlockState, VoxelShape> getShapeForEachState(Function<BlockState, VoxelShape> p_152459_) {
        return super.getShapeForEachState(p_152459_);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return getShape(state, getter, pos, context);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos below = pos.below();
        BlockState support = level.getBlockState(below);

        return support.canOcclude() && support.isFaceSturdy(level, below, Direction.UP);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos,
                                Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {

        if (!this.canSurvive(state, level, pos)) {
            level.destroyBlock(pos, true);
        }

        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, movedByPiston);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }
}

