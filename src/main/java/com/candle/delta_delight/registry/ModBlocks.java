package com.candle.delta_delight.registry;

import com.candle.delta_delight.DeltaDelight;
import com.candle.delta_delight.content.DDPlaceableFoodBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@SuppressWarnings("unused")
public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, DeltaDelight.MODID);

    @SuppressWarnings("SameParameterValue")
    private static RegistryObject<Block> registerPlaceableFoodBlock(
            String id, double minX, double minY, double minZ, double maxX, double maxY, double maxZ
    ) {
        return BLOCKS.register(
                id,
                () -> new DDPlaceableFoodBlock(BlockBehaviour.Properties.of()
                        .strength(0.2f)        // 破坏难度
                        .noOcclusion()          // 不完全遮挡（对透明物体很重要）
                        .pushReaction(PushReaction.NORMAL),
                        minX, minY, minZ, maxX, maxY, maxZ
                )
        );
    }

    public static final RegistryObject<Block> OLIVIA_CHAMPAGNE_BLOCK =
            registerPlaceableFoodBlock("olivia_champagne_block", 6, 0, 6, 10, 12, 10);
    public static final RegistryObject<Block> BLUE_SAPPHIRE_TEQUILA_BLOCK =
            registerPlaceableFoodBlock("blue_sapphire_tequila_block", 5, 0, 5, 11, 14, 11);
    public static final RegistryObject<Block> DIAMOND_CAVIAR_BLOCK =
            registerPlaceableFoodBlock("diamond_caviar_block", 5, 0, 5, 11, 3, 11);
    public static final RegistryObject<Block> COFFEE_BLOCK =
            registerPlaceableFoodBlock("coffee_block", 6, 0, 6, 10, 8, 10);
    public static final RegistryObject<Block> SEAFOOD_CANNED_PORRIDGE_BLOCK =
            registerPlaceableFoodBlock("seafood_canned_porridge_block", 5, 0, 6.5, 11, 6, 9.5);
    public static final RegistryObject<Block> NUTRITIOUS_CANNED_PORRIDGE_BLOCK =
            registerPlaceableFoodBlock("nutritious_canned_porridge_block", 4.5, 0, 6, 11.5, 3.25, 10);
    public static final RegistryObject<Block> PORRIDGE_BLOCK =
            registerPlaceableFoodBlock("porridge_block", 6, 0, 6, 10, 6.5, 10);
}
