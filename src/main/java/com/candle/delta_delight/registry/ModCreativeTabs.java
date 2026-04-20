package com.candle.delta_delight.registry;

import com.candle.delta_delight.DeltaDelight;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

@SuppressWarnings("unused")
public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, DeltaDelight.MODID);

    public static final List<RegistryObject<Item>> TAB_ITEMS = List.of(
            ModItems.HEART_OF_AFRICA,
            ModItems.OLIVIA_CHAMPAGNE,
            ModItems.DIAMOND_CAVIAR,
            ModItems.BLUE_SAPPHIRE_TEQUILA,
            ModItems.COFFEE,
            ModItems.SEAFOOD_CANNED_PORRIDGE,
            ModItems.NUTRITIOUS_CANNED_PORRIDGE,
            ModItems.PORRIDGE,
            ModItems.LEMON_TEA,
            ModItems.GINGERBREAD_MAN,
            ModItems.ORANGE_ENERGY_GEL,
            ModItems.ENGLISH_TEA_BAG,
            ModItems.COKE,
            ModItems.CANNED_RATION,
            ModItems.CHOCOLATE,
            ModItems.FIRED_NODDLES,
            ModItems.VITAMIN_EFFERVESCENT_TABLET,
            ModItems.SUGAR_FREE_ENERGY_BAR,
            ModItems.FIELD_ENERGY_BAR,
            ModItems.YOGURT,
            ModItems.CRISPY_NODDLES,
            ModItems.SHAKER,
            ModItems.JUNIPER_SPIRIT,
            ModItems.HERBAL_TEA,
            ModItems.AMBER_ESSENCE,
            ModItems.MOLASSES,
            ModItems.RECON_ARROW,
            ModItems.DAWN_MUSIC_DISC,
            ModItems.SPOTLIGHT_HUNTER_MUSIC_DISC,
            ModItems.IN_THE_SUMMER_MUSIC_DISC,
            ModItems.KING_OF_THE_RING_MUSIC_DISC,
            ModItems.MAKING_LEGENDS_MUSIC_DISC,
            ModItems.MENU1_MUSIC_DISC,
            ModItems.MENU2_MUSIC_DISC,
            ModItems.PLAY_WITH_FIRE_MUSIC_DISC
    );

    public static final RegistryObject<CreativeModeTab> DELTA_DELIGHT = TABS.register(
            DeltaDelight.MODID,
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup." + DeltaDelight.MODID))
                    .icon(() -> new ItemStack(ModItems.HEART_OF_AFRICA.get()))
                    .displayItems((params, output) -> TAB_ITEMS.forEach(item -> output.accept(item.get())))
                    .build()
    );
}
