package com.candle.delta_delight;

import com.candle.delta_delight.network.ModMessages;
import com.candle.delta_delight.registry.*;
import com.mojang.logging.LogUtils;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(DeltaDelight.MODID)
public class DeltaDelight {
    public static final String MODID = "delta_delight";
    public static final Logger LOGGER = LogUtils.getLogger();


    public DeltaDelight() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModBlocks.BLOCKS.register(modEventBus);
        ModEntityTypes.ENTITY_TYPES.register(modEventBus);
        ModSoundEvents.SOUND_EVENTS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModCreativeTabs.TABS.register(modEventBus);
        ModLootModifiers.register(modEventBus);
        ModMenuTypes.MENU_TYPES.register(modEventBus);
        ModMessages.register();
    }
}
