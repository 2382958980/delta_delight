package com.candle.delta_delight.registry;

import com.candle.delta_delight.DeltaDelight;
import com.candle.delta_delight.content.GeneralBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {

    // 创建 Block 注册表
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, DeltaDelight.MODID);

    public static final RegistryObject<Block> OLIVIA_CHAMPAGNE_BLOCK = BLOCKS.register(
            "olivia_champagne_block",
            () -> new GeneralBlock(BlockBehaviour.Properties
                    .of().strength(0.2f)        // 破坏难度
                    .noOcclusion()          // 不完全遮挡（对透明物体很重要）
                    .pushReaction(PushReaction.NORMAL),
                    6, 0, 6, 10, 12, 10
            )
    );
    public static final RegistryObject<Block> BLUE_SAPPHIRE_TEQUILA_BLOCK = BLOCKS.register(
            "blue_sapphire_tequila_block",
            () -> new GeneralBlock(BlockBehaviour.Properties
                    .of().strength(0.2f)
                    .noOcclusion()
                    .pushReaction(PushReaction.NORMAL),
                    5, 0, 5, 11, 14, 11
            )
    );
    public static final RegistryObject<Block> DIAMOND_CAVIAR_BLOCK = BLOCKS.register(
            "diamond_caviar_block",
            () -> new GeneralBlock(BlockBehaviour.Properties
                    .of().strength(0.2f)
                    .noOcclusion()
                    .pushReaction(PushReaction.NORMAL),
                    5, 0, 5, 11, 3, 11
            )
    );
    public static final RegistryObject<Block> COFFEE_BLOCK = BLOCKS.register(
            "coffee_block",
            () -> new GeneralBlock(BlockBehaviour.Properties
                    .of().strength(0.2f)
                    .noOcclusion()
                    .pushReaction(PushReaction.NORMAL),
                    6, 0, 6, 10, 8, 10
            )
    );
    public static final RegistryObject<Block> SEAFOOD_CANNED_PORRIDGE_BLOCK = BLOCKS.register(
            "seafood_canned_porridge_block",
            () -> new GeneralBlock(BlockBehaviour.Properties
                    .of().strength(0.2f)
                    .noOcclusion()
                    .pushReaction(PushReaction.NORMAL),
                    5, 0, 6.5, 11, 6, 9.5
            )
    );
    public static final RegistryObject<Block> NUTRITIOUS_CANNED_PORRIDGE_BLOCK = BLOCKS.register(
            "nouritous_canned_porridge_block",
            () -> new GeneralBlock(BlockBehaviour.Properties
                    .of().strength(0.2f)
                    .noOcclusion()
                    .pushReaction(PushReaction.NORMAL),
                    4.5, 0, 6, 11.5, 3.25, 10
            )
    );
}
