package com.candle.delta_delight.registry;

import com.candle.delta_delight.content.ChampagneItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import com.candle.delta_delight.registry.ModBlocks;

/**
 * ModItems - 负责注册本 mod 的 Item 对象
 *
 * 使用方式（在主 mod 类的构造器或 setup 中）:
 *    ModItems.register(FMLJavaModLoadingContext.get().getModEventBus());
 */
public final class ModItems {

    public static final String MODID = "delta_delight";
    // DeferredRegister 用于注册 Item
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

    public static final RegistryObject<Item> CHAMPAGNE =
            ITEMS.register("champagne",
                    () -> new ChampagneItem(ModBlocks.CHAMPAGNE_BLOCK.get(),
                            new Item.Properties()
                                    .food(ModFoods.CHAMPAGNE)
                    )
            );

    private ModItems() { /* no instantiation */ }

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}