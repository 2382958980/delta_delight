package com.candle.delta_delight;

import com.candle.delta_delight.client.screen.ShakerScreen;
import com.candle.delta_delight.cocktail.CocktailAppearanceManager;
import com.candle.delta_delight.cocktail.CocktailItem;
import com.candle.delta_delight.network.ModMessages;
import com.candle.delta_delight.registry.*;
import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(DeltaDelight.MODID)
public class DeltaDelight {
    public static final String MODID = "delta_delight";
    public static final Logger LOGGER = LogUtils.getLogger();

    public DeltaDelight() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModCreativeTabs.TABS.register(modEventBus);
        ModLootModifiers.LOOT_MODIFIERS.register(modEventBus);
        ModMenuTypes.MENU_TYPES.register(modEventBus);
        ModMessages.register();
    }

    @EventBusSubscriber(value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
            CocktailAppearanceManager.ensureLoaded();
            event.register(
                    CocktailAppearanceManager::getTintColor,
                    ModItems.MIXED_COCKTAIL.get()
            );
        }

        @SubscribeEvent
        public static void clientSetup(FMLClientSetupEvent event) {
            LOGGER.info("DELTA DELIGHT Client Setup");
            event.enqueueWork(() -> {
                MenuScreens.register(ModMenuTypes.SHAKER.get(), ShakerScreen::new);
                ItemProperties.register(
                        ModItems.MIXED_COCKTAIL.get(),
                        ResourceLocation.fromNamespaceAndPath(MODID, "cocktail_base_style"),
                        (stack, level, entity, seed) -> switch (CocktailItem.getStoredBaseKey(stack)) {
                            case "herbal_tea" -> 1.0F;
                            case "amber_essence" -> 2.0F;
                            case "molasses" -> 3.0F;
                            default -> 0.0F;
                        }
                );
                CocktailAppearanceManager.ensureLoaded();
            });
        }
    }
}
