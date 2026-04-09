package com.candle.delta_delight;

import com.candle.delta_delight.network.ModMessages;
import com.candle.delta_delight.registry.*;
import com.mojang.logging.LogUtils;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

@Mod(DeltaDelight.MODID)
public class DeltaDelight {
    public static final String MODID = "delta_delight";
    public static final Logger LOGGER = LogUtils.getLogger();


    public DeltaDelight(IEventBus modEventBus) {
        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModCreativeTabs.TABS.register(modEventBus);
        ModLootModifiers.LOOT_MODIFIERS.register(modEventBus);
        ModMenuTypes.MENU_TYPES.register(modEventBus);
        ModMessages.register();
    }
}
