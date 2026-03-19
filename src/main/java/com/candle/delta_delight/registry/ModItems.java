package com.candle.delta_delight.registry;

import com.candle.delta_delight.content.GeneralFoodItem;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModItems {

    public static final String MODID = "delta_delight";
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

    public static final RegistryObject<Item> CHAMPAGNE = registerPlacedFood(
            "champagne", ModBlocks.CHAMPAGNE_BLOCK, ModFoods.CHAMPAGNE, Rarity.EPIC, Items.GLASS_BOTTLE, 32, UseAnim.DRINK);
    public static final RegistryObject<Item> CAVIAR = registerPlacedFood(
            "caviar", ModBlocks.CAVIAR_BLOCK, ModFoods.CAVIAR, Rarity.EPIC, null, 16, UseAnim.EAT);
    public static final RegistryObject<Item> TEQUILA = registerPlacedFood(
            "tequila", ModBlocks.TEQUILA_BLOCK, ModFoods.TEQUILA, Rarity.UNCOMMON, Items.GLASS_BOTTLE, 32, UseAnim.DRINK);
    public static final RegistryObject<Item> COFFEE = registerPlacedFood(
            "coffee", ModBlocks.COFFEE_BLOCK, ModFoods.COFFEE, Rarity.UNCOMMON, Items.GLASS_BOTTLE, 16, UseAnim.DRINK);
    public static final RegistryObject<Item> SEAFOODCAN = registerPlacedFood(
            "seafoodcan", ModBlocks.SEAFOODCAN_BLOCK, ModFoods.SEAFOODCAN, Rarity.UNCOMMON, null, 32, UseAnim.EAT);

    public static final RegistryObject<Item> NOURISHCAN = registerFood(
            "nourishcan", ModFoods.NOURISHCAN, null, 32, UseAnim.EAT);
    public static final RegistryObject<Item> LEMONTEA = registerFood(
            "lemontea", ModFoods.LEMONTEA, null, 24, UseAnim.DRINK);
    public static final RegistryObject<Item> GINGERBREADMAN = registerFood(
            "gingerbreadman", ModFoods.GINGERBREADMAN, null, 32, UseAnim.EAT);
    public static final RegistryObject<Item> ORANGEL = registerFood(
            "orangel", ModFoods.ORANGEL, null, 16, UseAnim.EAT);
    public static final RegistryObject<Item> ENGLISHTEA = registerFood(
            "englishtea", ModFoods.ENGLISHTEA, null, 32, UseAnim.DRINK);
    public static final RegistryObject<Item> ARMYCAN = registerFood(
            "armycan", ModFoods.ARMYCAN, null, 32, UseAnim.EAT);
    public static final RegistryObject<Item> AFRICAHEART = ITEMS.register(
            "africaheart", () -> new Item(new Item.Properties().rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> YUMMYNOODLES = registerFood(
            "yummynoodles", ModFoods.YUMMYNOODLES, null, 32, UseAnim.EAT);
    public static final RegistryObject<Item> VITABLETS = registerFood(
            "vitablets", ModFoods.VITABLETS, null, 16, UseAnim.EAT);
    public static final RegistryObject<Item> YOGURT = registerFood(
            "yogurt", ModFoods.YOGURT, null, 24, UseAnim.EAT);
    public static final RegistryObject<Item> FASTNOODLES = registerFood(
            "fastnoodles", ModFoods.FASTNOODLES, null, 32, UseAnim.EAT);
    public static final RegistryObject<Item> WILDBAR = registerFood(
            "wildbar", ModFoods.WILDBAR, null, 16, UseAnim.EAT);
    public static final RegistryObject<Item> NOSUGARBAR = registerFood(
            "nosugarbar", ModFoods.NOSUGARBAR, null, 16, UseAnim.EAT);
    public static final RegistryObject<Item> SUGARTRIANGLE = registerFood(
            "sugartriangle", ModFoods.SUGARTRIANGLE, null, 24, UseAnim.EAT);

    private static RegistryObject<Item> registerFood(String name, FoodProperties food,
                                                     Item returnItem, int useDuration, UseAnim useAnim) {
        return ITEMS.register(name, () -> new GeneralFoodItem(
                null,
                new Item.Properties().food(food),
                returnItem,
                useDuration,
                useAnim
        ));
    }

    private static RegistryObject<Item> registerPlacedFood(String name, RegistryObject<Block> block,
                                                           FoodProperties food, Rarity rarity,
                                                           Item returnItem, int useDuration, UseAnim useAnim) {
        return ITEMS.register(name, () -> new GeneralFoodItem(
                block.get(),
                new Item.Properties().food(food).rarity(rarity),
                returnItem,
                useDuration,
                useAnim
        ));
    }

    private ModItems() {
    }

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}
