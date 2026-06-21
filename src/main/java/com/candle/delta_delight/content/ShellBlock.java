package com.candle.delta_delight.content;

import com.candle.delta_delight.DeltaDelight;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.advancements.Advancement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class ShellBlock extends HorizontalDirectionalBlock implements EntityBlock, SimpleWaterloggedBlock {
    public static final BooleanProperty OPEN = BooleanProperty.create("open");
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final IntegerProperty ROTATION = IntegerProperty.create("rotation", 0, 15);
    private static final ResourceLocation BREAD_IN_SHELL_ADVANCEMENT =
            new ResourceLocation(DeltaDelight.MODID, "bread_in_shell");
    private static final int ROTATION_SEGMENTS = 16;
    private static final float DEGREES_PER_SEGMENT = 360.0F / ROTATION_SEGMENTS;
    private static final VoxelShape CENTERED_SHAPE = Block.box(4.5D, 0.0D, 4.5D, 11.5D, 2.0D, 11.5D);

    public ShellBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(OPEN, false)
                .setValue(WATERLOGGED, false)
                .setValue(ROTATION, 0));
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull VoxelShape getShape(BlockState state, @NotNull BlockGetter getter, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return CENTERED_SHAPE;
    }

    @Override
    protected @NotNull ImmutableMap<BlockState, VoxelShape> getShapeForEachState(@NotNull Function<BlockState, VoxelShape> shapeFunction) {
        return super.getShapeForEachState(shapeFunction);
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull VoxelShape getCollisionShape(@NotNull BlockState state, @NotNull BlockGetter getter, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return getShape(state, getter, pos, context);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean canSurvive(@NotNull BlockState state, LevelReader level, BlockPos pos) {
        return Block.canSupportCenter(level, pos.below(), Direction.UP);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void neighborChanged(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos,
                                @NotNull Block neighborBlock, @NotNull BlockPos neighborPos, boolean movedByPiston) {
        if (!canSurvive(state, level, pos)) {
            level.destroyBlock(pos, true);
        }
        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, movedByPiston);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
        int rotation = context.getLevel().getRandom().nextInt(ROTATION_SEGMENTS);
        return defaultBlockState()
                .setValue(FACING, getFacingForRotation(rotation))
                .setValue(OPEN, false)
                .setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER)
                .setValue(ROTATION, rotation);
    }

    public static float getRotationDegrees(BlockState state) {
        return state.getValue(ROTATION) * DEGREES_PER_SEGMENT;
    }

    public static Direction getFacingForRotation(int rotation) {
        int quadrant = Math.floorMod(rotation + 2, ROTATION_SEGMENTS) / 4;
        return switch (quadrant) {
            case 1 -> Direction.EAST;
            case 2 -> Direction.SOUTH;
            case 3 -> Direction.WEST;
            default -> Direction.NORTH;
        };
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull BlockState updateShape(@NotNull BlockState state, @NotNull Direction direction,
                                           @NotNull BlockState neighborState, @NotNull LevelAccessor level,
                                           @NotNull BlockPos currentPos, @NotNull BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return super.updateShape(state, direction, neighborState, level, currentPos, neighborPos);
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos,
                                          @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hit) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        boolean open = state.getValue(OPEN);
        if (!open) {
            level.setBlock(pos, state.setValue(OPEN, true), Block.UPDATE_ALL);
            playOpenSound(level, pos);
            if (state.getValue(WATERLOGGED)) {
                spawnUnderwaterOpenParticles(level, pos);
            }
            awardBreadInShellAdvancement(level, pos, player);
            return InteractionResult.CONSUME;
        }

        if (player.isShiftKeyDown()) {
            level.setBlock(pos, state.setValue(OPEN, false), Block.UPDATE_ALL);
            playCloseSound(level, pos);
            return InteractionResult.CONSUME;
        }

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof ShellBlockEntity shell) {
            ItemStack held = player.getItemInHand(hand);
            if (!shell.isEmpty() && held.isEmpty()) {
                ItemStack removed = shell.removeStoredItem();
                player.setItemInHand(hand, removed);
                return InteractionResult.CONSUME;
            }

            if (shell.isEmpty() && !held.isEmpty()) {
                ItemStack stored = held.copy();
                stored.setCount(1);
                shell.setStoredItem(stored);
                shell.setNaturallyGenerated(false);
                held.shrink(1);
                return InteractionResult.CONSUME;
            }
        }

        return InteractionResult.PASS;
    }

    private static void awardBreadInShellAdvancement(Level level, BlockPos pos, Player player) {
        if (!(player instanceof ServerPlayer serverPlayer)) {
            return;
        }

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof ShellBlockEntity shell)
                || !shell.isNaturallyGenerated()
                || !shell.getStoredItem().is(Items.BREAD)) {
            return;
        }

        Advancement advancement = serverPlayer.server.getAdvancements().getAdvancement(BREAD_IN_SHELL_ADVANCEMENT);
        if (advancement != null) {
            serverPlayer.getAdvancements().award(advancement, "open_natural_bread_shell");
        }
    }

    private static void playOpenSound(Level level, BlockPos pos) {
        level.playSound(null, pos, SoundEvents.BUBBLE_COLUMN_BUBBLE_POP, SoundSource.BLOCKS, 0.6F, 1.2F);
    }

    private static void playCloseSound(Level level, BlockPos pos) {
        level.playSound(null, pos, SoundEvents.BUBBLE_COLUMN_BUBBLE_POP, SoundSource.BLOCKS, 0.6F, 0.9F);
    }

    private static void spawnUnderwaterOpenParticles(Level level, BlockPos pos) {
        double x = pos.getX() + 0.5D;
        double y = pos.getY() + 0.25D;
        double z = pos.getZ() + 0.5D;

        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.BUBBLE, x, y, z, 12, 0.25D, 0.12D, 0.25D, 0.02D);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onRemove(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos,
                         @NotNull BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof ShellBlockEntity shell && !shell.isEmpty()) {
                Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), shell.getStoredItem());
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new ShellBlockEntity(pos, state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING, OPEN, WATERLOGGED, ROTATION);
    }
}
