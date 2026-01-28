package com.candle.delta_delight.registry;

import com.candle.delta_delight.content.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import com.candle.delta_delight.Delta_delight;

public class ModBlocks {

    // 创建 Block 注册表
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, Delta_delight.MODID);

    public static final RegistryObject<Block> CHAMPAGNE_BLOCK = BLOCKS.register(
            "champagne_block",
            () -> new GeneralBlock(BlockBehaviour.Properties
                    .of().strength(0.2f)        // 破坏难度
                    .noOcclusion()          // 不完全遮挡（对透明物体很重要）
                    .pushReaction(PushReaction.NORMAL),
                    6,0,6,10,12,10
            )
    );
    public static final RegistryObject<Block> TEQUILA_BLOCK = BLOCKS.register(
            "tequila_block",
            () -> new GeneralBlock(BlockBehaviour.Properties
                    .of().strength(0.2f)
                    .noOcclusion()
                    .pushReaction(PushReaction.NORMAL),
                    5,0,5,11,14,11
            )
    );
    public static final RegistryObject<Block> CAVIAR_BLOCK = BLOCKS.register(
            "caviar_block",
            () -> new GeneralBlock(BlockBehaviour.Properties
                    .of().strength(0.2f)
                    .noOcclusion()
                    .pushReaction(PushReaction.NORMAL),
                    5,0,5,11,3,11
            )
    );
    public static final RegistryObject<Block> COFFEE_BLOCK = BLOCKS.register(
            "coffee_block",
            () -> new GeneralBlock(BlockBehaviour.Properties
                    .of().strength(0.2f)
                    .noOcclusion()
                    .pushReaction(PushReaction.NORMAL),
                    6,0,6,10,8,10
            )
    );
    public static final RegistryObject<Block> SEAFOODCAN_BLOCK = BLOCKS.register(
            "seafoodcan_block",
            () -> new GeneralBlock(BlockBehaviour.Properties
                    .of().strength(0.2f)
                    .noOcclusion()
                    .pushReaction(PushReaction.NORMAL),
                    5,0,6.5,11,6,9.5
            )
    );
}