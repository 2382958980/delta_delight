package com.candle.delta_delight.registry;

import com.candle.delta_delight.content.CaviarItem;
import com.candle.delta_delight.content.ChampagneItem;
import com.candle.delta_delight.content.DrinkItem;
import com.candle.delta_delight.content.TequilaItem;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
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
                                    .rarity(Rarity.EPIC)
                    )
            );
    public static final RegistryObject<Item> CAVIAR =
            ITEMS.register("caviar",
                    () -> new CaviarItem(ModBlocks.CAVIAR_BLOCK.get(),
                            new Item.Properties()
                                    .food(ModFoods.CAVIAR)
                                    .rarity(Rarity.EPIC)
                    )
            );
    public static final RegistryObject<Item> TEQUILA =
            ITEMS.register("tequila",
                    () -> new TequilaItem(ModBlocks.TEQUILA_BLOCK.get(),
                            new Item.Properties()
                                    .food(ModFoods.TEQUILA)
                                    .rarity(Rarity.UNCOMMON)
                    )
            );
    public static final RegistryObject<Item> COFFEE =
            ITEMS.register("coffee",
                    () -> new TequilaItem(ModBlocks.COFFEE_BLOCK.get(),
                            new Item.Properties()
                                    .food(ModFoods.TEQUILA)
                                    .rarity(Rarity.UNCOMMON)
                    )
            );

    public static final RegistryObject<Item> SEAFOODCAN = ITEMS.register("seafoodcan",
            () -> new Item((new Item.Properties()
                    .food(ModFoods.SEAFOODCAN))));
    public static final RegistryObject<Item> NOURISHCAN = ITEMS.register("nourishcan",
            () -> new Item((new Item.Properties()
                    .food(ModFoods.NOURISHCAN))));
    public static final RegistryObject<Item> LEMONTEA = ITEMS.register("lemontea",
            () -> new DrinkItem((new Item.Properties()
                    .food(ModFoods.NOURISHCAN))));
    public static final RegistryObject<Item> GINGERBREADMAN = ITEMS.register("gingerbreadman",
            () -> new Item((new Item.Properties()
                    .food(ModFoods.NOURISHCAN))));
    public static final RegistryObject<Item> ORANGEL = ITEMS.register("orangel",
            () -> new Item((new Item.Properties()
                    .food(ModFoods.NOURISHCAN))));
    public static final RegistryObject<Item> ENGLISHTEA = ITEMS.register("englishtea",
            () -> new Item((new Item.Properties()
                    .food(ModFoods.NOURISHCAN))));
    public static final RegistryObject<Item> ARMYCAN = ITEMS.register("armycan",
            () -> new Item((new Item.Properties()
                    .food(ModFoods.NOURISHCAN))));
    public static final RegistryObject<Item> AFRICAHEART = ITEMS.register("africaheart",
            () -> new Item((new Item.Properties()
                    .rarity(Rarity.EPIC))));
    public static final RegistryObject<Item> YUMMYNOODLES = ITEMS.register("yummynoodles",
            () -> new Item((new Item.Properties()
                    .food(ModFoods.NOURISHCAN))));
    public static final RegistryObject<Item> VITABLETS = ITEMS.register("vitablets",
            () -> new Item((new Item.Properties()
                    .food(ModFoods.NOURISHCAN))));
    public static final RegistryObject<Item> YOGURT = ITEMS.register("yogurt",
            () -> new Item((new Item.Properties()
                    .food(ModFoods.NOURISHCAN))));
    public static final RegistryObject<Item> FASTNOODLES = ITEMS.register("fastnoodles",
            () -> new Item((new Item.Properties()
                    .food(ModFoods.NOURISHCAN))));
    public static final RegistryObject<Item> WILDBAR = ITEMS.register("wildbar",
            () -> new Item((new Item.Properties()
                    .food(ModFoods.NOURISHCAN))));
    public static final RegistryObject<Item> NOSUGARBAR = ITEMS.register("nosugarbar",
            () -> new Item((new Item.Properties()
                    .food(ModFoods.NOURISHCAN))));
    public static final RegistryObject<Item> SUGARTRIANGLE = ITEMS.register("sugartriangle",
            () -> new Item((new Item.Properties()
                    .food(ModFoods.NOURISHCAN))));


    private ModItems() { /* no instantiation */ }

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}