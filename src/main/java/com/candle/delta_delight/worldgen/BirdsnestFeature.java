package com.candle.delta_delight.worldgen;

import com.candle.delta_delight.DeltaDelight;
import com.candle.delta_delight.content.BirdsnestBlock;
import com.candle.delta_delight.content.BirdsnestBlockEntity;
import com.candle.delta_delight.registry.ModBlocks;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import org.jetbrains.annotations.NotNull;

public class BirdsnestFeature extends Feature<NoneFeatureConfiguration> {
    private static final ResourceLocation LOOT_TABLE = new ResourceLocation(DeltaDelight.MODID, "chests/birdsnest");
    private static final int SEARCH_RADIUS = 4;
    private static final int SEARCH_ABOVE_SURFACE = 32;
    private static final int SEARCH_BELOW_SURFACE = 4;
    private static final int ATTEMPTS = 8;

    public BirdsnestFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(@NotNull FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        RandomSource random = context.random();
        BlockPos origin = context.origin();
        BlockPos.MutableBlockPos supportPos = new BlockPos.MutableBlockPos();

        for (int attempt = 0; attempt < ATTEMPTS; attempt++) {
            int x = origin.getX() + random.nextInt(SEARCH_RADIUS * 2 + 1) - SEARCH_RADIUS;
            int z = origin.getZ() + random.nextInt(SEARCH_RADIUS * 2 + 1) - SEARCH_RADIUS;
            int surfaceY = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, x, z);
            int topY = Math.min(level.getMaxBuildHeight() - 2, surfaceY + SEARCH_ABOVE_SURFACE);
            int minY = Math.max(level.getMinBuildHeight(), surfaceY - SEARCH_BELOW_SURFACE);

            for (int y = topY; y >= minY; y--) {
                supportPos.set(x, y, z);
                if (!level.getBlockState(supportPos).is(BlockTags.LEAVES)) {
                    continue;
                }

                BlockPos nestPos = supportPos.above();
                if (tryPlaceNest(level, random, nestPos)) {
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean tryPlaceNest(WorldGenLevel level, RandomSource random, BlockPos pos) {
        if (!level.isEmptyBlock(pos)) {
            return false;
        }

        int rotation = random.nextInt(16);
        BlockState state = ModBlocks.BIRDSNEST_BLOCK.get().defaultBlockState()
                .setValue(BirdsnestBlock.FACING, BirdsnestBlock.getFacingForRotation(rotation))
                .setValue(BirdsnestBlock.ROTATION, rotation);
        if (!state.canSurvive(level, pos) || !level.setBlock(pos, state, 2)) {
            return false;
        }

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof BirdsnestBlockEntity birdsnest) {
            birdsnest.setLootTable(LOOT_TABLE, random.nextLong());
        }
        return true;
    }
}
