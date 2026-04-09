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
import org.jetbrains.annotations.NotNull;

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

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull VoxelShape getShape(BlockState state, @NotNull BlockGetter getter, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPES.get(state.getValue(FACING));
    }

    @Override
    protected @NotNull ImmutableMap<BlockState, VoxelShape> getShapeForEachState(@NotNull Function<BlockState, VoxelShape> p_152459_) {
        return super.getShapeForEachState(p_152459_);
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull VoxelShape getCollisionShape(@NotNull BlockState state, @NotNull BlockGetter getter, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return getShape(state, getter, pos, context);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean canSurvive(@NotNull BlockState state, LevelReader level, BlockPos pos) {
        BlockPos below = pos.below();
        BlockState support = level.getBlockState(below);

        return support.canOcclude() && support.isFaceSturdy(level, below, Direction.UP);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void neighborChanged(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos,
                                @NotNull Block neighborBlock, @NotNull BlockPos neighborPos, boolean movedByPiston) {

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
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }
}

