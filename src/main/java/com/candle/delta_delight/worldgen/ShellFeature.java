package com.candle.delta_delight.worldgen;

import com.candle.delta_delight.content.ShellBlock;
import com.candle.delta_delight.content.ShellBlockEntity;
import com.candle.delta_delight.registry.ModBlocks;
import com.candle.delta_delight.registry.ModItems;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;

public class ShellFeature extends Feature<NoneFeatureConfiguration> {
    private static final int ROTATION_SEGMENTS = 16;
    private static final int SEARCH_RADIUS = 4;
    private static final int ATTEMPTS = 8;
    private static final WeightedItem[] LOOT = {
            new WeightedItem(Items.KELP, 35),
            new WeightedItem(Items.SEAGRASS, 35),
            new WeightedItem(Items.FLINT, 35),
            new WeightedItem(Items.IRON_NUGGET, 35),
            new WeightedItem(Items.GOLD_NUGGET, 35),
            new WeightedItem(Items.PUFFERFISH, 35),
            new WeightedItem(Items.BREAD, 35),
            new WeightedItem(Items.CLAY, 35),
            new WeightedItem(Items.ENDER_PEARL, 15),
            new WeightedItem(Items.PEARLESCENT_FROGLIGHT, 15),
            new WeightedItem(Items.NAUTILUS_SHELL, 15),
            new WeightedItem(Items.PRISMARINE_SHARD, 15),
            new WeightedItem(Items.AMETHYST_SHARD, 15),
            new WeightedItem(Items.PRISMARINE_CRYSTALS, 15),
            new WeightedItem(Items.LAPIS_LAZULI, 15),
            new WeightedItem(Items.DIAMOND, 5),
            new WeightedItem(Items.GHAST_TEAR, 5),
            new WeightedItem(Items.EMERALD, 5),
            new WeightedItem(Items.GOLD_BLOCK, 5),
            new WeightedItem(Items.ANCIENT_DEBRIS, 4),
            new WeightedItem(ModItems.TEAR_OF_OCEAN.get(), 1)
    };

    public ShellFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(@NotNull FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        RandomSource random = context.random();
        BlockPos origin = context.origin();

        for (int attempt = 0; attempt < ATTEMPTS; attempt++) {
            int x = origin.getX() + random.nextInt(SEARCH_RADIUS * 2 + 1) - SEARCH_RADIUS;
            int z = origin.getZ() + random.nextInt(SEARCH_RADIUS * 2 + 1) - SEARCH_RADIUS;
            int y = level.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, x, z);
            BlockPos shellPos = new BlockPos(x, y, z);

            if (tryPlaceShell(level, random, shellPos)) {
                return true;
            }
        }

        return false;
    }

    private static boolean tryPlaceShell(WorldGenLevel level, RandomSource random, BlockPos pos) {
        BlockState targetState = level.getBlockState(pos);
        if (targetState.getFluidState().getType() != Fluids.WATER || !isValidShellSpace(targetState)) {
            return false;
        }

        int rotation = random.nextInt(ROTATION_SEGMENTS);
        BlockState shellState = ModBlocks.SHELL_BLOCK.get().defaultBlockState()
                .setValue(ShellBlock.FACING, ShellBlock.getFacingForRotation(rotation))
                .setValue(ShellBlock.OPEN, false)
                .setValue(ShellBlock.WATERLOGGED, true)
                .setValue(ShellBlock.ROTATION, rotation);
        if (!shellState.canSurvive(level, pos) || !level.setBlock(pos, shellState, 2)) {
            return false;
        }

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof ShellBlockEntity shell) {
            shell.setStoredItem(randomLoot(random));
            shell.setNaturallyGenerated(true);
        }
        return true;
    }

    private static boolean isValidShellSpace(BlockState state) {
        return state.is(Blocks.WATER)
                || state.is(Blocks.SEAGRASS)
                || state.is(Blocks.TALL_SEAGRASS)
                || state.is(Blocks.KELP)
                || state.is(Blocks.KELP_PLANT)
                || state.is(Blocks.SEA_PICKLE);
    }

    private static ItemStack randomLoot(RandomSource random) {
        int totalWeight = 0;
        for (WeightedItem item : LOOT) {
            totalWeight += item.weight();
        }

        int selected = random.nextInt(totalWeight);
        for (WeightedItem item : LOOT) {
            selected -= item.weight();
            if (selected < 0) {
                return new ItemStack(item.item());
            }
        }
        return new ItemStack(LOOT[0].item());
    }

    private record WeightedItem(Item item, int weight) {
    }
}
