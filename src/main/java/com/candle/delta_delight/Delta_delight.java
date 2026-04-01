package com.candle.delta_delight;

import com.candle.delta_delight.client.screen.ShakerScreen;
import com.candle.delta_delight.cocktail.CocktailAppearanceManager;
import com.candle.delta_delight.cocktail.CocktailItem;
import com.candle.delta_delight.network.ModMessages;
import com.candle.delta_delight.registry.ModBlocks;
import com.candle.delta_delight.registry.ModCreativeTabs;
import com.candle.delta_delight.registry.ModItems;
import com.candle.delta_delight.registry.ModLootModifiers;
import com.candle.delta_delight.registry.ModMenuTypes;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

@Mod(Delta_delight.MODID)
public class Delta_delight {
    public static final String MODID = "delta_delight";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Delta_delight() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModCreativeTabs.TABS.register(modEventBus);
        ModLootModifiers.LOOT_MODIFIERS.register(modEventBus);
        ModMenuTypes.MENU_TYPES.register(modEventBus);
        ModMessages.register();

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);

        MinecraftForge.EVENT_BUS.register(this);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("HELLO FROM COMMON SETUP");
        LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));

        if (Config.logDirtBlock) {
            LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));
        }

        Config.items.forEach((item) -> LOGGER.info("ITEM >> {}", item));
    }

    private void clientSetup(FMLClientSetupEvent event) {
        LOGGER.info("DELTA DELIGHT Client Setup");
        event.enqueueWork(() -> {
            MenuScreens.register(ModMenuTypes.SHAKER.get(), ShakerScreen::new);
            ItemProperties.register(
                    ModItems.MIXED_COCKTAIL.get(),
                    ResourceLocation.fromNamespaceAndPath(MODID, "cocktail_base_style"),
                    (stack, level, entity, seed) -> switch (CocktailItem.getStoredBaseKey(stack)) {
                        case "qingyiyin" -> 1.0F;
                        case "hupolu" -> 2.0F;
                        case "tangmizhi" -> 3.0F;
                        default -> 0.0F;
                    }
            );
            CocktailAppearanceManager.ensureLoaded();
        });
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("HELLO from server starting");
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }

        @SubscribeEvent
        public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
            CocktailAppearanceManager.ensureLoaded();
            event.register(
                    (stack, tintIndex) -> CocktailAppearanceManager.getTintColor(stack, tintIndex),
                    ModItems.MIXED_COCKTAIL.get()
            );
        }
    }
}
