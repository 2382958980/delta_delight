package com.candle.delta_delight;

import com.candle.delta_delight.client.screen.ShakerScreen;
import com.candle.delta_delight.cocktail.CocktailAppearanceManager;
import com.candle.delta_delight.cocktail.CocktailItem;
import com.candle.delta_delight.registry.ModItems;
import com.candle.delta_delight.registry.ModMenuTypes;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = DeltaDelight.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class DeltaDelightClient {
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        DeltaDelight.LOGGER.debug("DELTA DELIGHT Client Setup");
        event.enqueueWork(() -> {
            MenuScreens.register(ModMenuTypes.SHAKER.get(), ShakerScreen::new);
            ItemProperties.register(
                    ModItems.MIXED_COCKTAIL.get(),
                    ResourceLocation.fromNamespaceAndPath(DeltaDelight.MODID, "cocktail_base_style"),
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

    @SubscribeEvent
    public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
        DeltaDelight.LOGGER.debug("DELTA DELIGHT Register Item Colors");
        CocktailAppearanceManager.ensureLoaded();
        event.register(
                CocktailAppearanceManager::getTintColor,
                ModItems.MIXED_COCKTAIL.get()
        );
    }
}
