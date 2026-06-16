package com.candle.delta_delight.registry;

import com.candle.delta_delight.DeltaDelight;
import com.candle.delta_delight.content.BirdsnestBlockEntity;
import com.candle.delta_delight.content.ShellBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModBlockEntityTypes {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, DeltaDelight.MODID);

    public static final RegistryObject<BlockEntityType<BirdsnestBlockEntity>> BIRDSNEST = BLOCK_ENTITY_TYPES.register(
            "birdsnest",
            () -> BlockEntityType.Builder.of(BirdsnestBlockEntity::new, ModBlocks.BIRDSNEST_BLOCK.get()).build(null)
    );
    public static final RegistryObject<BlockEntityType<ShellBlockEntity>> SHELL = BLOCK_ENTITY_TYPES.register(
            "shell",
            () -> BlockEntityType.Builder.of(ShellBlockEntity::new, ModBlocks.SHELL_BLOCK.get()).build(null)
    );

    private ModBlockEntityTypes() {
    }
}
