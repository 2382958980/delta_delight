package com.candle.delta_delight.registry;

import com.candle.delta_delight.cocktail.CocktailItem;
import com.candle.delta_delight.content.DDFoodItem;
import com.candle.delta_delight.content.DDPlaceableFoodBlockItem;
import com.candle.delta_delight.content.ShakerItem;
import com.candle.delta_delight.content.TooltipItem;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@SuppressWarnings("unused")
public final class ModItems {
    public static final String MODID = "delta_delight";
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

    public static final RegistryObject<Item> OLIVIA_CHAMPAGNE = registerPlaceableFood(
            "olivia_champagne", ModBlocks.OLIVIA_CHAMPAGNE_BLOCK, ModFoods.OLIVIA_CHAMPAGNE, Rarity.EPIC, Items.GLASS_BOTTLE, 32, UseAnim.DRINK);
    public static final RegistryObject<Item> DIAMOND_CAVIAR = registerPlaceableFood(
            "diamond_caviar", ModBlocks.DIAMOND_CAVIAR_BLOCK, ModFoods.DIAMOND_CAVIAR, Rarity.EPIC, Items.GOLD_INGOT, 16, UseAnim.EAT);
    public static final RegistryObject<Item> BLUE_SAPPHIRE_TEQUILA = registerPlaceableFood(
            "blue_sapphire_tequila", ModBlocks.BLUE_SAPPHIRE_TEQUILA_BLOCK, ModFoods.BLUE_SAPPHIRE_TEQUILA, Rarity.UNCOMMON, Items.GLASS_BOTTLE, 32, UseAnim.DRINK);
    public static final RegistryObject<Item> COFFEE = registerPlaceableFood(
            "coffee", ModBlocks.COFFEE_BLOCK, ModFoods.COFFEE, Rarity.UNCOMMON, Items.GLASS_BOTTLE, 16, UseAnim.DRINK);
    public static final RegistryObject<Item> SEAFOOD_CANNED_PORRIDGE = registerPlaceableFood(
            "seafood_canned_porridge", ModBlocks.SEAFOOD_CANNED_PORRIDGE_BLOCK, ModFoods.SEAFOOD_CANNED_PORRIDGE, Rarity.UNCOMMON, Items.IRON_INGOT, 32, UseAnim.EAT);
    public static final RegistryObject<Item> NUTRITIOUS_CANNED_PORRIDGE = registerPlaceableFood(
            "nutritious_canned_porridge", ModBlocks.NUTRITIOUS_CANNED_PORRIDGE_BLOCK, ModFoods.NUTRITIOUS_CANNED_PORRIDGE, Rarity.UNCOMMON, Items.IRON_INGOT, 32, UseAnim.EAT);
    public static final RegistryObject<Item> LEMON_TEA = registerFood(
            "lemon_tea", ModFoods.LEMON_TEA, Items.IRON_INGOT, 24, UseAnim.DRINK);
    public static final RegistryObject<Item> GINGERBREAD_MAN = registerFood(
            "gingerbread_man", ModFoods.GINGERBREAD_MAN, null, 32, UseAnim.EAT);
    public static final RegistryObject<Item> ORANGE_ENERGY_GEL = registerFood(
            "orange_energy_gel", ModFoods.ORANGE_ENERGY_GEL, null, 16, UseAnim.EAT);
    public static final RegistryObject<Item> ENGLISH_TEA_BAG = registerFood(
            "english_tea_bag", ModFoods.ENGLISH_TEA_BAG, Items.IRON_INGOT, 32, UseAnim.EAT);
    public static final RegistryObject<Item> COKE = registerFood(
            "coke", ModFoods.COKE, Items.IRON_INGOT, 32, UseAnim.DRINK);
    public static final RegistryObject<Item> CANNED_RATION = registerFood(
            "canned_ration", ModFoods.CANNED_RATION, Items.IRON_INGOT, 32, UseAnim.EAT);
    public static final RegistryObject<Item> HEART_OF_AFRICA = ITEMS.register(
            "heart_of_africa", () -> new Item(new Item.Properties().rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> FIRED_NODDLES = registerFood(
            "fired_noddles", ModFoods.FIRED_NODDLES, null, 32, UseAnim.EAT);
    public static final RegistryObject<Item> VITAMIN_EFFERVESCENT_TABLET = registerFood(
            "vitamin_effervescent_tablet", ModFoods.VITAMIN_EFFERVESCENT_TABLET, null, 16, UseAnim.EAT);
    public static final RegistryObject<Item> YOGURT = registerFood(
            "yogurt", ModFoods.YOGURT, null, 24, UseAnim.EAT);
    public static final RegistryObject<Item> CRISPY_NODDLES = registerFood(
            "crispy_noddles", ModFoods.CRISPY_NODDLES, null, 32, UseAnim.EAT);
    public static final RegistryObject<Item> FIELD_ENERGY_BAR = registerFood(
            "field_energy_bar", ModFoods.FIELD_ENERGY_BAR, null, 16, UseAnim.EAT);
    public static final RegistryObject<Item> SUGAR_FREE_ENERGY_BAR = registerFood(
            "sugar_free_energy_bar", ModFoods.SUGAR_FREE_ENERGY_BAR, null, 16, UseAnim.EAT);
    public static final RegistryObject<Item> CHOCOLATE = registerFood(
            "chocolate", ModFoods.CHOCOLATE, null, 24, UseAnim.EAT);
    public static final RegistryObject<Item> PORRIDGE = registerFood(
            "porridge", ModFoods.PORRIDGE, null, 32, UseAnim.EAT);
    public static final RegistryObject<Item> HERBAL_TEA = ITEMS.register(
            "herbal_tea", () -> new TooltipItem(new Item.Properties().craftRemainder(Items.GLASS_BOTTLE)));
    public static final RegistryObject<Item> MOLASSES = ITEMS.register(
            "molasses", () -> new TooltipItem(new Item.Properties().craftRemainder(Items.GLASS_BOTTLE)));
    public static final RegistryObject<Item> JUNIPER_SPIRIT = ITEMS.register(
            "juniper_spirit", () -> new TooltipItem(new Item.Properties().craftRemainder(Items.GLASS_BOTTLE)));
    public static final RegistryObject<Item> AMBER_ESSENCE = ITEMS.register(
            "amber_essence", () -> new TooltipItem(new Item.Properties().craftRemainder(Items.GLASS_BOTTLE)));
    public static final RegistryObject<Item> MIXED_COCKTAIL = ITEMS.register(
            "mixed_cocktail", () -> new CocktailItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> SHAKER = ITEMS.register(
            "shaker", () -> new ShakerItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> BOOK = ITEMS.register(
            "book",() -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> DAWN_MUSIC_DISC = registerMusicDisc(
            "dawn_music_disc", ModSoundEvents.MUSIC_DISC_DAWN, 8, 243);
    public static final RegistryObject<Item> IN_THE_SUMMER_MUSIC_DISC = registerMusicDisc(
            "in_the_summer_music_disc", ModSoundEvents.MUSIC_DISC_IN_THE_SUMMER, 14, 181);
    public static final RegistryObject<Item> KING_OF_THE_RING_MUSIC_DISC = registerMusicDisc(
            "king_of_the_ring_music_disc", ModSoundEvents.MUSIC_DISC_KING_OF_THE_RING, 9, 203);
    public static final RegistryObject<Item> MAKING_LEGENDS_MUSIC_DISC = registerMusicDisc(
            "making_legends_music_disc", ModSoundEvents.MUSIC_DISC_MAKING_LEGENDS, 10, 126);
    public static final RegistryObject<Item> MENU1_MUSIC_DISC = registerMusicDisc(
            "menu1_music_disc", ModSoundEvents.MUSIC_DISC_MENU1, 11, 71);
    public static final RegistryObject<Item> MENU2_MUSIC_DISC = registerMusicDisc(
            "menu2_music_disc", ModSoundEvents.MUSIC_DISC_MENU2, 12, 111);
    public static final RegistryObject<Item> PLAY_WITH_FIRE_MUSIC_DISC = registerMusicDisc(
            "play_with_fire_music_disc", ModSoundEvents.MUSIC_DISC_PLAY_WITH_FIRE, 13, 165);
    public static final RegistryObject<Item> SPOTLIGHT_HUNTER_MUSIC_DISC = registerMusicDisc(
            "spotlight_hunter_music_disc", ModSoundEvents.MUSIC_DISC_SPOTLIGHT_HUNTER, 15, 174);

    private static RegistryObject<Item> registerFood(String name, FoodProperties food,
                                                     Item returnItem, int useDuration, UseAnim useAnim) {
        return ITEMS.register(name, () -> {
            Item.Properties properties = new Item.Properties().food(food).rarity(Rarity.COMMON);
            if (returnItem != null) {
                properties.craftRemainder(returnItem);
            }
            return new DDFoodItem(properties, returnItem, useDuration, useAnim);
        });
    }

    private static RegistryObject<Item> registerPlaceableFood(String name, RegistryObject<Block> block,
                                                              FoodProperties food, Rarity rarity,
                                                              Item returnItem, int useDuration, UseAnim useAnim) {
        return ITEMS.register(name, () -> {
            Item.Properties properties = new Item.Properties().food(food).rarity(rarity);
            if (returnItem != null) {
                properties.craftRemainder(returnItem);
            }
            return new DDPlaceableFoodBlockItem(block.get(), properties, returnItem, useDuration, useAnim);
        });
    }

    private static RegistryObject<Item> registerMusicDisc(String name, RegistryObject<SoundEvent> sound,
                                                          int analogOutput, int durationSeconds) {
        return ITEMS.register(name, () -> new RecordItem(
                analogOutput,
                sound,
                new Item.Properties().stacksTo(1).rarity(Rarity.RARE),
                durationSeconds * 20
        ));
    }
}
